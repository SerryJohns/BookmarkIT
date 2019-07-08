package com.andela.bookmarkit.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.andela.bookmarkit.MainActivity;
import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.local.City;
import com.andela.bookmarkit.ui.base.BaseFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_LOCATION = 100;

    private Place selectedPlace;
    private Place currentPlace;
    private PlacesClient placesClient;
    private GoogleMap googleMap;
    private List<Place.Field> placeFields;

    private ConstraintLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView txtCityName;
    private TextView txtCityDesc;
    private TextView txtBookmark;
    private FloatingActionButton fabFocusMap;
    private FloatingActionButton fabCitiesList;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeFields = Arrays.asList(Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG);

        initMapAndPlaces();
        initBottomSheet();
        initViews();
        setupAutoCompleteFragment();
        initViewListeners();
    }

    private void initMapAndPlaces() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_api_key));
            placesClient = Places.createClient(getContext());
            getCurrentLocation();
        }
    }

    private void initBottomSheet() {
        bottomSheet = (ConstraintLayout) getActivity().findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void initViews() {
        txtCityName = (TextView) getActivity().findViewById(R.id.txt_city_name);
        txtCityDesc = (TextView) getActivity().findViewById(R.id.txt_city_desc);
        fabFocusMap = (FloatingActionButton) getActivity().findViewById(R.id.fab_focus_map);
        fabCitiesList = (FloatingActionButton) getActivity().findViewById(R.id.fab_cities_list);
        txtBookmark = (TextView) getActivity().findViewById(R.id.txt_bookmark_text);
    }

    private void setupAutoCompleteFragment() {
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(placeFields);
        autocompleteSupportFragment.setTypeFilter(TypeFilter.CITIES);

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                selectedPlace = place;
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                updateBottomSheet(place);
                updateCameraPosition(place);
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "An error occurred: " + status);
            }
        });
    }

    private void updateBottomSheet(Place place) {
        txtCityName.setText(place.getName());
        txtCityDesc.setText(place.getAddress());
    }

    private void updateCameraPosition(Place place) {
        if (place != null && place.getLatLng() != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getAddress()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
        } else {
            Toast.makeText(getContext(), getString(R.string.msg_wrong_location), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);

            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()) {
                        FindCurrentPlaceResponse response = task.getResult();
                        if (response.getPlaceLikelihoods().size() > 0) {
                            PlaceLikelihood placeLikelihood = response.getPlaceLikelihoods().get(0);
                            Place place = placeLikelihood.getPlace();
                            currentPlace = place;
                            updateCameraPosition(place);
                            updateBottomSheet(place);
                        }
                    } else {
                        Log.e(TAG, "Place not found");
                    }
                }
            });
        } else {
            getLocationPermission();
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                        }, REQUEST_LOCATION
                );
            }
        }
    }

    private void initViewListeners() {
        fabFocusMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCameraPosition(currentPlace);
            }
        });

        fabCitiesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookmarkedCities("");
            }
        });

        txtBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPlace != null && selectedPlace.getLatLng() != null) {
                    MainActivity activity = (MainActivity) getActivity();

                    City city = new City(
                            new Date(),
                            selectedPlace.getName(),
                            selectedPlace.getAddress(),
                            selectedPlace.getLatLng().latitude,
                            selectedPlace.getLatLng().longitude
                    );

                    activity.fragmentSwitcher.showDetailsFragment(city);
                } else {
                    Toast.makeText(getContext(), getString(R.string.msg_select_city), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showBookmarkedCities(String query) {
        MainActivity activity = (MainActivity) getActivity();
        activity.fragmentSwitcher.showCitiesFragment(query);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(getContext(),
                            getString(R.string.msg_permission_required),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
