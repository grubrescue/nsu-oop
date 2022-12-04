package ru.nsu.fit.smolyakov.caclulator.complex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing basic arithmetic operations on complex numbers.
 * 
 * <p>
 * 
 */
public record Complex(double r, double i) {
    public static Complex valueOf(String from) {
        Pattern regexPattern = 
            Pattern.compile("(?<real>-??\\d+)(?<imaginarySign>\\+|-)(?<imaginary>\\d+)i"); 
            // TODO: floating-number digits parsing

        Matcher matcher = regexPattern.matcher(from);
        if (!matcher.matches()) {
            throw new NumberFormatException("complex number have to be presented as A+Bi");
        }

        double r = Double.parseDouble(matcher.group("real"));
        double i = Double.parseDouble(matcher.group("imaginary"));

        if (matcher.group("imaginarySign").equals("-")) {
            i *= -1;
        }

        return new Complex(r, i);
    }

    public Complex negate() {
        return new Complex(-this.r, -this.i);
    }

    public Complex add(Complex a) {
        return new Complex(this.r + a.r, this.i + a.i);
    }

    public Complex substract(Complex a) {
        return add(a.negate());
    }

    public Complex multiply(Complex a) {
        return new Complex(
            this.r * a.r - this.i * a.i, 
            this.r * a.i + this.i * a.r
        );
    }

    /**
     * 
     * @param a
     * @return
     * 
     */
    public Complex divide(Complex a) {
        if (a.r == 0 && a.i == 0) {
            return new Complex(Double.NaN, Double.NaN);
        }

        double rTmp = 
            (this.r * a.r + this.i * a.i) 
            / (a.r * a.r + a.i * a.i);
        double iTmp = 
            (a.r * this.i - this.r * a.i) 
            / (a.r * a.r + a.i * a.i);

        return new Complex(rTmp, iTmp);
    }

    public Complex sin() {
        return new Complex(
            Math.sin(r) * Math.cosh(i),
            Math.cos(r) * Math.sinh(i)
        );
    }

    public Complex cos() {
        return new Complex(
            Math.cos(r) * Math.cosh(i),
            - Math.sin(r) * Math.sinh(i)
        );
    }
}
