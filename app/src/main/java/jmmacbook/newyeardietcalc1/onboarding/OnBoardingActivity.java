package jmmacbook.newyeardietcalc1.onboarding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import jmmacbook.newyeardietcalc1.R;
import jmmacbook.newyeardietcalc1.onboarding.ob_fragments.OnBoardingScreenOne;
import jmmacbook.newyeardietcalc1.onboarding.ob_fragments.OnBoardingScreenTwo;

public class OnBoardingActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vpOnBoarding);
        FragmentManager fragmentManager = getSupportFragmentManager();

        final MyOnBoardingAdapter mAdapter = new MyOnBoardingAdapter(fragmentManager);
        viewPager.setAdapter(mAdapter);

        SmartTabLayout indicator = (SmartTabLayout) findViewById(R.id.stlIndicator);
        indicator.setViewPager(viewPager);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        OnBoardingScreenOne.getAnimation().start();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed()
    {
        //nothing
    }

    class MyOnBoardingAdapter extends FragmentPagerAdapter
    {

        MyOnBoardingAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = null;

            if (position == 0)
            {
                fragment = OnBoardingScreenOne.getInstance();
                return fragment;
            }
            else if (position == 1)
            {
                fragment = OnBoardingScreenTwo.getInstance();
                return fragment;
            }

            return fragment;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

    }

}
