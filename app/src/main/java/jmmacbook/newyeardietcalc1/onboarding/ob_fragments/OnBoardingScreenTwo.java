package jmmacbook.newyeardietcalc1.onboarding.ob_fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import jmmacbook.newyeardietcalc1.MainActivity;

import jmmacbook.newyeardietcalc1.R;

/**
 * Created by jmmacbook on 8/3/16.
 */
public class OnBoardingScreenTwo extends Fragment {
    private EditText etDefaultSurplus;
    private EditText etDefaultDeficit;
    private RadioButton rbImperial;
    private RadioGroup rgUnits;

    //create and return an instance of OnboardingScreenTwo
    public static OnBoardingScreenTwo getInstance() {
        return new OnBoardingScreenTwo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View screenTwoLayout = inflater.inflate(R.layout.screen_two_onboarding, container, false);

        final Button btnFinish = (Button) screenTwoLayout.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });
        btnFinish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnFinish.setBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.SlightlyDarkerBlue));
                    btnFinish.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.DarkWhite));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnFinish.setBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.DarkBlue));
                    btnFinish.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.White));
                }
                return false;
            }
        });
        etDefaultSurplus = (EditText) screenTwoLayout.findViewById(R.id.metDefaultSurplus);
        etDefaultDeficit = (EditText) screenTwoLayout.findViewById(R.id.metDefaultDeficit);
        rbImperial = (RadioButton) screenTwoLayout.findViewById(R.id.rbImperial);
        rgUnits = (RadioGroup) screenTwoLayout.findViewById(R.id.rgUnits);

        return screenTwoLayout;
    }


    //Saves user settings and navigates from the onboarding screens to main screens
    private void finishOnboarding() {
        SharedPreferences preferences = getContext().getSharedPreferences(MainActivity.SHARED_PREFERENCES_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();

        boolean incorrectSurplusInput = false;
        boolean incorrectDeficitInput = false;
        boolean noUnitsSelection = false;

        if (checkIsEmpty(etDefaultDeficit)) {
            Toast.makeText(getActivity(),
                    "Please input a deficit multiplier", Toast.LENGTH_SHORT).show();
        }
        else if (checkIsEmpty(etDefaultSurplus)) {
            Toast.makeText(getActivity(),
                    "Please input a surplus multiplier", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!isWithinCorrectRange(Float.parseFloat(etDefaultSurplus.getText().toString()))) //float was null
            {
                Toast.makeText(getActivity(),
                        "Please enter a surplus decimal from 0 to 1",
                        Toast.LENGTH_SHORT).show();
                incorrectSurplusInput = true;
            }
            else {
                editor.putFloat(MainActivity.DEFAULT_SURPLUS_ID,
                        Float.parseFloat(etDefaultSurplus.getText().toString()));
                incorrectSurplusInput = false;

            }

            if (!isWithinCorrectRange(Float.parseFloat(etDefaultDeficit.getText().toString()))) {
                Toast.makeText(getActivity(),
                        "Please enter a deficit decimal from 0 to 1",
                        Toast.LENGTH_SHORT).show();
                incorrectDeficitInput = true;
            }
            else {
                editor.putFloat(MainActivity.DEFAULT_DEFICIT_ID,
                        Float.parseFloat(etDefaultDeficit.getText().toString()));
                incorrectDeficitInput = false;

            }
            if (rgUnits.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getActivity(),
                        "Please select either Metric or Imperial Units",
                        Toast.LENGTH_SHORT).show();
                noUnitsSelection = true;
            }
            else {
                editor.putBoolean(MainActivity.UNITS_ID, rbImperial.isChecked());
                noUnitsSelection = false;
            }
            editor.putBoolean(MainActivity.ONBOARDING_COMPLETE_ID, true);

            if (!incorrectDeficitInput && !incorrectSurplusInput && !noUnitsSelection) {
                editor.commit();
                Intent startMain = new Intent(getActivity(), MainActivity.class);
                startActivity(startMain);
            }
        }
    }

    //checks to make sure that inputs are positive decimals
    private boolean isWithinCorrectRange(float inFloat) {
        return !(inFloat >= 1.0 || inFloat < 0);
    }

    private boolean checkIsEmpty(EditText etInput) {
        if (etInput.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }
}
