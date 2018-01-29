package jmmacbook.newyeardietcalc1.onboarding.ob_fragments;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import jmmacbook.newyeardietcalc1.R;

/**
 * Created by jmmacbook on 8/3/16.
 */
public class OnBoardingScreenOne extends Fragment {
    ImageView ivAnimationHolder;
    static AnimationDrawable saladAnimation;

    public static OnBoardingScreenOne getInstance() {
        return new OnBoardingScreenOne();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screenOneLayout = inflater.inflate(R.layout.screen_one_onboarding, container, false);
        ivAnimationHolder = (ImageView) screenOneLayout.findViewById(R.id.ivAnimationHolder);
        ivAnimationHolder.setBackgroundResource(R.drawable.animation_salad_list);
        saladAnimation = (AnimationDrawable) ivAnimationHolder.getBackground();

        return screenOneLayout;
    }

    public static AnimationDrawable getAnimation() {
        return saladAnimation;
    }

}
