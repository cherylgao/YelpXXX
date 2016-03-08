package edu.scu.cheryl.yelpxxx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class LuckyActivity extends AppCompatActivity {

    CheckBox chinese;
    CheckBox thai;
    CheckBox korean;
    CheckBox american;
    CheckBox mexican;
    CheckBox japanese;
    Button submit;
    double longitude;
    double latitude;

    String radius;
    StringBuffer term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky);

        chinese = (CheckBox) findViewById(R.id.chinese);
        thai = (CheckBox) findViewById(R.id.thai);
        korean = (CheckBox) findViewById(R.id.korean);
        american = (CheckBox) findViewById(R.id.american);
        mexican = (CheckBox) findViewById(R.id.mexican);
        japanese = (CheckBox) findViewById(R.id.janpanese);
        submit = (Button) findViewById(R.id.random);
        term = new StringBuffer();

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (radius == null) {
                    Toast.makeText(getApplicationContext(), "Please select a radius.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(LuckyActivity.this, DetailActivity.class);
                if (chinese.isChecked()) {
                    term.append("chinese ");
                }
                if (japanese.isChecked()) {
                    term.append("japanese ");
                }
                if (mexican.isChecked()) {
                    term.append("mexican ");
                }
                if (american.isChecked()) {
                    term.append("american ");
                }
                if (thai.isChecked()) {
                    term.append("thai ");
                }
                if (korean.isChecked()) {
                    term.append("korean ");
                }
                i.putExtra("term", term.toString());

                i.putExtra("radius", radius);
                latitude = getIntent().getDoubleExtra("latitude", 0.0);
                longitude = getIntent().getDoubleExtra("longitude", 0.0);
                i.putExtra("activity", "lucky");
                startActivity(i);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.fiveMile:
                if (checked) {
                    radius = "5";
                }
                break;
            case R.id.fifMile:
                if (checked) {
                    radius = "15";
                }
                break;
            case R.id.twMile:
                if (checked) {
                    radius = "25";
                }
                break;
        }
    }
}

