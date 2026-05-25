public class PolynomialNumber implements Comparable<PolynomialNumber> {
    private int coefficient;
    private final int degree;
    private final String sign;

    public PolynomialNumber(int coefficient, int degree) {
        this.sign = coefficient >= 0 ? "+" : "-";
        this.coefficient = degree == 0 && coefficient == 0 ? 1 : coefficient;
        this.degree = degree;
    }

    public PolynomialNumber(int coefficient) {
        this(coefficient, 1);
    }

    public PolynomialNumber() {
        this.sign = "+";
        this.coefficient = 1;
        this.degree = 0;
    }

    public void addCoefficient(int n) {
        this.coefficient += n;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public int getDegree() {
        return degree;
    }

    public String getSign() {
        return sign;
    }

    public boolean sameSign(PolynomialNumber other) {
        return (coefficient == 0 && other.getCoefficient() == 0) || (coefficient < 0 && other.getCoefficient() < 0)
                || (coefficient > 0 && other.getCoefficient() > 0);
    }

    @Override
    public int compareTo(PolynomialNumber other) {
        return Integer.compare(degree, other.getDegree());
    }

    @Override
    public boolean equals(Object o) {
        return this == o
                || (o instanceof PolynomialNumber other && coefficient == other.getCoefficient()
                        && degree == other.getDegree());
    }

    @Override
    public int hashCode() {
        return String.valueOf(coefficient).hashCode() + String.valueOf(degree).hashCode();
    }

    @Override
    public String toString() {
        if (degree == 0) {
            return String.valueOf(coefficient);
        }
        if (coefficient == 1 && degree == 1) {
            return "x";
        }
        if (coefficient == 1) {
            return "x^" + degree;
        }
        if (coefficient == 0) {
            return "";
        }
        if (degree == 1) {
            return coefficient + "x";
        }
        return coefficient + "x^" + degree;
    }
}