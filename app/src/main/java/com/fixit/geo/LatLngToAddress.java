package com.fixit.geo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.fixit.app.R;
import com.fixit.general.BiFunction;
import com.fixit.general.Function;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kostyantin on 11/2/2017.
 */
public class LatLngToAddress implements BiFunction<Context, LatLng, FetchAddressIntentService.Result> {
    @Override
    public FetchAddressIntentService.Result apply(Context context, LatLng latLng) {
        // Errors could still arise from using the Geocoder (for example, if there is no
        // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
        // simply not have an address for a location. In all these cases, we communicate with the
        // receiver using a resultCode indicating failure. If an address is found, we use a
        // resultCode indicating success.

        // The Geocoder used in this sample. The Geocoder's responses are localized for the given
        // Locale, which represents a specific geographical or linguistic region. Locales are used
        // to alter the presentation of information such as numbers or dates to suit the conventions
        // in the region they describe.
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        String errorMessage;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    // In this sample, we get just a single address.
                    1);

            errorMessage = null;
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = context.getString(R.string.service_not_available);
            Log.e(Constants.LOG_TAG_FETCH_ADDRESS, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = context.getString(R.string.invalid_lat_long_used);
            Log.e(Constants.LOG_TAG_FETCH_ADDRESS, errorMessage + ". " +
                    "Latitude = " + latLng.latitude +
                    ", Longitude = " + latLng.longitude, illegalArgumentException);
        }


        // Handle case where no address was found.
        if ((addresses == null || addresses.size()  == 0) && errorMessage == null) {
            errorMessage = context.getString(R.string.no_address_found);
            FILog.e(Constants.LOG_TAG_FETCH_ADDRESS, errorMessage);
        }

        if(errorMessage != null) {
            return new FetchAddressIntentService.Result(errorMessage);
        } else {
            return new FetchAddressIntentService.Result(addresses.get(0));
        }
    }
}
