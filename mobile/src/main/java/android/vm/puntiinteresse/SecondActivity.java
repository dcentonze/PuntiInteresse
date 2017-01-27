package android.vm.puntiinteresse;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SecondActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    TextView nameTv;
    Intent intent;
    String username;
    private boolean mPermissionDenied = false;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //nameTv=(TextView)findViewById(R.id.name_tv);
        MapFragment mMapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        intent = getIntent();

        // username=intent.getStringExtra("username");
        //nameTv.setText(username);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
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
        Toast.makeText(this,"Pulsante dell myLocation pigiato",Toast.LENGTH_SHORT).show();
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


}
