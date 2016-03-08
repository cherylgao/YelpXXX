package edu.scu.cheryl.yelpxxx;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cz.msebera.android.httpclient.Header;
import retrofit.Call;
import retrofit.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String CONSUMER_KEY = "rEl2e37-TQueqIpLeFNEGA";
    private static final String CONSUMER_SECRET = "2tfEDEf3aX2b93BJj86IQHPe270";
    private static final String TOKEN = "JrmitrWu9XVqy0namjf6PGLFZ_370LNc";
    private static final String TOKEN_SECRET = "Pse21d6tQkRqinuQUYnADDs-s3c";

    Call<SearchResponse> call;
    TextView restName;
    TextView restAddr;
    TextView phoneNum;
    ImageView restImage;
    ListView dishList;
    ListView reviewList;
    List<Business> restaurants;
    Button writeReview;
    int restaurant_id = -1;
    String activity;
    SharedPreferences dataStore;
    SharedPreferences.Editor myEditor;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        restName = (TextView)findViewById(R.id.restName);
        restAddr = (TextView)findViewById(R.id.restAddress);
        phoneNum = (TextView)findViewById(R.id.phoneNum);
        restImage = (ImageView)findViewById(R.id.restImage);
        writeReview = (Button)findViewById(R.id.writeReview);
        dishList = (ListView)findViewById(R.id.dishList);
        reviewList = (ListView)findViewById(R.id.reviewList);
        restaurants = new ArrayList<>();
        dataStore = getPreferences(Activity.MODE_PRIVATE);
        Intent i = getIntent();
        activity = i.getStringExtra("activity");
        writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
                intent.putExtra("id",restaurant_id);
                startActivity(intent);
            }
        });

        //dataStore = context.getSharedPreferences(SP_NAME, 0);

        getLucky(activity, i);

        getList(activity,i);


    }

    @Override
    protected void onResume() {
        super.onResume();
        restName.setText(dataStore.getString("name", ""));
        restAddr.setText(dataStore.getString("address", ""));
        String path = dataStore.getString("img", "");
        phoneNum.setText(dataStore.getString("phone", ""));
        try {
            DownloadImageTask task=new DownloadImageTask(restImage);
            task.execute(path);
        }catch (Exception e){
            e.printStackTrace();
        }

        String baseURL = "http://52.193.219.37:8080/db_rest/rest";
        AsyncHttpClient client = new AsyncHttpClient();
        int candidateId = getIntent().getIntExtra("id",-1);
        restaurant_id = candidateId != -1? candidateId: restaurant_id;
        if(restaurant_id <= 0){
            Intent i = getIntent();
            String name = i.getStringExtra("name");
            String addr = i.getStringExtra("address");
            getDishReview(name, addr);
        }
        else {
            client.get(baseURL + "/review/" + restaurant_id, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        System.out.println("get review response:" + response);
                        String[] review = new String[response.length()];
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            System.out.println("get review by restaurant_id - id: " + jsonObject.getInt("id") +
                                    ", restaurant_id: " + jsonObject.getString("restaurant_id") +
                                    ", review: " + jsonObject.getString("review"));
                            review[i] = jsonObject.getString("review");
                        }
                        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, review);
                        reviewList.setAdapter(arrayAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //AsyncHttpClient.log.w(LOG_TAG, "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", throwable);
                }
            });
        }
    }

    private void showDetail(Business b){
        restName.setText(b.name());
        String address = "";
        String add = "";
        for(String addr: b.location().displayAddress()){
            address += addr+" \n";
            add += add+" ";
        }
        restAddr.setText(address);
        phoneNum.setText(b.displayPhone());
        restImage = (ImageView)findViewById(R.id.restImage);
        String path = b.imageUrl();
        try {

            DownloadImageTask task=new DownloadImageTask(restImage);
            task.execute(path);
        }catch (Exception e){
            e.printStackTrace();
        }
        myEditor = dataStore.edit();
        myEditor.putString("address",address);
        myEditor.putString("phone",b.displayPhone());
        myEditor.putString("name",b.name());
        myEditor.putString("img",path);
        // connect to database to get review and dish rank
        getDishReview(b.name(), add);
    }

    // connect to db and get review dish rank(get)
    private void getDishReview(final String name,final String address){
        // get restaurant id from rest api

        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("address", address);
        String baseURL = "http://52.193.219.37:8080/db_rest/rest";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(baseURL + "/restaurant", params, new JsonHttpResponseHandler() {
            // return restaurant
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("post restaurant successfull with response:" + response);
                try {
                    restaurant_id = response.getInt("id");
                    myEditor = dataStore.edit();
                    myEditor.putInt(address + name, restaurant_id);
                    myEditor.commit();
                    //get review
                    AsyncHttpClient client = new AsyncHttpClient();
                    String baseURL = "http://52.193.219.37:8080/db_rest/rest";
                    client.get(baseURL + "/review/" + restaurant_id, null, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            try {
                                System.out.println("get review response:" + response);
                                String[] review = new String[response.length()];
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    System.out.println("get review by restaurant_id - id: " + jsonObject.getInt("id") +
                                            ", restaurant_id: " + jsonObject.getString("restaurant_id") +
                                            ", review: " + jsonObject.getString("review"));
                                    review[i] = jsonObject.getString("review");
                                }
                                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, review);
                                reviewList.setAdapter(arrayAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                    });

                    // get dishes
                    client.get(baseURL + "/dish/" + restaurant_id, null, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            try {
                                System.out.println("get dish response:" + response);
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
//                        System.out.println("get dish by restaurant_id - id: " + jsonObject.getInt("id") +
//                                ", restaurant_id: " + jsonObject.getString("restaurant_id") +
//                                ", name: " + jsonObject.getString("name") +
//                                ", voting: " + jsonObject.getInt("voting"));
                                }
                            } catch (Exception e) {
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                    });
                } catch (Exception e) {
                }
                super.onSuccess(statusCode, headers, response);
            }

        });
        //restaurant_id = dataStore.getInt(address+name,0);
        System.out.println("++++++++++++" + restaurant_id);

    }

    //from lucky
    //1.get intent
    //2.make yelp search according to intent data
    //3.randomly pick one and show result
    private void getLucky(String activity,Intent i){
        if(activity != null && activity.equals("lucky")){
            GetRestAsync g = new GetRestAsync(i);
            g.execute();
        }
    }

    private void getList(String activity, Intent i){
        if(activity != null && activity.equals("list")){
            String name = i.getStringExtra("name");
            String img = i.getStringExtra("img");
            String addr = i.getStringExtra("address");
            String phone = i.getStringExtra("phone");
            restName.setText(name);
            restAddr.setText(addr);
            phoneNum.setText(phone);
            myEditor = dataStore.edit();
            myEditor.putString("address",addr);
            myEditor.putString("phone",phone);
            myEditor.putString("name",name);
            myEditor.putString("img",img);
            myEditor.commit();
            try {
                DownloadImageTask task=new DownloadImageTask(restImage);
                task.execute(img);
                //getDishReview(name, addr);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public class GetRestAsync extends AsyncTask<Void,Void,String> {
        List<Business> rest;
        Intent i;
        public GetRestAsync(Intent i){
            this.rest = new ArrayList<>();
            this.i = i;
        }

        protected String doInBackground(Void... params) {
            String radius = i.getStringExtra("radius");
            String term = i.getStringExtra("term");
            double latitude=getIntent().getDoubleExtra("latitude", 0.0);
            double longitude=getIntent().getDoubleExtra("longitude", 0.0);
            Scanner readLine = new Scanner(term);
            YelpAPIFactory apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
            YelpAPI yelpAPI = apiFactory.createAPI();
            while(readLine.hasNext()) {
                Map<String, String> para = new HashMap<>();
                para.put("limit", "5");
                para.put("radius_filter", radius);
                para.put("term", readLine.next()+" food");
                para.put("lang", "fr");
                try {
                    CoordinateOptions coordinate = CoordinateOptions.builder()
                            .latitude(latitude).longitude(longitude).build();
                    call = yelpAPI.search("San Jose", para);// hard code for now
                    Response<SearchResponse> response = call.execute();
                    for (Business b : response.body().businesses()) {
                        rest.add(b);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "s";
        }

        @Override
        protected void onPostExecute(String s) {
            Collections.shuffle(rest);
            showDetail(rest.get(0));
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
                Intent intent2 = new Intent(DetailActivity.this, AboutUs.class);
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
