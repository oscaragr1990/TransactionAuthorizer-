package com.trx.auth.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trx.auth.enums.ViolationsEnum;
import com.trx.auth.model.Account;
import com.trx.auth.model.Response;
import com.trx.auth.model.Transaction;
import com.trx.auth.services.TrxService;
import com.trx.auth.services.util.GeneralUtility;

@Service
public class TrxServiceImpl implements TrxService {

	private Map<Integer, Account> listAccounts = new HashMap<>();

	@Override
	public Response accountCreate(Account inAccount) {
		Set<ViolationsEnum> lstV = runAccountBusinessRules(true, inAccount);
		if (lstV.isEmpty()) {
			listAccounts.put(inAccount.getId(), inAccount);
		}
		return GeneralUtility.buildOutput(inAccount, lstV);

	}

	@Override
	public Response authorizeTrx(Transaction inTransaction) {
		Set<ViolationsEnum> lstV = runAccountBusinessTx(inTransaction);
		if (lstV.isEmpty()) {
			updateAccount(inTransaction);
		}

		Account auxAccount = listAccounts.get(inTransaction.getId());

		return GeneralUtility.buildOutput(auxAccount == null ? null : new Account(auxAccount), lstV);
	}

	/**
	 * Run business rules for accounts
	 * 
	 * @param inAccount
	 * @return
	 */
	private Set<ViolationsEnum> runAccountBusinessRules(boolean isNewAccount, Account inAccount) {
		Set<ViolationsEnum> listViolations = new HashSet<>();

		// account-already-initialized

		if (isNewAccount) {
			Account account = listAccounts.get(inAccount.getId());
			if (account != null) {
				listViolations.add(ViolationsEnum.ACCOUNT_ALREADY_INITIALIZED);
			}
		} else {
			// account-not-initialized
			if (inAccount == null) {
				listViolations.add(ViolationsEnum.ACCOUNT_NOT_INITIALIZED);
			} else {
				// card-not-active
				if (!inAccount.isActiveCard()) {
					listViolations.add(ViolationsEnum.CARD_NOT_ACTIVE);
				}
			}
		}
		return listViolations;
	}

	/**
	 * Execute business rules for transactions
	 * 
	 * @param inTrx
	 * @return
	 */
	private Set<ViolationsEnum> runAccountBusinessTx(Transaction inTrx) {

		Integer rgTxMn = 2;
		Integer countTxMn = 3;
		Integer countTxDp = 1;

		Set<ViolationsEnum> listViolations = runAccountBusinessRules(false, listAccounts.get(inTrx.getId()));

		if (!listViolations.isEmpty()) {
			return listViolations;
		}

		// Insufficient-limit
		if (listAccounts.get(inTrx.getId()).getAvailableLimit() < inTrx.getAmount()) {
			listViolations.add(ViolationsEnum.INSUFFICIENT_LIMIT);
		}

		// high-frequency-small-interval / doubled-transaction
		List<Transaction> lstTrx = this.listAccounts.get(inTrx.getId()).getTransactions().stream()
				.collect(Collectors.toList());
		lstTrx.add(inTrx);

		if (!lstTrx.isEmpty()) {
			List<Transaction> listTxReplica = new ArrayList<>();
			List<Transaction> listTxFreq = new ArrayList<>();

			Date limS = GeneralUtility.addDaysDate(inTrx.getTime(), -rgTxMn);
			Date limE = GeneralUtility.addDaysDate(inTrx.getTime(), rgTxMn);

			for (Transaction trx : lstTrx) {
				if (GeneralUtility.verifyTrxReplication(trx, inTrx, limS, limE)) {
					listTxReplica.add(trx);
				}
				if (GeneralUtility.isWithinRange(trx.getTime(), limS, limE)) {
					listTxFreq.add(trx);
				}
			}

			// high-frequency-small-interval
			if (listTxFreq.size() > countTxMn) {
				listViolations.add(ViolationsEnum.HIGHT_FREQUENCY_SMALL_INTERVAL);
			}
			// doubled-transaction
			if (listTxReplica.size() > countTxDp) {
				listViolations.add(ViolationsEnum.DOUBLED_TRANSACTION);
			}
		}

		return listViolations;

	}

	/**
	 * Update account information
	 * 
	 * @param inTrx new transaction
	 */
	private void updateAccount(Transaction inTrx) {
		int availableLimit = this.listAccounts.get(inTrx.getId()).getAvailableLimit();
		this.listAccounts.get(inTrx.getId()).setAvailableLimit(availableLimit - inTrx.getAmount());
		this.listAccounts.get(inTrx.getId()).getTransactions().add(inTrx);
	}

	@Override
	public void resetAccounts() {
		this.listAccounts = new HashMap<>();
	}

	@Override
	public List<Account> consultAccounts() {
		return listAccounts.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
	}

	@Override
	public Set<Transaction> consultTrx(Integer id) {
		return listAccounts.get(id).getTransactions();
	}

}
