package edu.scu.cheryl.yelpxxx;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Response;

public class RestaurantList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<Business> restaurants;
    private static final String CONSUMER_KEY = "rEl2e37-TQueqIpLeFNEGA";
    private static final String CONSUMER_SECRET = "2tfEDEf3aX2b93BJj86IQHPe270";
    private static final String TOKEN = "JrmitrWu9XVqy0namjf6PGLFZ_370LNc";
    private static final String TOKEN_SECRET = "Pse21d6tQkRqinuQUYnADDs-s3c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        String restaurant= getIntent().getStringExtra("restaurant");
        String city=getIntent().getStringExtra("city");
        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET,TOKEN,TOKEN_SECRET);
        YelpAPI yelpAPI= apiFactory.createAPI();
        Map<String, String> params= new HashMap<>();
        params.put("term", restaurant);
        params.put("limit", "4");
        Call<SearchResponse> call= yelpAPI.search(city, params);
        try {
            Response<SearchResponse> response = call.execute();
            SearchResponse searchResponse=response.body();
            restaurants=searchResponse.businesses();


        }catch(IOException e){
            toast("IOException");
        }



        ListView lv = (ListView) findViewById(R.id.restaurantList);
        lv.setAdapter(new RestaurantArrayAdaptor(this, R.layout.my_list, restaurants));
        lv.setOnItemClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("YelpXXX");
        actionBar.setSubtitle("Beat Yelp!");
        //actionBar.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.action_bar_background));
        //actionBar.setIcon(R.mipmap.ic_fish);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Business restaurant = restaurants.get(position);
        Intent intent = new Intent(RestaurantList.this, DetailActivity.class);
        intent.putExtra("restaurantName", restaurant.id());
        startActivity(intent);

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
                Intent intent2 = new Intent(RestaurantList.this, AboutUs.class);
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
