package jmmacbook.newyeardietcalc1.main_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import jmmacbook.newyeardietcalc1.calculations.Calculations;
import jmmacbook.newyeardietcalc1.R;

import static android.graphics.Typeface.BOLD;

/**
 * Created by jmmacbook on 12/8/15.
 */
public class MacrosCalculator
        extends Fragment
        implements OnItemSelectedListener, View.OnClickListener {

    Spinner dietMacrosOptions;
    String selectedRatios;
    Button btnCalculateMacros;
    TextView tvFat;
    TextView tvCarbs;
    TextView tvProtien;
    Calculations calc;

    static double bodyweight;
    static EditText etTDEE;

    static boolean wasRefreshed;


    public static MacrosCalculator getInstance() {
        return new MacrosCalculator();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.macros_calculator, container, false);

        etTDEE = (EditText) view.findViewById(R.id.etTDEE);
        etTDEE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTDEE.setText(trimCaloriesLabel(etTDEE.getText().toString()));
                etTDEE.setSelection(etTDEE.getText().length());
                etTDEE.setImeOptions(EditorInfo.IME_ACTION_DONE);
                wasRefreshed = false;
            }
        });

        calc = new Calculations(getContext());

        dietMacrosOptions = (Spinner) view.findViewById(R.id.spPercentageMacrosSelect);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.MacroRatios, R.layout.spinner_settings);
        dietMacrosOptions.setAdapter(adapter);
        dietMacrosOptions.setOnItemSelectedListener(this);

        btnCalculateMacros = (Button) view.findViewById(R.id.btnCalculateMacros);
        btnCalculateMacros.setOnClickListener(this);
        btnCalculateMacros.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCalculateMacros.setBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.DarkYellowOrange));
                    btnCalculateMacros.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.SlightlyDarkerBlue));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCalculateMacros.setBackgroundColor(
                            ContextCompat.getColor(getContext(), R.color.YellowOrange));
                    btnCalculateMacros.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.DarkBlue));
                }

                return false;
            }
        });

        tvFat = (TextView) view.findViewById(R.id.tvFatGrams);
        tvCarbs = (TextView) view.findViewById(R.id.tvCarbGrams);
        tvProtien = (TextView) view.findViewById(R.id.tvProteinGrams);

        wasRefreshed = false;

        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedRatios = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    @Override
    public void onClick(View v) {
        if (trimCaloriesLabel(etTDEE.getText().toString()).isEmpty() || etTDEE.getText().toString().isEmpty()) {
            Toast.makeText(getContext(),
                    "Please use TDEE calculator or enter a number of calories per day manually",
                    Toast.LENGTH_LONG).show();
        }
        else {
            double[] macros =
                    calc.calculateMacros(calc.calculateMacroSpinnerSelection(selectedRatios),
                            Double.parseDouble(trimCaloriesLabel(etTDEE.getText().toString())), bodyweight);

            int[] intMacros = calc.roundMacros(macros);

            tvFat.setText(String.format("%sg fat per day", String.valueOf(intMacros[0])));
            makeTextMoreProminent(tvFat);
            tvCarbs.setText(String.format("%sg carbs per day", String.valueOf(intMacros[1])));
            makeTextMoreProminent(tvCarbs);
            tvProtien.setText(String.format("%sg protein per day", String.valueOf(intMacros[2])));
            makeTextMoreProminent(tvProtien);
        }

    }

    private void makeTextMoreProminent(TextView tvInput) {
        tvInput.setTypeface(null, BOLD);
        tvInput.setTextColor(ContextCompat.getColor(getContext(), R.color.YellowOrange));
    }

    public static void refresh(double tdee, double myBodyweight) {
        bodyweight = myBodyweight;
        etTDEE.setText(addCaloriesLabel(tdee));
        wasRefreshed = true;
    }

    private String trimCaloriesLabel(String tdeeWithCalories) {
        int firstWhitespace = tdeeWithCalories.indexOf(" ");
        if (wasRefreshed) {
            return tdeeWithCalories.substring(0, firstWhitespace);
        }
        if (tdeeWithCalories.length() > 4) {
            Toast.makeText(getContext(),
                    "Please enter a TDEE with less than 10000 Calories", Toast.LENGTH_LONG).show();
        }
        else {
            return tdeeWithCalories;
        }
        etTDEE.setText(tdeeWithCalories.substring(0, 4));
        return tdeeWithCalories.substring(0, 4);

    }

    private static String addCaloriesLabel(double tdee) {
        int intTDEE = (int) Math.round(tdee);
        String strTDEE = String.valueOf(intTDEE) + " Calories per day";
        return strTDEE;
    }
}
