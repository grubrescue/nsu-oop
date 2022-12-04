package ru.nsu.fit.smolyakov.caclulator.complex;

/**
 * Class containing basic arithmetic operations on complex numbers.
 * 
 * <p>
 * 
 */
public record Complex(double r, double i) {
    public static Complex of(double r, double i) {
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
     * @throws ArithmeticException ???
     * 
     */
    public Complex divide(Complex a) {
        double rTmp = 
            (this.r * a.r - this.i * a.i) 
            / (a.r * a.r + a.i * a.i);
        double iTmp = 
            (a.r * this.i - this.r * a.r) 
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
            Math.sin(r) * Math.sinh(i)
        );
    }
}
