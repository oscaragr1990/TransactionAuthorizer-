package com.trx.auth.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account implements Serializable {

	private static final long serialVersionUID = 6379000004719797409L;

	private int id;
	@JsonProperty("active-card")
	private boolean activeCard;
	@JsonProperty("available-limit")
	private Integer availableLimit;
	@JsonIgnore
	private Set<Transaction> transactions;

	public Account() {
		super();
	}

	public Account(Integer id, boolean activeCard, Integer availableLimit, Set<Transaction> transactions) {
		super();
		this.id = id;
		this.activeCard = activeCard;
		this.availableLimit = availableLimit;
		this.transactions = transactions;
	}

	public Account(Account inAccount) {
		super();
		this.id = inAccount.getId();
		this.activeCard = inAccount.isActiveCard();
		this.availableLimit = inAccount.getAvailableLimit();
		this.transactions = inAccount.getTransactions();
	}

	public boolean isActiveCard() {
		return activeCard;
	}

	public void setActiveCard(boolean activeCard) {
		this.activeCard = activeCard;
	}

	public Integer getAvailableLimit() {
		return availableLimit;
	}

	public void setAvailableLimit(Integer availableLimit) {
		this.availableLimit = availableLimit;
	}

	public Set<Transaction> getTransactions() {
		if (transactions == null) {
			transactions = new HashSet<>();
		}
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", activeCard=" + activeCard + ", availableLimit=" + availableLimit + "]";
	}

}
