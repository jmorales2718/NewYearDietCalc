package jmmacbook.newyeardietcalc1.calculations;

import android.content.Context;
import android.util.Log;

import jmmacbook.newyeardietcalc1.MainActivity;
import jmmacbook.newyeardietcalc1.R;


/**
 * Created by jmmacbook on 12/9/15.
 */
public class Calculations {
    private Context context;
    private double defaultDeficit;
    private double defaultSurplus;

    public Calculations() {
    }

    public Calculations(Context context) {
        this.context = context;
    }

    public double calculateTDEE(double LBM, double activityMult) {
        double finalTDEE = (370 + (21.6 * LBM)) * activityMult;
        return finalTDEE;
    }

    public double calculateActivityLevel(String activityLevel) {
        double activityMult = -1.0;
        if (activityLevel.equals("Less than 1 hour")) {
            activityMult = 1.1;
        }
        else if (activityLevel.equals("1-3 hours")) {
            activityMult = 1.2;
        }
        else if (activityLevel.equals("4-6 hours")) {
            activityMult = 1.35;
        }
        else if (activityLevel.equals("6+ hours")) {
            activityMult = 1.45;
        }
        return activityMult;
    }

    public double calculateLBM(boolean imperial, int weight, double bodyfat) {
        double argWeight = weight;
        if (imperial == true) {
            argWeight = argWeight / 2.2;
        }
        double bfDecimal = bodyfat / 100;
        double fatMass = argWeight * bfDecimal;
        double leanBodyMass = argWeight - fatMass;
        return leanBodyMass;
    }

    public int calculateMacroSpinnerSelection(String spinnerStringSelected) {
        if (spinnerStringSelected.equals(context.getResources().getString(R.string.cutting_low_fat))) {
            return 0;
        }
        else if (spinnerStringSelected.equals(context.getResources().getString(R.string.cutting_low_carb))) {
            return 1;
        }
        else if (spinnerStringSelected.equals(context.getResources().getString(R.string.bulking_high_carb))) {
            return 2;
        }
        else if (spinnerStringSelected.equals(context.getResources().getString(R.string.bulking_high_fat))) {
            return 3;
        }
        return -1;
    }

    public double[] calculateMacros(int macroSpinnerSelection, double tdee, double bodyweight) {
        double[] macros = new double[3];


        defaultSurplus = MainActivity.getDefaultSurplusMultiplier() + 1;
        Log.d("SURPLUS: ", String.valueOf(defaultSurplus));

        defaultDeficit = 1 - MainActivity.getDefaultDeficitMultiplier();


        switch (macroSpinnerSelection) {
            case 0:
                tdee *= defaultDeficit;
                macros[0] = (tdee * 0.2) / 9;
                macros[1] = (tdee * 0.4) / 4;
                macros[2] = (tdee * 0.4) / 4;
                return macros;
            case 1:
                tdee *= defaultDeficit;
                macros[0] = (tdee * 0.4) / 9;
                macros[1] = (tdee * 0.2) / 4;
                macros[2] = (tdee * 0.4) / 4;
                return macros;
            case 2:
                tdee *= defaultSurplus;
                macros[2] = bodyweight;
                macros[0] = (tdee * 0.2);
                macros[1] = (tdee - macros[0] - (macros[2] * 4)) / 4;
                macros[0] = macros[0] / 9;
                return macros;
            case 3:
                tdee *= defaultSurplus;
                macros[2] = bodyweight;
                macros[0] = (tdee * 0.4);
                macros[1] = (tdee - macros[0] - macros[2] * 4) / 4;
                macros[0] = macros[0] / 9;
                return macros;
        }
        return macros;
    }

    public int[] roundMacros(double[] macrosArr) {
        int[] toReturn = new int[macrosArr.length];
        for (int i = 0; i < macrosArr.length; i++) {
            toReturn[i] = (int) Math.round(macrosArr[i]);
        }
        return toReturn;
    }


}
