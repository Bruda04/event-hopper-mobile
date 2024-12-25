package com.ftn.eventhopper.filters;

import android.text.InputFilter;
import android.text.Spanned;

public class MinMaxInputFilter  implements InputFilter {
    private final double min;
    private final double max;

    public MinMaxInputFilter(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            // Concatenate new input with the existing text
            String input = dest.toString() + source.toString();
            double value = Double.parseDouble(input);

            // Check if value is within range
            if (value < min || value > max) {
                return ""; // Reject input if out of range
            }
        } catch (NumberFormatException e) {
            // Ignore invalid input (e.g., empty or incomplete input)
        }
        return null; // Accept input if valid
    }
}