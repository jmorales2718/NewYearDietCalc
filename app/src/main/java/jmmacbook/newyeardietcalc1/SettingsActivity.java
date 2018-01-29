package jmmacbook.newyeardietcalc1;

import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {
    public static final String DEFAULT_DEFICIT = "DEFAULT_DEFICIT";
    public static final String DEFAULT_SURPLUS = "DEFAULT_SURPLUS";
    public static final String IMPERIAL_SELECTED = "IMPERIAL_SELECTED";

    EditText metDefaultDeficit;
    EditText metDefaultSurplus;
    RadioButton rbSelectImperial;
    RadioButton rbSelectMetric;

    Button btnSaveSettings;
    Button btnCancelSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar toolbarSettings = (Toolbar) findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbarSettings);

        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_ID, 0);

        metDefaultDeficit = (EditText) findViewById(R.id.metDefaultDeficit);
        metDefaultDeficit.setText(String.valueOf(
                preferences.getFloat(MainActivity.DEFAULT_DEFICIT_ID, .2f)));
        metDefaultDeficit.setSelectAllOnFocus(true);
        metDefaultSurplus = (EditText) findViewById(R.id.metDefaultSurplus);
        metDefaultSurplus.setText(String.valueOf(
                preferences.getFloat(MainActivity.DEFAULT_SURPLUS_ID, .05f)));
        metDefaultSurplus.setSelectAllOnFocus(true);

        rbSelectImperial = (RadioButton) findViewById(R.id.rbSelectImperial);
        rbSelectMetric = (RadioButton) findViewById(R.id.rbSelectMetric);

        if (preferences.getBoolean(MainActivity.UNITS_ID, false)) {
            rbSelectImperial.setChecked(true);
            rbSelectMetric.setChecked(false);
        }
        else {
            rbSelectMetric.setChecked(true);
            rbSelectImperial.setChecked(false);
        }


        final SharedPreferences.Editor editor = preferences.edit();

        btnSaveSettings = (Button) findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if a positive decimal value was entered in the Default Deficit field
                if (!metDefaultDeficit.getText().toString().isEmpty()) {
                    if (Double.parseDouble(metDefaultDeficit.getText().toString()) >= 1.0 ||
                            Double.parseDouble(metDefaultDeficit.getText().toString()) < 0) {
                        Toast.makeText(SettingsActivity.this,
                                "Please input a decimal value between 0 and 1",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        editor.putFloat(MainActivity.DEFAULT_DEFICIT_ID,
                                Float.parseFloat(metDefaultDeficit.getText().toString()));

                    }
                }
                //check if a positive decimal value was entered in the Default Surplus field
                if (!metDefaultSurplus.getText().toString().isEmpty()) {
                    if (Double.parseDouble(metDefaultSurplus.getText().toString()) >= 1.0 ||
                            Double.parseDouble(metDefaultSurplus.getText().toString()) < 0) {
                        Toast.makeText(SettingsActivity.this,
                                "Please input a decimal value between 0 and 1",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        editor.putFloat(MainActivity.DEFAULT_SURPLUS_ID,
                                Float.parseFloat(metDefaultSurplus.getText().toString()));
                    }

                }
                editor.putBoolean(MainActivity.UNITS_ID, rbSelectImperial.isChecked());
                editor.commit();
                Toast.makeText(SettingsActivity.this,
                        "Settings Saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btnSaveSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSaveSettings.setBackgroundColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.SlightlyDarkerBlue));
                    btnSaveSettings.setTextColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.DarkWhite));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnSaveSettings.setBackgroundColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.DarkBlue));
                    btnSaveSettings.setTextColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.White));
                }
                return false;
            }
        });


        btnCancelSettings = (Button) findViewById(R.id.btnCancelSettings);
        btnCancelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancelSettings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCancelSettings.setBackgroundColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.SlightlyDarkerBlue));
                    btnCancelSettings.setTextColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.DarkWhite));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCancelSettings.setBackgroundColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.DarkBlue));
                    btnCancelSettings.setTextColor(
                            ContextCompat.getColor(SettingsActivity.this, R.color.White));
                }
                return false;
            }
        });

    }

}
