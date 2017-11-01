package com.fixit.geo;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.fixit.app.R;
import com.fixit.data.JobLocation;
import com.fixit.utils.CommonUtils;
import com.fixit.utils.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
public class FetchAddressIntentService extends IntentService {
    private static final String TAG = "FetchAddressIS";

    /**
     * The receiver where results are forwarded from this service.
     */
    private ResultReceiver mReceiver;

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public FetchAddressIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    /**
     * Tries to get the location address using a Geocoder. If successful, sends an address to a
     * result receiver. If unsuccessful, sends an error message instead.
     * Note: We define a {@link android.os.ResultReceiver} in * MainActivity to process content
     * sent from this service.
     *
     * This service calls this method from the default worker thread with the intent that started
     * the service. When this method returns, the service automatically stops.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(Constants.ARG_RESULT_RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }


        LatLng latLng = intent.getParcelableExtra(Constants.ARG_LAT_LNG);

        if (latLng == null) {
            Location location = intent.getParcelableExtra(Constants.ARG_LOCATION);

            if(location == null) {
                errorMessage = getString(R.string.no_location_data_provided);
                Log.wtf(TAG, errorMessage);
                deliverErrorToReceiver(errorMessage);
                return;
            } else {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }

        // Errors could still arise from using the Geocoder (for example, if there is no
        // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
        // simply not have an address for a location. In all these cases, we communicate with the
        // receiver using a resultCode indicating failure. If an address is found, we use a
        // resultCode indicating success.

        // The Geocoder used in this sample. The Geocoder's responses are localized for the given
        // Locale, which represents a specific geographical or linguistic region. Locales are used
        // to alter the presentation of information such as numbers or dates to suit the conventions
        // in the region they describe.
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + latLng.latitude +
                    ", Longitude = " + latLng.longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverErrorToReceiver(errorMessage);
        } else {
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(toJobLocation(addresses.get(0)));
        }
    }

    private JobLocation toJobLocation(Address address) {
        JobLocation jobLocation = new JobLocation();
        jobLocation.setStreet(address.getThoroughfare());
        String streetNum = address.getSubThoroughfare();
        if(!CommonUtils.isNumber(streetNum)) {
            streetNum = address.getFeatureName();
        }
        if(CommonUtils.isNumber(streetNum)) {
            jobLocation.setStreetNum(Integer.parseInt(streetNum));
        }
        jobLocation.setCity(address.getLocality());
        jobLocation.setNeighborhood(address.getSubLocality());
        jobLocation.setProvince(address.getAdminArea());
        jobLocation.setZipCode(address.getPostalCode());
        jobLocation.setLat(address.getLatitude());
        jobLocation.setLng(address.getLongitude());

        ArrayList<String> addressFragments = new ArrayList<>();
        for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressFragments.add(address.getAddressLine(i));
        }
        jobLocation.setGoogleAddress(TextUtils.join(System.getProperty("line.separator"), addressFragments));
        return jobLocation;
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverErrorToReceiver(String error) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARG_RESULT_DATA, error);
        mReceiver.send(Constants.RESULT_FAILURE, bundle);
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(JobLocation location) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARG_RESULT_DATA, location);
        mReceiver.send(Constants.RESULT_SUCCESS, bundle);
    }
}
