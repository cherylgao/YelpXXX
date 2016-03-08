package edu.scu.cheryl.yelpxxx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yelp.clientlib.entities.Business;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Cheryl on 16-2-4.
 */
public class RestaurantArrayAdaptor extends ArrayAdapter<Business> {


    private final List<Business> restaurants;

    public RestaurantArrayAdaptor(Context context, int resource, List<Business> restaurants) {

        super(context, resource, restaurants);
        this.restaurants = restaurants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.my_list, null);

        TextView textView = (TextView) row.findViewById(R.id.label);
        textView.setText(restaurants.get(position).name());
//        final ToggleButton favorite = (ToggleButton) row.findViewById(R.id.favoriteButton);
//        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Toast.makeText(getContext(), "set as favorite", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "removed from favorite", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        try {
            ImageView imageView = (ImageView) row.findViewById(R.id.icon);

            String path = restaurants.get(position).imageUrl();
            DownloadImageTask task=new DownloadImageTask(imageView);
            task.execute(path);


//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 8; // Experiment with different sizes
//
//            Bitmap bitmap= BitmapFactory.decodeFile(path, options);
//            imageView.setImageBitmap(bitmap);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } catch (Exception e) {
            Toast.makeText(getContext(), "image view problem", Toast.LENGTH_SHORT).show();
        }
        return row;
    }
//    public static Bitmap loadBitmap(String url) {
//        Bitmap bitmap = null;
//        InputStream in = null;
//        BufferedOutputStream out = null;
//
//        try {
//            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
//
//            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
//            copy(in, out);
//            out.flush();
//
//            final byte[] data = dataStream.toByteArray();
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            //options.inSampleSize = 1;
//
//            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
//        } catch (IOException e) {
//            Log.e(TAG, "Could not load Bitmap from: " + url);
//        } finally {
//            closeStream(in);
//            closeStream(out);
//        }
//
//        return bitmap;
//    }




    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/
}



