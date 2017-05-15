package com.fixit.core.rest.responses.data;

/**
 * Created by konstantin on 5/15/2017.
 */

public class TelephoneVerificationResponseData {

    private boolean validTelephoneNumber;
    private int verificationCode;

    public TelephoneVerificationResponseData() { }

    public boolean isValidTelephoneNumber() {
        return validTelephoneNumber;
    }

    public void setValidTelephoneNumber(boolean validTelephoneNumber) {
        this.validTelephoneNumber = validTelephoneNumber;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "TelephoneVerificationResponseData{" +
                "validTelephoneNumber=" + validTelephoneNumber +
                ", verificationCode=" + verificationCode +
                '}';
    }
}
