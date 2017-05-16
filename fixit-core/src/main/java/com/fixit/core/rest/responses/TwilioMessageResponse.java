package com.fixit.core.rest.responses;

import com.fixit.core.rest.apis.twilio.TwilioMessage;

import java.util.Date;
import java.util.Map;

/**
 * Created by konstantin on 5/16/2017.
 *
 */
public class TwilioMessageResponse {

    private String sid;
    private Date date_created;
    private Date date_updated;
    private Date data_sent;
    private String account_sid;
    private String to;
    private String from;
    private String body;
    private String status;
    private String num_segments;
    private String num_media;
    private String direction;
    private String api_version;
    private String price;
    private String price_unit;
    private String error_code;
    private String error_message;
    private String uri;
    private Map<String, String> subresource_uris;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    public Date getData_sent() {
        return data_sent;
    }

    public void setData_sent(Date data_sent) {
        this.data_sent = data_sent;
    }

    public String getAccount_sid() {
        return account_sid;
    }

    public void setAccount_sid(String account_sid) {
        this.account_sid = account_sid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNum_segments() {
        return num_segments;
    }

    public void setNum_segments(String num_segments) {
        this.num_segments = num_segments;
    }

    public String getNum_media() {
        return num_media;
    }

    public void setNum_media(String num_media) {
        this.num_media = num_media;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getSubresource_uris() {
        return subresource_uris;
    }

    public void setSubresource_uris(Map<String, String> subresource_uris) {
        this.subresource_uris = subresource_uris;
    }

    @Override
    public String toString() {
        return "TwilioMessage{" +
                "sid='" + sid + '\'' +
                ", date_created=" + date_created +
                ", date_updated=" + date_updated +
                ", data_sent=" + data_sent +
                ", account_sid='" + account_sid + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", body='" + body + '\'' +
                ", status='" + status + '\'' +
                ", num_segments='" + num_segments + '\'' +
                ", num_media='" + num_media + '\'' +
                ", direction='" + direction + '\'' +
                ", api_version='" + api_version + '\'' +
                ", price='" + price + '\'' +
                ", price_unit='" + price_unit + '\'' +
                ", error_code='" + error_code + '\'' +
                ", error_message='" + error_message + '\'' +
                ", uri='" + uri + '\'' +
                ", subresource_uris=" + subresource_uris +
                '}';
    }
}
