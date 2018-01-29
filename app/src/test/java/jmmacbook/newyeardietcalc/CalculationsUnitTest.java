package jmmacbook.newyeardietcalc;

import org.junit.Test;

import jmmacbook.newyeardietcalc1.calculations.Calculations;

import static org.junit.Assert.*;

/**
 * Created by jmmacbook on 10/9/16.
 */

public class CalculationsUnitTest
{
    Calculations c = new Calculations();

    @Test
    public void calculateLBM_isCorrect() throws Exception
    {
        assertEquals(68.0027, c.calculateLBM(true, 170, 12), .5);
    }

    @Test
    public void calulateTDEE_isCorrect() throws Exception
    {
        assertEquals(2482.29, c.calculateTDEE(68.0027, 1.35), 5.0);
    }


}

