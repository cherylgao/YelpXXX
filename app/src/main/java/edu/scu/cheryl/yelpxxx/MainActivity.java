package edu.scu.cheryl.yelpxxx;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    String restaurant;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button nearby= (Button) findViewById(R.id.nearbyButton);
        nearby.setOnClickListener(this);
        Button lucky= (Button) findViewById(R.id.luckyButton);
        lucky.setOnClickListener(this);
        EditText restaurantName =(EditText)findViewById(R.id.restaurantName);
        restaurant=restaurantName.getText().toString();
        EditText cityName =(EditText) findViewById(R.id.cityName);
        city=cityName.getText().toString();
        Button submit = (Button) findViewById(R.id.searchButton);
        submit.setOnClickListener(this);
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.nearbyButton:
                Intent intent= new Intent(MainActivity.this, RestaurantList.class);
                startActivity(intent);
                break;
            case R.id.luckyButton:
                Intent intent1= new Intent(MainActivity.this, LuckyActivity.class);
                startActivity(intent1);
            case R.id.searchButton:
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
}
