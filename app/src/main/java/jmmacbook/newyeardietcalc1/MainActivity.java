package jmmacbook.newyeardietcalc1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import jmmacbook.newyeardietcalc1.main_fragments.MacrosCalculator;
import jmmacbook.newyeardietcalc1.onboarding.OnBoardingActivity;

import jmmacbook.newyeardietcalc1.main_fragments.TDEECalc;

public class MainActivity
        extends AppCompatActivity
        implements TDEECalc.OnTDEECalculatedListener {
    public static final int REQUEST_CODE = 0;
    public static final double DEFAULT_DEFICIT = .2;
    public static final double DEFAULT_SURPLUS = .05;
    private static double tdee;
    private double lbm;

    public static boolean imperialSelected;
    public static double defaultDeficitMultiplier;
    public static double defaultSurplusMultiplier;

    public static final String DEFAULT_DEFICIT_ID = "DEFAULT_DEFICIT_ID";
    public static final String DEFAULT_SURPLUS_ID = "DEFAULT_SURPLUS_ID";
    public static final String UNITS_ID = "UNITS_ID";
    public static final String ONBOARDING_COMPLETE_ID = "ONBOARDING_COMPLETE_ID";
    public static final String SHARED_PREFERENCES_ID = "SHARED_PREFERENCES_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acivity);

        //check if onboarding has been done
        if (!isOnboardingComplete()) {
            Intent startOnboarding = new Intent(this, OnBoardingActivity.class);
            startActivity(startOnboarding);
        }
        else {

            retrieveSettingsSharedPreferences();

            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            FragmentManager fragmentManager = getSupportFragmentManager();
            MyAdapter mAdapter = new MyAdapter(fragmentManager);
            viewPager.setAdapter(mAdapter);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            tdee = 0.0;
            lbm = 0.0;
        }
    }

    @Override
    protected void onRestart() {
        if (isOnboardingComplete()) retrieveSettingsSharedPreferences();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        if (isOnboardingComplete()) retrieveSettingsSharedPreferences();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_acivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, REQUEST_CODE);
                break;
        }
        return true;
    }

    //retrieve settings from sharedpreferences
    private void retrieveSettingsSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_ID, 0);
        imperialSelected = preferences.getBoolean(MainActivity.UNITS_ID, true);
        defaultSurplusMultiplier = preferences.getFloat(MainActivity.DEFAULT_SURPLUS_ID, 0.0f);
        defaultDeficitMultiplier = preferences.getFloat(MainActivity.DEFAULT_DEFICIT_ID, 0.0f);
    }

    private boolean isOnboardingComplete() {
        SharedPreferences preferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_ID, 0);
        return preferences.getBoolean(MainActivity.ONBOARDING_COMPLETE_ID, false);
    }

    public static boolean isImperialSelected() {
        return imperialSelected;
    }

    public static double getDefaultDeficitMultiplier() {
        if (defaultDeficitMultiplier == 0.0) {
            return DEFAULT_DEFICIT;
        }

        return defaultDeficitMultiplier;
    }

    public static double getDefaultSurplusMultiplier() {
        if (defaultSurplusMultiplier == 0.0) {
            return DEFAULT_SURPLUS;
        }

        return defaultSurplusMultiplier;
    }

    @Override
    public void onTDEECalculated(double tdee) {
        this.tdee = tdee;
    }

    @Override
    public void onLBMCalculated(double lbm) {
        this.lbm = lbm;
    }

    @Override
    public void onBackPressed() {
        //prevent navigation back to onboarding
    }
}

class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0) {
            fragment = new TDEECalc();
            return fragment;
        }
        else if (position == 1) {
            fragment = MacrosCalculator.getInstance();
            return fragment;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "TDEE Calculator";
        }
        else if (position == 1) {
            return "Macros Calculator";
        }
        return null;
    }
}