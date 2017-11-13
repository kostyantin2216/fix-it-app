package com.fixit.geo;

import android.content.Context;
import android.location.Address;

import com.fixit.app.R;
import com.fixit.config.AppConfig;
import com.fixit.data.MutableLatLng;
import com.fixit.general.BiFunction;
import com.fixit.general.Function;
import com.fixit.geo.data.AddressComponent;
import com.fixit.geo.data.GeocodeResponse;
import com.fixit.geo.data.GeocodeResult;
import com.fixit.utils.CommonUtils;
import com.fixit.utils.FILog;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kostyantin on 11/2/2017.
 */
public class TextToAddress implements BiFunction<Context, String, FetchAddressIntentService.Result> {

    @Override
    public FetchAddressIntentService.Result apply(Context context, String text) {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        String errorMessage;
        Address address = null;

        try {
            String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",
                    URLEncoder.encode(text, "utf-8"));

            FILog.i("Fetching geo data from " + url);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            int maxRequestRetries = AppConfig.getInteger(context, AppConfig.KEY_MAX_ADDRESS_VALIDATION_REQUEST_RETRIES, 3);
            GeocodeResponse geocodeResponse = parseAddress(httpClient, request, 0, maxRequestRetries);

            if(geocodeResponse.getStatus().equals("OK")) {
                List<GeocodeResult> results = geocodeResponse.getResults();
                if(results.size() == 0) {
                    errorMessage = context.getString(R.string.could_not_find_location);
                } else {
                    errorMessage = null;
                    address = new Address(Locale.getDefault());
                    GeocodeResult geocodeResult = results.get(0);

                    Map<AddressComponent.Type, AddressComponent> addressComponentsByType = AddressComponent.sortByType(geocodeResult.getAddress_components());
                    for (Map.Entry<AddressComponent.Type, AddressComponent> addressComponentForType : addressComponentsByType.entrySet()) {
                        AddressComponent addressComponent = addressComponentForType.getValue();
                        switch (addressComponentForType.getKey()) {
                            case street_number:
                                address.setSubThoroughfare(addressComponent.getLong_name());
                                break;
                            case route:
                                address.setThoroughfare(addressComponent.getLong_name());
                                break;
                            case sublocality:
                                address.setSubLocality(addressComponent.getLong_name());
                                break;
                            case locality:
                                address.setLocality(addressComponent.getLong_name());
                                break;
                            case administrative_area_level_1:
                                address.setAdminArea(addressComponent.getLong_name());
                                break;
                            case postal_code:
                                address.setPostalCode(addressComponent.getLong_name());
                                break;
                            case country:
                                address.setCountryName(addressComponent.getLong_name());
                                break;
                        }
                    }

                    address.setAddressLine(0, geocodeResult.getFormatted_address());

                    MutableLatLng location = geocodeResult.getGeometry().getLocation();
                    address.setLatitude(location.getLat());
                    address.setLongitude(location.getLng());
                }
            } else {
                errorMessage = context.getString(R.string.invalid_location);
            }

        } catch (IOException e) {
            errorMessage = context.getString(R.string.geocoder_unavailable);
        }

        if(errorMessage != null) {
            return new FetchAddressIntentService.Result(errorMessage);
        } else {
            return new FetchAddressIntentService.Result(address);
        }
    }

    private GeocodeResponse parseAddress(OkHttpClient httpClient, Request request, int retries, int maxRetries) throws IOException {
        Response response = httpClient.newCall(request).execute();

        GeocodeResponse geocodeResponse = new Gson().fromJson(response.body().charStream(), GeocodeResponse.class);

        if(geocodeResponse.getStatus().equals("OVER_QUERY_LIMIT") && retries < maxRetries) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            geocodeResponse = parseAddress(httpClient, request, ++retries, maxRetries);
        }

        return geocodeResponse;
    }
}
