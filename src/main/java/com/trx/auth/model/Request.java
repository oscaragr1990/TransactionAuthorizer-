package com.trx.auth.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request implements Serializable {

	private static final long serialVersionUID = 7999626518149274354L;

	@JsonProperty("account")
	private Account account;
	@JsonProperty("transaction")
	private Transaction transaction;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "Request [account=" + account + ", transaction=" + transaction + "]";
	}

}
