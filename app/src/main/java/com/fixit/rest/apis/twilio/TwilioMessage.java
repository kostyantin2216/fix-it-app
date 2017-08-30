package com.fixit.rest.apis.twilio;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konstantin on 5/16/2017.
 */

public class TwilioMessage {

    private final static String FIELD_FROM = "From";
    private final static String FIELD_TO = "To";
    private final static String FIELD_BODY = "Body";
    private final static String FIELD_PROVIDE_FEEDBACK = "ProvideFeedback";

    private final String from;
    private final String to;
    private final String body;
    private final boolean provideFeedback;

    private TwilioMessage(TwilioMessage.Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.body = builder.body;
        this.provideFeedback = builder.provideFeedback;
    }

    public Map<String, String> toFieldMap() {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put(FIELD_FROM, from);
        fieldMap.put(FIELD_TO, to);
        fieldMap.put(FIELD_BODY, body);

        if(provideFeedback) {
            fieldMap.put(FIELD_PROVIDE_FEEDBACK, "true");
        }

        return fieldMap;
    }

    public static class Builder {
        private String from;
        private String to;
        private String body;
        private boolean provideFeedback = false;

        public Builder(String from, String to, String body) {
            this.from = from;
            this.to = to;
            this.body = body;
        }

        public Builder provideFeedback() {
            this.provideFeedback = true;
            return this;
        }

        public TwilioMessage build() {
            return new TwilioMessage(this);
        }
    }

}
