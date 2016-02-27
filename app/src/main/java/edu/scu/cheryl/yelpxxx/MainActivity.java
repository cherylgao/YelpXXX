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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    String restaurant;
    String city;
    Button nearby;
    Button lucky;
    EditText restaurantName;
    EditText cityName;
    TextView location;
    Button search;
    Button show;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    double mLatitude;
    double mLongitude;
    final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of GoogleAPIClient.
        if (checkPlayServices()) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        }
        location = (TextView) findViewById(R.id.lblLocation);

        nearby = (Button) findViewById(R.id.nearbyButton);
        nearby.setOnClickListener(this);
        lucky = (Button) findViewById(R.id.luckyButton);
        lucky.setOnClickListener(this);
        restaurantName = (EditText) findViewById(R.id.restaurantName);
        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(this);

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
        System.out.println("end of onCreate");
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        getLocation();

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1000).show();
            } else {
                toast("this device is not supported");
                finish();
            }
            return false;
        }
        return true;
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
            location.setText(mLatitude + ", " + mLongitude);
        } else {
            location.setText("cannot get location");
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
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
                Location myLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        System.out.println("start of onStart");
        super.onStart();
        mGoogleApiClient.connect();

    }
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }
    @Override
    protected void onResume() {
        System.out.println("start of onResume");
        super.onResume();

        checkPlayServices();
        //mGoogleApiClient.connect();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.nearbyButton:
                mGoogleApiClient.connect();
                restaurant=restaurantName.getText().toString();
                Intent intent= new Intent(MainActivity.this, RestaurantList.class);
                intent.putExtra("restaurant", restaurant);
                intent.putExtra("latitude", mLatitude);
                intent.putExtra("longitude", mLongitude);
                startActivity(intent);
                break;
            case R.id.show:
                //getLocation();
                mGoogleApiClient.connect();
                break;
            case R.id.luckyButton:
                Intent intent1= new Intent(MainActivity.this, LuckyActivity.class);
                startActivity(intent1);
                break;
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
        Log.i(TAG, "connection failed:connection result.getErrorCode()= " + connectionResult.getErrorCode());



    }
}
