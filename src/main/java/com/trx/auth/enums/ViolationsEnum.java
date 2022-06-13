package com.trx.auth.enums;

public enum ViolationsEnum {

    ACCOUNT_NOT_INITIALIZED("account-not-initialized"),
    CARD_NOT_ACTIVE("card-not-active"),
    INSUFFICIENT_LIMIT("insufficient-limit"),
    HIGHT_FREQUENCY_SMALL_INTERVAL("high-frequency-small-interval"),
    DOUBLED_TRANSACTION("doubled-transaction"),
    ACCOUNT_ALREADY_INITIALIZED("account-already-initialized");

    private String description;

    ViolationsEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}