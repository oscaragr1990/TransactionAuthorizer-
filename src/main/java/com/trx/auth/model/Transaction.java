package com.trx.auth.model;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {

	private static final long serialVersionUID = -6508821439615756635L;

	private Integer id;
	private String merchant;
	private Integer amount;
	private Date time;

	public Transaction() {
		super();
	}

	public Transaction(Integer id, String merchant, Integer amount, Date time) {
		super();
		this.id = id;
		this.merchant = merchant;
		this.amount = amount;
		this.time = time;
	}

	public Integer getId() {
		return id;
	}

	public String getMerchant() {
		return merchant;
	}

	public Integer getAmount() {
		return amount;
	}

	public Date getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", merchant=" + merchant + ", amount=" + amount + ", time=" + time + "]";
	}

}
