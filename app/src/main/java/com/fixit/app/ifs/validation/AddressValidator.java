package com.fixit.app.ifs.validation;

import android.os.AsyncTask;

import com.fixit.app.ifs.geodata.GeocodeResponse;
import com.fixit.app.ifs.geodata.GeocodeResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by konstantin on 3/29/2017.
 */

public class AddressValidator {

    public void validate(String address, AddressValidationCallback callback) {
        new ValidationTask(callback).execute(address);
    }

    private static class ValidationTask extends AsyncTask<String, Void, Boolean> {

        private final AddressValidationCallback mCallback;

        private ValidationTask(AddressValidationCallback callback) {
            this.mCallback = callback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String address = params[0];

            OkHttpClient httpClient = new OkHttpClient.Builder().build();

            try {
                Request request = new Request.Builder()
                        .url(String.format(
                                "https://maps.googleapis.com/maps/api/geocode/json?address=%s",
                                URLEncoder.encode(address, "utf-8")
                        ))
                        .get()
                        .build();

                Response response = httpClient.newCall(request).execute();

                GeocodeResponse geocodeResponse = new Gson().fromJson(response.body().charStream(), GeocodeResponse.class);

                if(geocodeResponse.getStatus().equals("OK")) {
                    for(GeocodeResult result : geocodeResponse.getResults()) {
                        if(result.getTypes().contains("street_address")) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                mCallback.onValidationError("Could not execute request to google apis geocoder.", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isValid) {
            mCallback.onAddressValidated(isValid);
        }
    }

    public interface AddressValidationCallback {
        public void onAddressValidated(boolean isValid);
        public void onValidationError(String error, Throwable t);
    }

}
