package com.fixit.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fixit.app.R;
import com.fixit.config.AppConfig;
import com.fixit.controllers.SearchController;
import com.fixit.data.JobLocation;
import com.fixit.data.Profession;
import com.fixit.external.google.GoogleClientManager;
import com.fixit.external.google.GoogleMapWrapper;
import com.fixit.general.PermissionManager;
import com.fixit.geo.FetchAddressIntentService;
import com.fixit.ui.adapters.CommonMarkerInfoWindowAdapter;
import com.fixit.ui.adapters.PlaceAutocompleteAdapter;
import com.fixit.utils.Constants;
import com.fixit.utils.GlobalPreferences;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kostyantin on 10/28/2017.
 */

public class LocationPickerFragment extends BaseFragment<SearchController>
        implements GoogleClientManager.GooglePlacesCallback,
        OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMapWrapper mMap;
    private GoogleApiClient mApiClient;
    private ResultReceiver mResultReceiver;

    private String mProfession;
    private ViewHolder mView;

    private static class ViewHolder {
        final ViewGroup root;
        final AutoCompleteTextView actvAddress;

        Marker locationMarker;

        ViewHolder(View v, AdapterView.OnItemClickListener addressItemClickListener) {
            root = (ViewGroup) v.findViewById(R.id.root);

            actvAddress = (AutoCompleteTextView) v.findViewById(R.id.actv_address);
            actvAddress.setOnItemClickListener(addressItemClickListener);
        }

        public void setAddress(CharSequence address) {
            this.actvAddress.setText(address);
        }
    }

    public static LocationPickerFragment newInstance(Profession profession) {
        LocationPickerFragment fragment = new LocationPickerFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_PROFESSION, profession);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResultReceiver = new AddressResultReceiver(new Handler());
        mProfession = ((Profession) getArguments().getParcelable(Constants.ARG_PROFESSION)).getName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location_picker, container, false);

        mView = new ViewHolder(v, (parent, view, position, id) -> hideKeyboard(mView.root));

        setToolbar((Toolbar) v.findViewById(R.id.toolbar), true);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPlaceChosen(Place place) {
        mView.setAddress(place.getAddress());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = new GoogleMapWrapper(googleMap);

        mMap.getMap().setInfoWindowAdapter(new CommonMarkerInfoWindowAdapter(getContext()));
        mMap.getMap().setOnMapClickListener(this);
        mMap.getMap().setPadding(0, mView.actvAddress.getHeight(), 0, 0);

        showCurrentLocation();
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient != null) {
            mApiClient = googleApiClient;

            if (mView.actvAddress.getAdapter() == null) {
                mView.actvAddress.setAdapter(new PlaceAutocompleteAdapter(getContext(), googleApiClient, null,
                        new AutocompleteFilter
                                .Builder()
                                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                                .setCountry("ZA")
                                .build()
                ));
            }

            showCurrentLocation();
        }
    }

    public void showCurrentLocation() {
        if (mMap != null && mApiClient != null) {
            if(PermissionManager.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                showCurrentLocationWithPermission();
            } else {
                SearchController controller = getController();
                final PermissionManager pm = new PermissionManager(controller != null ? controller.getAnalyticsManager() : null);
                pm.requestPermissions(getActivity(), GlobalPreferences.isLocationPermissionExplained(getContext()), new PermissionManager.PermissionRequest() {
                    @Override
                    public void onPermissionGranted(String[] permissions) {
                        showCurrentLocationWithPermission();
                    }

                    @Override
                    public void onPermissionDenied(String[] permissions) {
                        if(!GlobalPreferences.isLocationPermissionExplained(getContext())) {
                            onPermissionExplanationRequest(permissions);
                        } else {
                            showDefaultLocation();
                        }
                    }

                    @Override
                    public void onPermissionExplanationRequest(String[] permissions) {
                        new MaterialDialog.Builder(getContext())
                                .title(R.string.permission_request)
                                .content(R.string.location_permission_explanation)
                                .positiveText(R.string.grant)
                                .onPositive((dialog, which) -> pm.requestPermissions(getActivity(), true, this))
                                .negativeText(R.string.deny)
                                .onNegative((dialog, which) -> showDefaultLocation())
                                .show();
                        GlobalPreferences.setLocationPermissionExplained(getContext());
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }

    private void showDefaultLocation() {
        String title = null;
        double latitude = 0.0,
               longitude = 0.0;

        Bundle args = getArguments();
        if(args != null) {
            title = args.getString(Constants.ARG_DEFAULT_LOCATION_ADDRESS, null);
            latitude = args.getDouble(Constants.ARG_DEFAULT_LOCATION_LATITUDE, 0.0);
            longitude = args.getDouble(Constants.ARG_DEFAULT_LOCATION_LONGITUDE, 0.0);
        }

        if(title == null || (latitude == 0.0 && longitude == 0.0)) {
            Context context = getContext();
            title = AppConfig.getString(context, Constants.ARG_DEFAULT_LOCATION_ADDRESS, "NA");
            latitude = Double.parseDouble(AppConfig.getString(context, Constants.ARG_DEFAULT_LOCATION_LATITUDE, "0.0"));
            longitude = Double.parseDouble(AppConfig.getString(context, Constants.ARG_DEFAULT_LOCATION_LONGITUDE, "0.0"));
        }

        mView.locationMarker = mMap.showMarker(latitude, longitude, mProfession, title,  true);
    }

    @SuppressWarnings({"MissingPermission"})
    private void showCurrentLocationWithPermission() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);

        if(location != null) {
            fetchAddress(new LatLng(location.getLatitude(), location.getLongitude()));
        } else {
            showDefaultLocation();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.getMap().setOnMapClickListener(null);
        hideKeyboard(mView.root);
        mView.locationMarker.remove();
        fetchAddress(latLng);
    }

    private void fetchAddress(LatLng latLng) {
        Context ctx = getContext();
        Intent intent = new Intent(ctx, FetchAddressIntentService.class);
        intent.putExtra(Constants.ARG_RESULT_RECEIVER, mResultReceiver);
        intent.putExtra(Constants.ARG_LAT_LNG, latLng);

        ctx.startService(intent);
    }

    private boolean isValidSelection() {
        if(mView.locationMarker != null) {
            JobLocation jobLocation = (JobLocation) mView.locationMarker.getTag();
            if(jobLocation != null) {
                return jobLocation.getLat() != 0 && jobLocation.getLng() != 0 && !TextUtils.isEmpty(jobLocation.getStreet());
            }
        }
        return false;
    }

    private class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == Constants.RESULT_SUCCESS) {
                JobLocation jobLocation = resultData.getParcelable(Constants.ARG_RESULT_DATA);

                mView.locationMarker = mMap.showMarker(jobLocation.getLat(), jobLocation.getLng(),
                        mProfession, jobLocation.getGoogleAddress(), true);
                mView.locationMarker.setTag(jobLocation);
                mView.setAddress(jobLocation.getGoogleAddress());
            } else {
                notifyUser("Error: " + resultData.getString(Constants.ARG_RESULT_DATA));
            }

            mMap.getMap().setOnMapClickListener(LocationPickerFragment.this);
        }
    }
}
