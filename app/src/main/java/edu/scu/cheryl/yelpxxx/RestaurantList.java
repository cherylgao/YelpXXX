package edu.scu.cheryl.yelpxxx;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.yelp.clientlib.entities.options.CoordinateOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RestaurantList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<Business> restaurants;
    private static final String CONSUMER_KEY = "rEl2e37-TQueqIpLeFNEGA";
    private static final String CONSUMER_SECRET = "2tfEDEf3aX2b93BJj86IQHPe270";
    private static final String TOKEN = "JrmitrWu9XVqy0namjf6PGLFZ_370LNc";
    private static final String TOKEN_SECRET = "Pse21d6tQkRqinuQUYnADDs-s3c";
    private ListView lv;
    double latitude;
    double longitude;
    Call<SearchResponse> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        String restaurant= getIntent().getStringExtra("restaurant");
        final String city=getIntent().getStringExtra("city");
        latitude=getIntent().getDoubleExtra("latitude", 0.0);
        longitude=getIntent().getDoubleExtra("longitude", 0.0);

            final Map<String, String> para = new HashMap<>();
            para.put("term", restaurant);
            para.put("limit", "4");
            para.put("lang", "fr");
            lv = (ListView) findViewById(R.id.restaurantList);
            lv.setOnItemClickListener(this);
            final RestaurantList cur = this;
        /*try {
            Response<SearchResponse> response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    try {
                        YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
                        YelpAPI yelpAPI = apiFactory.createAPI();
                        if (city!=null) {
                             call = yelpAPI.search(city, para);
                        }else {
                            CoordinateOptions coordinate = CoordinateOptions.builder()
                                    .latitude(latitude)
                                    .longitude(longitude).build();
                            call = yelpAPI.search(coordinate, para);
                        }
                        Response<SearchResponse> response = call.execute();
                        restaurants = response.body().businesses();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "s";
                }

                @Override
                protected void onPostExecute(String result) {
                    //toast("kkkkkkk");
                    lv.setAdapter(new RestaurantArrayAdaptor(cur, R.layout.my_list, restaurants));

                    //mSearchResultsText.setText(result);
                    //setProgressBarIndeterminateVisibility(false);
                }
            }.execute();

        /*Callback<SearchResponse> callback=new Callback<SearchResponse>(){

            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                //Response<SearchResponse> response = call.execute();
                SearchResponse searchResponse=response.body();
                //restaurants=searchResponse.businesses();
                //update the UI text with the searchReponse
            }
            @Override
            public void onFailure(Throwable t) {
                //System.out.println("wrong");
            }
        };*/
            //call.enqueue(callback);
            //restaurants = new ArrayList<>();



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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Business restaurant = restaurants.get(position);
        Intent intent = new Intent(RestaurantList.this, DetailActivity.class);
        intent.putExtra("id", restaurant.id());
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
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
