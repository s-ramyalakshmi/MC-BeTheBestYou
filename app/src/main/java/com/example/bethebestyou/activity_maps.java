package com.example.bethebestyou;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import android.content.Intent;
public class activity_maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private double latitude=33.436133;
    private double longitude= -111.999783;
    private PlacesClient placesClient;
    private Context context;
    private static final String PHARMACY_TYPE = "pharmacy";
    private FusedLocationProviderClient fusedLocationClient;
    private Button startButton, endButton;
    String receivedMessage= "CVS";
    private String sourceLocation = "33.425026, -111.937437";
    private String destinationLocation = "33.436133, -111.999783";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public interface DistanceMatrixService {

    }

    DistanceMatrixService service = retrofit.create(DistanceMatrixService.class);

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();

        if (intent != null) {
            receivedMessage = intent.getStringExtra("place");
            Log.e("place", receivedMessage );
        }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Places.initialize(getApplicationContext(), "ENTER YOUR API KEY");
        placesClient = Places.createClient(this);
        TextView distanceTextView = findViewById(R.id.textView1);
        TextView durationTextView = findViewById(R.id.textView2);
        TextView durationTrafficTextView = findViewById(R.id.textView3);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            private GoogleMap googleMap;

            @Override
            public void onMapReady(GoogleMap googleMap) {
                this.googleMap = googleMap;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
                findNearbyPharmacies(latitude, longitude, googleMap);
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.e(String.valueOf(latitude), "Latitude ");
                        Log.e(String.valueOf(longitude), "Longitude ");
                    } else {
                        Log.e("Not available", "Latitude ");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Log.e("Denied", "onRequestPermissionsResult: ");
            }
        }
    }

    @SuppressLint("MissingPermission")

    private void findNearbyPharmacies(double lat, double lng, GoogleMap googleMap) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
        String location = lat + "," + lng;
        String radius = "1000"; // Adjust the radius as needed
        String query = receivedMessage;
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(RectangularBounds.newInstance(LatLngBounds.builder().include(new LatLng(lat, lng)).build()))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindAutocompletePredictionsResponse response = task.getResult();
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    String placeId = prediction.getPlaceId();
                    List<Place.Field> placeDetailFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
                    FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeDetailFields).build();
                    placesClient.fetchPlace(fetchPlaceRequest).addOnCompleteListener(fetchTask -> {
                        if (fetchTask.isSuccessful()) {
                            FetchPlaceResponse fetchPlaceResponse = fetchTask.getResult();
                            Place place = fetchPlaceResponse.getPlace();
                            LatLng placeLatLng = place.getLatLng();
                            Log.i("NearbyPharmacies", "Place: " + place.getName() + ", " + place.getLatLng());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
                            if (placeLatLng != null) {
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(placeLatLng)
                                        .title(place.getName());
                                googleMap.addMarker(markerOptions);
                            }
                        } else {
                            Log.e("NearbyPharmacies", "Place not found: " + fetchTask.getException());
                        }
                    });
                }
            } else {
                Log.e("NearbyPharmacies", "Autocomplete prediction fetching failed: " + task.getException());
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}
