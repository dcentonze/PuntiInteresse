package android.vm.puntiinteresse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.PointOfInterest;

/**
 * Created by acbes on 25/01/2017.
 */

public class MainActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnPoiClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    TextView welcomeTv;
    Button changeBt;

    MainActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeBt=(Button)findViewById(R.id.change_bt);


        //changeTextEdit=(EditText)findViewById(R.id.edit_txt);
        changeBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(activity,SecondActivity.class);
                startActivity(intent);




            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
