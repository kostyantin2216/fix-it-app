package com.fixit.external;

/**
 * Created by konstantin on 5/10/2017.
 */

public class ExternalLogin {

    enum Status {
        SUCCESS,
        ERROR,
        CANCELED,
        NO_PROFILE,
        NO_EMAIL
    }

    enum Source {
        FACEBOOK,
        GOOGLE
    }

    interface Callback {
        void onLoggedIn(Source source, Status status, String firstName, String lastName, String email);
    }

}
