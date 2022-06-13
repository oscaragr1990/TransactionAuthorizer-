package com.trx.auth.services;

import java.util.List;
import java.util.Set;

import com.trx.auth.model.Account;
import com.trx.auth.model.Response;
import com.trx.auth.model.Transaction;

public interface TrxService {

	/**
	 * Account creation
	 * 
	 * @param inAccount
	 * @return
	 */
	public Response accountCreate(Account inAccount);

	/**
	 * Transaction authorization
	 * 
	 * @param inTransaction
	 * @return
	 */
	public Response authorizeTrx(Transaction inTransaction);

	/**
	 * Reset temporary account information
	 */
	public void resetAccounts();

	/**
	 * Check existing accounts
	 * 
	 * @return
	 */
	public List<Account> consultAccounts();

	/**
	 * check the transactions of an account
	 * 
	 * @return
	 */
	public Set<Transaction> consultTrx(Integer id);

}
