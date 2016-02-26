package edu.scu.cheryl.yelpxxx;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    String restaurant;
    String city;
    Button nearby;
    Button lucky;
    EditText restaurantName;
    EditText cityName;
    Button search;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String mLatitudeText;
    String mLongitudeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of GoogleAPIClient.
        //GoogleApiClient mGoogleApiClient= new GoogleApiClient.Builder();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        nearby = (Button) findViewById(R.id.nearbyButton);
        nearby.setOnClickListener(this);
        lucky = (Button) findViewById(R.id.luckyButton);
        lucky.setOnClickListener(this);
        restaurantName = (EditText) findViewById(R.id.restaurantName);

        cityName = (EditText) findViewById(R.id.cityName);

        search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(this);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Pley");
        actionBar.setSubtitle("Beat Yelp!");
        //actionBar.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.action_bar_background));
        //actionBar.setIcon(R.mipmap.ic_fish);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
             mLatitudeText= String.valueOf(mLastLocation.getLatitude());
             mLongitudeText=String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.nearbyButton:
                restaurant=restaurantName.getText().toString();
                Intent intent= new Intent(MainActivity.this, RestaurantList.class);
                intent.putExtra("restaurant", restaurant);
                intent.putExtra("latitude", mLatitudeText);
                intent.putExtra("longitude", mLongitudeText);
                startActivity(intent);
                break;
            case R.id.luckyButton:
                Intent intent1= new Intent(MainActivity.this, LuckyActivity.class);
                startActivity(intent1);
            case R.id.searchButton:
                restaurant=restaurantName.getText().toString();
                city=cityName.getText().toString();
                Intent intent2= new Intent(MainActivity.this, RestaurantList.class);
                intent2.putExtra("restaurant", restaurant);
                intent2.putExtra("city", city);
                startActivity(intent2);
                break;
            default:
                toast("unknown action");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_information:
                Intent intent2 = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent2);
                break;
            case R.id.action_delete:
                Intent intent3= new Intent(Intent.ACTION_DELETE);
                intent3.setData(Uri.parse("package:edu.scu.cheryl.yelpxxx"));
                startActivity(intent3);
                break;
            default:
                toast("unknown action ...");
        }

        return true;
    }
    private void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        if (!mIntentInProgress && result.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                result.startResolutionForResult(this, // your activity
//                        RC_SIGN_IN);
//            } catch (IntentSender.SendIntentException e) {
//                // The intent was canceled before it was sent. Return to
//                // default
//                // state and attempt to connect to get an updated
//                // ConnectionResult.
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }

    }
}
