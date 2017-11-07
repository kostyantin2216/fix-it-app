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
import com.fixit.general.DelayedAction;
import com.fixit.general.PermissionManager;
import com.fixit.geo.FetchAddressIntentService;
import com.fixit.ui.adapters.CommonMarkerInfoWindowAdapter;
import com.fixit.ui.adapters.PlaceAutocompleteAdapter;
import com.fixit.ui.components.CancelableAutoCompleteTextView;
import com.fixit.ui.components.FloatingTextButton;
import com.fixit.ui.helpers.UITutorials;
import com.fixit.utils.CommonUtils;
import com.fixit.utils.Constants;
import com.fixit.utils.DataUtils;
import com.fixit.utils.FILog;
import com.fixit.utils.GlobalPreferences;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Kostyantin on 10/28/2017.
 */

public class LocationPickerFragment extends BaseFragment<SearchController>
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private LocationPickerFragmentInteractionListener mListener;

    private GoogleMapWrapper mMap;
    private GoogleApiClient mApiClient;
    private ResultReceiver mResultReceiver;

    private String mProfession;
    private ViewManager mView;

    private class ViewManager implements AdapterView.OnItemClickListener, View.OnClickListener, CancelableAutoCompleteTextView.OnClearListener {
        final ViewGroup root;
        final CancelableAutoCompleteTextView actvAddress;
        final FloatingTextButton btnDone;

        Marker locationMarker;

        final DelayedAction loaderAction;
        final long delayMillis;

        ViewManager(View v) {
            root = (ViewGroup) v.findViewById(R.id.root);

            setToolbar((Toolbar) v.findViewById(R.id.toolbar), true);

            actvAddress = (CancelableAutoCompleteTextView) v.findViewById(R.id.actv_address);
            actvAddress.setOnItemClickListener(this);
            actvAddress.setOnClearListener(this);

            btnDone = (FloatingTextButton) v.findViewById(R.id.fab_done);
            btnDone.setTitle(getString(R.string.find_profession_format, mProfession));
            btnDone.setOnClickListener(this);

            loaderAction = new DelayedAction(() -> LocationPickerFragment.this.showLoader(getString(R.string.loading_location), false));
            delayMillis = AppConfig.getInteger(root.getContext(), AppConfig.KEY_MAP_LOADER_DELAY_MILLIS, 700);
        }

        public void setAddress(CharSequence address) {
            this.actvAddress.setText(address);
            this.actvAddress.clearFocus();
        }

        public void showLoader() {
            loaderAction.perform(delayMillis);
        }

        public void hideLoader() {
            loaderAction.cancel();
            LocationPickerFragment.this.hideLoader();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LocationPickerFragment.this.hideKeyboard(root);

            fetchAddress(actvAddress.getText().toString());
        }

        public void updateDoneButton() {
            if(isValidSelection()) {
                btnDone.setVisibility(View.VISIBLE);
                Address address = (Address) mView.locationMarker.getTag();
                btnDone.setTitle(getString(R.string.i_need_profession_at_location, mProfession, DataUtils.combineAddressLines(address)));
                btnDone.post(() -> {
                    mMap.setBottomPadding(btnDone.getHeight() + 40);
                    if (!UITutorials.isTutorialComplete(UITutorials.TUTORIAL_BEGIN_SEARCH, getContext())) {
                        UITutorials.create(UITutorials.TUTORIAL_SEARCH_SCREEN, btnDone, getString(R.string.click_to_search));
                    }
                });
            } else {
                if(mMap != null) {
                    mMap.setBottomPadding(0);
                }
                btnDone.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            beginSearch();
        }

        public void clearMarker() {
            if(locationMarker != null) {
                locationMarker.remove();
                locationMarker.setTag(null);
                updateDoneButton();
            }
        }

        @Override
        public void onAutoCompleteCleared() {
            clearMarker();
        }
    }

    private boolean beginSearch() {
        if(isValidSelection()) {
            mListener.performSearch(mProfession, (Address) mView.locationMarker.getTag());
            mView.clearMarker();
            return true;
        } else {
            notifyUser(getString(R.string.cant_search_with_address));
            return false;
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
        Profession profession = getArguments().getParcelable(Constants.ARG_PROFESSION);

        assert profession != null;

        mProfession = profession.getNamePlural();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location_picker, container, false);

        mView = new ViewManager(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView.updateDoneButton();

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map, mapFragment)
                .commit();

        mapFragment.getMapAsync(this);

        /*UITutorials.create(UITutorials.TUTORIAL_SEARCH_LOCATION, mView.actvAddress, getString(R.string.tutorial_search_location))
                .show(getFragmentManager());*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof LocationPickerFragmentInteractionListener) {
            mListener = (LocationPickerFragmentInteractionListener) context;
        } else {
            throw new IllegalArgumentException("context does not implement "
                    + LocationPickerFragmentInteractionListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = new GoogleMapWrapper(googleMap);

        mMap.setTopPadding(mView.actvAddress.getHeight() + 15);

        mMap.getMap().setInfoWindowAdapter(new CommonMarkerInfoWindowAdapter(getContext()));
        mMap.getMap().setOnMapClickListener(this);
        mMap.getMap().setOnInfoWindowClickListener(this);
        mMap.getMap().getUiSettings().setZoomControlsEnabled(true);
        mMap.getMap().getUiSettings().setMapToolbarEnabled(false);

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
        boolean markLocation = false;
        double latitude = 0.0,
               longitude = 0.0;

        Bundle args = getArguments();
        if(args != null) {
            markLocation = args.getBoolean(Constants.ARG_MARK_LOCATION, markLocation);
            latitude = args.getDouble(Constants.ARG_DEFAULT_LOCATION_LATITUDE, 0.0);
            longitude = args.getDouble(Constants.ARG_DEFAULT_LOCATION_LONGITUDE, 0.0);
        }

        if(latitude == 0.0 && longitude == 0.0) {
            Context context = getContext();
            latitude = Double.parseDouble(AppConfig.getString(context, Constants.ARG_DEFAULT_LOCATION_LATITUDE, "0.0"));
            longitude = Double.parseDouble(AppConfig.getString(context, Constants.ARG_DEFAULT_LOCATION_LONGITUDE, "0.0"));
        }

        LatLng location = new LatLng(latitude, longitude);
        if(markLocation) {
            fetchAddress(location);
        } else {
            mView.clearMarker();
            mMap.moveTo(location);
        }
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
        fetchAddress(latLng);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        beginSearch();
    }

    private void fetchAddress(LatLng latLng) {
        fetchAddress(new FetchAddressIntentService.Params(latLng));
    }

    private void fetchAddress(String addressText) {
        fetchAddress(new FetchAddressIntentService.Params(addressText));
    }

    private void fetchAddress(FetchAddressIntentService.Params params) {
        mView.clearMarker();

        Context ctx = getContext();
        Intent intent = new Intent(ctx, FetchAddressIntentService.class);
        intent.putExtra(Constants.ARG_RESULT_RECEIVER, mResultReceiver);
        intent.putExtra(Constants.ARG_PARAMS, params);

        mView.showLoader();

        ctx.startService(intent);
    }

    private boolean isValidSelection() {
        if(mView.locationMarker != null) {
            Address address = (Address) mView.locationMarker.getTag();
            if(address != null) {
                return address.hasLatitude() && address.hasLongitude() && !TextUtils.isEmpty(address.getThoroughfare());
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
            mView.hideLoader();
            if(resultCode == Constants.RESULT_SUCCESS) {
                Address address = resultData.getParcelable(Constants.ARG_RESULT_DATA);

                assert address != null;

                String formattedAddress = DataUtils.combineAddressLines(address);
                mView.locationMarker = mMap.showMarker(address.getLatitude(), address.getLongitude(),
                        mProfession, formattedAddress, true);
                mView.locationMarker.setTag(address);
                mView.setAddress(formattedAddress);
                mView.updateDoneButton();
            } else {
                notifyUser("Error: " + resultData.getString(Constants.ARG_RESULT_DATA));
            }

            mMap.getMap().setOnMapClickListener(LocationPickerFragment.this);
        }
    }

    public interface LocationPickerFragmentInteractionListener {
        void performSearch(String profession, Address address);
    }
}
