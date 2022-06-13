package com.trx.auth.model;

import java.util.Set;

public class Response {
	private Account account;
	private Set<String> violations;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Set<String> getViolations() {
		return violations;
	}

	public void setViolations(Set<String> violations) {
		this.violations = violations;
	}

	@Override
	public String toString() {
		return "Response [account=" + account + ", violations=" + violations + "]";
	}

}
