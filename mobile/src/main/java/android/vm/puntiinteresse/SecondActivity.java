package android.vm.puntiinteresse;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.regex.Pattern;

public class SecondActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnPoiClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        OnStreetViewPanoramaReadyCallback{
    TextView nameTv,indirizzoTv,telefonoTv;
    Intent intent;
    private boolean mPermissionDenied = false;
    private static final String TAG = SecondActivity.class.getSimpleName();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Place currentPlace;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        nameTv=(TextView)findViewById(R.id.name_tv);
        indirizzoTv=(TextView)findViewById(R.id.indirizzo_tv);
        telefonoTv=(TextView)findViewById(R.id.telefono_tv);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        MapFragment mMapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        intent = getIntent();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult( builder.build( this ), PLACE_PICKER_REQUEST );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }

        // username=intent.getStringExtra("username");
        //nameTv.setText(username);
    }@Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        mMap.clear();
        super.onStop();
    }


    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if( requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK ) {
            displayPlace( PlacePicker.getPlace( data, this ) );
            currentPlace=PlacePicker.getPlace(data,this);
            mMap.addMarker(new MarkerOptions().position(currentPlace.getLatLng()).title(currentPlace.getName().toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace.getLatLng(),16));
            mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(currentPlace.getLatLng(),currentPlace.getLatLng()));
            placePhotosTask();




        }
    }
    private void displayPlace( Place place ) {
        if( place == null )
            return;

        if( !TextUtils.isEmpty( place.getName() ) ) {
            nameTv.setText("Nome: " + place.getName() + "\n");
        }
        if( !TextUtils.isEmpty( place.getAddress() ) ) {
             indirizzoTv.setText("Indirizzo: " + place.getAddress() + "\n");
        }
        if( !TextUtils.isEmpty( place.getPhoneNumber() ) ) {
            telefonoTv.setText(place.getPhoneNumber());
            //Pattern pattern = Pattern.compile("+[0-9]{2}");
            Linkify.addLinks(telefonoTv, Linkify.PHONE_NUMBERS);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        //mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        mMap.setOnPoiClickListener(this);
        UiSettings u =mMap.getUiSettings();
        u.setMyLocationButtonEnabled(false);
        u.setMapToolbarEnabled(true);
        u.setScrollGesturesEnabled(false);
        u.setZoomGesturesEnabled(true);
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

    }

    private void enableMyLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
                PermissionUtils.requestPermission(this,LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION,true);
        }else if (mMap!=null)mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this,"Pulsante dell myLocation pigiato",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }

    }
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onPoiClick(PointOfInterest poi) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+currentPlace.getAddress()));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
    }
    private void placePhotosTask() {
        final String placeId = currentPlace.getId();

        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask(mRecyclerView.getWidth(),mRecyclerView.getHeight(),mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                //imageSwitcher.setImageResource(R.drawable.empty_photo);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(final AttributedPhoto attributedPhoto) {
                progressBar.setVisibility(View.GONE);
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    adapter = new MyRecyclerViewAdapter(attributedPhoto.bitmap,SecondActivity.this);
                    mRecyclerView.setAdapter(adapter);
                }
            }
        }.execute(placeId);
    }
}
