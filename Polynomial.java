import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Polynomial {
    private final List<PolynomialNumber> list = new ArrayList<>();
    private final Set<Integer> degrees = new HashSet<>();
    private static final double DEFAULT_ALMOST_ZERO = 0.0000000001;
    private static final double ALMOST_ZERO_MAX = 0.0001;
    private static double almostZero = DEFAULT_ALMOST_ZERO;

    public Polynomial() {
    }

    public Polynomial(PolynomialNumber[] arr) {
        Collections.addAll(list, arr);
    }

    public Polynomial(List<PolynomialNumber> list) {
        this.list.addAll(list);
    }

    public Polynomial add(int x, int degree) {
        boolean added = degrees.add(degree);
        if (!added) {
            consolidateSameDegree(x, degree);
        } else {
            list.add(new PolynomialNumber(x, degree));
            sortByDegree();
        }
        return this;
    }

    private void consolidateSameDegree(int x, int degree) {
        int toAdd = x == 0 && degree == 0 ? 1 : x;
        if (list.getFirst().getDegree() == degree) {
            list.getFirst().addCoefficient(toAdd);
            return;
        }
        if (list.getLast().getDegree() == degree) {
            list.getLast().addCoefficient(toAdd);
            return;
        }
        binarySearch(toAdd, degree);
    }

    private void binarySearch(int x, int degree) {
        int min = 0;
        int max = list.size() - 1;
        while (min <= max) {
            int mid = min + (max - min) / 2;
            PolynomialNumber num = list.get(mid);
            if (num.getDegree() == degree) {
                num.addCoefficient(x);
                return;
            }
            if (num.getDegree() < degree) {
                min = mid + 1;
            } else {
                max = mid - 1;
            }
        }
    }

    private void sortByDegree() {
        Collections.sort(list);
        Collections.reverse(list);
    }

    public void clear() {
        list.clear();
        degrees.clear();
    }

    public static boolean sameSign(PolynomialNumber a, PolynomialNumber b) {
        return a.sameSign(b);
    }

    public static double avg(PolynomialNumber a, PolynomialNumber b) {
        return (a.getCoefficient() + b.getCoefficient()) / 2.0;
    }

    public double evaluate(double x) {
        double result = 0;
        for (PolynomialNumber num : list) {
            result += num.getCoefficient() * Math.pow(x, num.getDegree());
        }
        return result;
    }

    public Polynomial derivative() {
        Polynomial deriv = new Polynomial();
        for (PolynomialNumber num : list) {
            if (num.getDegree() > 0) {
                deriv.add(num.getCoefficient() * num.getDegree(), num.getDegree() - 1);
            }
        }
        return deriv;
    }

    public List<double[]> getRealZeros() {
        List<double[]> zeros = new ArrayList<>();
        switch (list.getFirst().getDegree()) {
            case 1 -> addLinearZero(zeros);
            case 2 -> addQuadraticZeros(zeros);
            default -> addZerosOfHigherPolynomial(zeros);
        }
        return zeros;
    }

    private void addLinearZero(List<double[]> zeros) {
        if (list.size() == 1) {
            zeros.add(new double[] { 0.0, 0.0 });
            return;
        }
        double b = list.get(1).getCoefficient();
        double m = list.getFirst().getCoefficient();
        // y = mx + b
        // 0 = mx + b, -b = mx, x = -b/m
        zeros.add(new double[] { -b / m, 0.0 });
    }

    private void addQuadraticZeros(List<double[]> zeros) {
        double a = list.getFirst().getCoefficient();
        int size = list.size();
        if (size == 1) {
            zeros.add(new double[] { 0.0, 0.0 });
            return;
        }
        if (size == 2 && list.get(1).getDegree() == 1) {
            cIsZero(zeros, a);
            return;
        }
        if (size == 2 && list.get(1).getDegree() == 0) {
            bIsZero(zeros, a);
            return;
        }
        fullQuadratic(zeros, a);
    }

    private void cIsZero(List<double[]> zeros, double a) {
        // 0 = ax^2 + bx
        // x(ax + b) = 0
        // x = 0, -b / a
        double b = list.get(1).getCoefficient();
        zeros.add(new double[] { 0.0, 0.0 });
        zeros.add(new double[] { -b / a, 0.0 });
    }

    private void bIsZero(List<double[]> zeros, double a) {
        // 0 = ax^2 + c
        // x^2 = -c / a
        // sqrt(-c/a) = (+-)x -> two real solutions if -c/a >0
        double c = list.get(1).getCoefficient();
        if (a * c >= 0) {
            return;
        }
        double solution = Math.sqrt(-c / a);
        zeros.add(new double[] { solution, 0.0 });
        zeros.add(new double[] { -solution, 0.0 });
    }

    private void fullQuadratic(List<double[]> zeros, double a) {
        // y = (-b (+-)sqrt(b^2 - 4ac)) / 2a
        // 0 = (-b + discriminant) / 2a, 0 = (-b - discriminant) / 2a
        double b = list.get(1).getCoefficient();
        double c = list.get(2).getCoefficient();
        double discriminant = (b * b) - (4 * a * c);
        if (discriminant < 0) {
            return;
        }
        if (discriminant == 0) {
            zeros.add(new double[] { -b / (2 * a) });
            return;
        }
        double sqrtD = Math.sqrt(discriminant);
        double solution1 = (-b + sqrtD) / (2 * a);
        double solution2 = (-b - sqrtD) / (2 * a);
        zeros.add(new double[] { solution1, 0.0 });
        zeros.add(new double[] { solution2, 0.0 });
    }

    private double newtonRaphson(double initialGuess) {
        Polynomial deriv = derivative();
        double x = initialGuess;
        for (int i = 0; i < 100; i++) {
            double fx = evaluate(x);
            double fpx = deriv.evaluate(x);
            if (Math.abs(fpx) < almostZero) {
                break;
            }
            double newX = x - fx / fpx;
            if (Math.abs(newX - x) < almostZero) {
                break;
            }
            x = newX;
        }
        return x;
    }

    private void addZerosOfHigherPolynomial(List<double[]> zeros) {
        List<Double> foundRoots = new ArrayList<>();
        for (double guess = -10; guess <= 10; guess += 0.5) {
            double root = newtonRaphson(guess);
            if (Double.isNaN(root) || Double.isInfinite(root)) {
                continue;
            }
            double val = normalizeZero(evaluate(root));
            handleDuplicate(zeros, foundRoots, val, root);
        }
    }

    private void handleDuplicate(List<double[]> zeros, List<Double> foundRoots, double val, double root) {
        if (Math.abs(val) <= almostZero && !isDuplicate(foundRoots, normalizeZero(root))) {
            double toAdd = normalizeZero(root);
            foundRoots.add(toAdd);
            zeros.add(new double[] { toAdd, 0.0 });
        }
    }

    private boolean isDuplicate(List<Double> foundRoots, double root) {
        for (Double r : foundRoots) {
            if (Math.abs(r - root) <= almostZero) {
                return true;
            }
        }
        return false;
    }

    private static double normalizeZero(double num) {
        return Math.round(num * 10000.0) / 10000.0;
    }

    public List<PolynomialNumber> getList() {
        return list;
    }

    public static double getAlmostZero() {
        return almostZero;
    }

    public static void setAlmostZero(double value) {
        if (value > ALMOST_ZERO_MAX) {
            System.out.println("Invalid value: " + value);
            return;
        }
        Polynomial.almostZero = value;
    }

    @Override
    public String toString() {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(list.get(0).toString());
        sb.append(" ");
        for (int i = 1; i < list.size(); i++) {
            PolynomialNumber n = list.get(i);
            sb.append(n.getSign()).append(" ").append(n).append(" ");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}