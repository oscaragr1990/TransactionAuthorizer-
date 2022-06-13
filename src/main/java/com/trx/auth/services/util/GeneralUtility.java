package com.trx.auth.services.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.trx.auth.enums.ViolationsEnum;
import com.trx.auth.model.Account;
import com.trx.auth.model.Response;
import com.trx.auth.model.Transaction;

public class GeneralUtility {

	private GeneralUtility() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * 
	 * @param inDate
	 * @param inStartDate
	 * @param inEndDate
	 * @return
	 */
	public static boolean isWithinRange(Date inDate, Date inStartDate, Date inEndDate) {
		return !(inDate.before(inStartDate) || inDate.after(inEndDate));
	}

	/**
	 * 
	 * @param inTrx01
	 * @param inTrx02
	 * @param inStartDate
	 * @param inEndDate
	 * @return
	 */
	public static boolean verifyTrxReplication(Transaction inTrx01, Transaction inTrx02, Date inStartDate,
			Date inEndDate) {
		boolean replicaTrx = inTrx01.getMerchant().equals(inTrx02.getMerchant())
				&& inTrx01.getAmount().equals(inTrx02.getAmount());

		if (replicaTrx && inTrx01.getTime().equals(inTrx02.getTime())) {
			return replicaTrx;
		}
		return replicaTrx && isWithinRange(inTrx01.getTime(), inStartDate, inEndDate);
	}

	/**
	 * 
	 * @param inDate
	 * @param inDays
	 * @return
	 */
	public static Date addDaysDate(Date inDate, Integer inDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(inDate);
		cal.add(Calendar.MINUTE, inDays);
		return cal.getTime();
	}

	/**
	 * 
	 * Method to build the response
	 * 
	 * @param inAccount       Account
	 * @param inlstViolations List of violations
	 * @return
	 */
	public static Response buildOutput(Account inAccount, Set<ViolationsEnum> inlstViolations) {
		Response out = new Response();
		out.setAccount(inAccount);
		out.setViolations(new HashSet<>());
		for (ViolationsEnum violation : inlstViolations) {
			out.getViolations().add(violation.getDescription());
		}
		return out;
	}

}
