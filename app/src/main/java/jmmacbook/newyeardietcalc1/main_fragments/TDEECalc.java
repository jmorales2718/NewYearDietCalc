package jmmacbook.newyeardietcalc1.main_fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import jmmacbook.newyeardietcalc1.MainActivity;
import jmmacbook.newyeardietcalc1.calculations.Calculations;

import jmmacbook.newyeardietcalc1.R;

/**
 * Created by jmmacbook on 12/8/15.
 */
public class TDEECalc
        extends Fragment
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Calculations c;
    boolean imperialUnits;
    boolean noActivityLevelSelection;
    String activLvl;
    Spinner spActivityMult;
    Button btnCalcTDEE;
    EditText etWeightForCalc;
    EditText etBfForCalc;
    TextView tvTDEE;
    RadioButton rbImperial;
    RadioButton rbMetric;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tddecalc, container, false);

        noActivityLevelSelection = false;

        c = new Calculations(getContext());
        rbImperial = (RadioButton) view.findViewById(R.id.selectImperial);
        rbMetric = (RadioButton) view.findViewById(R.id.selectMetric);

        rbImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
                rbMetric.setChecked(false);
            }
        });
        rbMetric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
                rbImperial.setChecked(false);
            }
        });

        tvTDEE = (TextView) view.findViewById(R.id.tvTDEE);
        etWeightForCalc = (EditText) view.findViewById(R.id.etWeight);
        etBfForCalc = (EditText) view.findViewById(R.id.etBodyFatPercentage);

        btnCalcTDEE = (Button) view.findViewById(R.id.btnCalculateTDEE);
        btnCalcTDEE.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCalcTDEE.setBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.DarkerOrange));
                    btnCalcTDEE.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.SlightlyDarkerBlue));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCalcTDEE.setBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.DarkOrange));
                    btnCalcTDEE.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.DarkBlue));

                }
                return false;
            }
        });
        btnCalcTDEE.setOnClickListener(this);

        spActivityMult = (Spinner) view.findViewById(R.id.spActivityMultiplier);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.ActivityLevels, R.layout.spinner_settings);
        spActivityMult.setAdapter(adapter);
        spActivityMult.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onResume() {
        if (MainActivity.isImperialSelected()) {
            rbImperial.setChecked(true);
            rbMetric.setChecked(false);
            imperialUnits = true;
        }
        else {
            rbMetric.setChecked(true);
            rbImperial.setChecked(false);
            imperialUnits = false;
        }

        super.onResume();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            activLvl = parent.getItemAtPosition(position).toString();
            noActivityLevelSelection = false;
        }
        else {
            noActivityLevelSelection = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        nothing
    }

    @Override
    public void onClick(View v) {
        String weight;
        String bdyfat;
        Integer intW = 0;
        Integer intbf = 0;

        if (etWeightForCalc.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please input a Weight",
                    Toast.LENGTH_SHORT).show();
        }
        else if (etBfForCalc.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please input a BodyFat Percentage",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            weight = etWeightForCalc.getText().toString();
            intW = Integer.parseInt(weight);
            bdyfat = etBfForCalc.getText().toString();
            intbf = Integer.parseInt(bdyfat);
        }

        if (intW == 0) {
            Toast.makeText(getActivity(), "Please input a Weight",
                    Toast.LENGTH_SHORT).show();
        }
        else if (intbf == 0) {
            Toast.makeText(getActivity(), "Please input a BodyFat Percentage",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            double activityMultiplier = 0.0;
            double leanBodyMass = 0.0;
            double tdee = 0.0;
            if (!noActivityLevelSelection) {
                activityMultiplier = c.calculateActivityLevel(activLvl);
                leanBodyMass = c.calculateLBM(imperialUnits, intW, intbf);
                tdee = c.calculateTDEE(leanBodyMass, activityMultiplier);

                initListener();
                listener.onTDEECalculated(tdee);
                listener.onLBMCalculated(leanBodyMass);

                tvTDEE.setText(String.format("%s Calories per day", String.valueOf(Math.round(tdee))));
                tvTDEE.setTextColor(ContextCompat.getColor(getContext(), R.color.DarkOrange));
                tvTDEE.setTypeface(null, Typeface.BOLD);
                MacrosCalculator.refresh(tdee, Double.parseDouble(etWeightForCalc.getText().toString()));
            }
            else {
                Toast.makeText(getActivity(),
                        "Please select an activity level in hours per week",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private OnTDEECalculatedListener listener;


    public interface OnTDEECalculatedListener {
        public void onTDEECalculated(double tdee);

        public void onLBMCalculated(double lbm);
    }

    private void initListener() {
        try {
            this.listener = (OnTDEECalculatedListener) getActivity();
        } catch (final ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement OnTDEECalculatedListener");
        }

    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.selectImperial:
                if (checked) {
                    imperialUnits = true;
                }
                break;
            case R.id.selectMetric:
                if (checked) {
                    imperialUnits = false;
                }
        }
    }

}
