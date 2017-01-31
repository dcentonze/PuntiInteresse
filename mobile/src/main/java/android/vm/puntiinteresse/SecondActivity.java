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
    int currentIndex = 0;
    String username;
    StreetViewPanorama mStreetView;
    private boolean mPermissionDenied = false;
    private static final String TAG = SecondActivity.class.getSimpleName();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Place currentPlace;
    ImageSwitcher imageSwitcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        nameTv=(TextView)findViewById(R.id.name_tv);
        indirizzoTv=(TextView)findViewById(R.id.indirizzo_tv);
        telefonoTv=(TextView)findViewById(R.id.telefono_tv);
        imageSwitcher=(ImageSwitcher) findViewById(R.id.image_switch);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        //imageSwitcher.setImageResource(R.drawable.ic_launcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                                     public View makeView() {
                                         ImageView myView = new ImageView(getApplicationContext());
                                         return myView;
                                     }
                                 });
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        MapFragment mMapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        intent = getIntent();
       // if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
         //   return;
        //StreetViewPanoramaFragment streetViewPanoramaFragment =
          //      (StreetViewPanoramaFragment) getFragmentManager()
            //            .findFragmentById(R.id.streetviewpanorama);
        //streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);


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
    /*private void displayPlacePicker() {
        if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
            return;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult( builder.build( this ), PLACE_PICKER_REQUEST );
        } catch ( GooglePlayServicesRepairableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );
        } catch ( GooglePlayServicesNotAvailableException e ) {
            Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
        }
    }*/

    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if( requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK ) {
            displayPlace( PlacePicker.getPlace( data, this ) );
            currentPlace=PlacePicker.getPlace(data,this);
            mMap.addMarker(new MarkerOptions().position(currentPlace.getLatLng()).title(currentPlace.getName().toString()));
            //mMap.moveCamera(CameraUpdateFactory.zoomBy(15f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace.getLatLng(),16));

            //mMap.moveCamera(CameraUpdateFactory.zoomBy(15f));
            mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(currentPlace.getLatLng(),currentPlace.getLatLng()));
            placePhotosTask();
            //mStreetView.setPosition(new LatLng(currentPlace.getLatLng().latitude,currentPlace.getLatLng().longitude));
            // Get a PlacePhotoMetadataResult containing metadata for the first 10 photos.
           /* PlacePhotoMetadataResult result = Places.GeoDataApi
                    .getPlacePhotos(mGoogleApiClient, currentPlace.getId()).await();
            // Get a PhotoMetadataBuffer instance containing a list of photos (PhotoMetadata).
            if (result != null && result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                // Get the first photo in the list.
                PlacePhotoMetadata photo = photoMetadataBuffer.get(0);
                // Get a full-size bitmap for the photo.
                Bitmap image = photo.getPhoto(mGoogleApiClient).await().getBitmap();
                // Get the attribution text.
                CharSequence attribution = photo.getAttributions();
                BitmapDrawable imageBit = new BitmapDrawable(Resources.getSystem(),image);
                imageSwitcher.setImageDrawable(imageBit);
            }*/


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
        mStreetView = streetViewPanorama;
    }
    private void placePhotosTask() {
        final String placeId = currentPlace.getId();

        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask(imageSwitcher.getWidth(),imageSwitcher.getHeight(),mGoogleApiClient) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                imageSwitcher.setImageResource(R.drawable.empty_photo);
            }

            @Override
            protected void onPostExecute(final AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    imageSwitcher.setImageDrawable(attributedPhoto.bitmap.get(currentIndex));
                    imageSwitcher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  Check If index reaches maximum then reset it
                            currentIndex++;
                            if (currentIndex == attributedPhoto.bitmap.size())
                                currentIndex = 0;
                            imageSwitcher.setImageDrawable(attributedPhoto.bitmap.get(currentIndex)); // set the image in ImageSwitcher
                        }
                    });

                   /* // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                        mText.setVisibility(View.GONE);
                    } else {
                        mText.setVisibility(View.VISIBLE);
                        mText.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
                    }*/

                }
            }
        }.execute(placeId);
    }
}
