package com.fixit.core.rest.requests.data;

/**
 * Created by konstantin on 5/15/2017.
 */

public class TelephoneVerificationRequestData {

    private String telephone;

    public TelephoneVerificationRequestData() { }

    public TelephoneVerificationRequestData(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "TelephoneVerificationRequestData{" +
                "telephone='" + telephone + '\'' +
                '}';
    }
}
