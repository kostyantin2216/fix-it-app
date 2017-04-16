package com.fixit.app.ifs.validation;

import android.os.AsyncTask;

import com.fixit.app.ifs.geodata.AddressComponent;
import com.fixit.app.ifs.geodata.GeocodeResponse;
import com.fixit.app.ifs.geodata.GeocodeResult;
import com.fixit.core.data.JobLocation;
import com.fixit.core.data.MutableLatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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

    private static class ValidationTask extends AsyncTask<String, Void, AddressValidationResult> {

        private final AddressValidationCallback mCallback;

        private ValidationTask(AddressValidationCallback callback) {
            this.mCallback = callback;
        }

        @Override
        protected AddressValidationResult doInBackground(String... params) {
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
                            return new AddressValidationResult(geocodeResponse);
                        }
                    }
                }
            } catch (IOException e) {
                mCallback.onValidationError("Could not execute request to google apis geocoder.", e);
            }

            return new AddressValidationResult(null);
        }

        @Override
        protected void onPostExecute(AddressValidationResult result) {
            mCallback.onAddressValidated(result);
        }
    }

    public static class AddressValidationResult {
        public final JobLocation jobLocation;

        private AddressValidationResult(GeocodeResponse geocodeResponse) {
            if(geocodeResponse != null && geocodeResponse.getStatus().equals("OK")) {
                List<GeocodeResult> results = geocodeResponse.getResults();
                GeocodeResult result = null;
                for(GeocodeResult tempResult : results) {
                    if(tempResult.getTypes().contains("street_address")) {
                        result = tempResult;
                        break;
                    }
                }

                if(result != null) {
                    jobLocation = new JobLocation();

                    Map<AddressComponent.Type, AddressComponent> addressComponentsByType = AddressComponent.sortByType(result.getAddress_components());

                    for(Map.Entry<AddressComponent.Type, AddressComponent> addressComponentForType : addressComponentsByType.entrySet()) {
                        AddressComponent addressComponent = addressComponentForType.getValue();
                        switch (addressComponentForType.getKey()) {
                            case street_number:
                                jobLocation.setStreetNum(Integer.valueOf(addressComponent.getLong_name()));
                                break;
                            case route:
                                jobLocation.setStreet(addressComponent.getLong_name());
                                break;
                            case sublocality:
                                jobLocation.setNeighborhood(addressComponent.getLong_name());
                                break;
                            case locality:
                                jobLocation.setCity(addressComponent.getLong_name());
                                break;
                            case administrative_area_level_1:
                                jobLocation.setProvince(addressComponent.getLong_name());
                                break;
                            case postal_code:
                                jobLocation.setZipCode(addressComponent.getLong_name());
                                break;
                        }
                    }

                    MutableLatLng latLng = result.getGeometry().getLocation();
                    jobLocation.setLat(latLng.getLat());
                    jobLocation.setLng(latLng.getLng());
                } else {
                    jobLocation = null;
                }
            } else {
                this.jobLocation = null;
            }
        }
    }

    public interface AddressValidationCallback {
        public void onAddressValidated(AddressValidationResult result);
        public void onValidationError(String error, Throwable t);
    }

}
