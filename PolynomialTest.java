import java.util.List;

public class PolynomialTest {
    private static void printPassMsg(boolean passed) {
        System.out.println("Passed: " + passed);
    }

    private static boolean toStrEquals(Polynomial n, String template) {
        return n.toString().equals(template);
    }

    private static void testAdd(Polynomial n) {
        System.out.println(n);
        printPassMsg(toStrEquals(n, ""));
        n.add(7, 7).add(6, 6).add(5, 5).add(4, 4);
        System.out.println(n);
        printPassMsg(toStrEquals(n, "7x^7 + 6x^6 + 5x^5 + 4x^4"));
        n.add(3, 3).add(2, 2).add(1, 1).add(0, 0);
        System.out.println(n);
        printPassMsg(toStrEquals(n, "7x^7 + 6x^6 + 5x^5 + 4x^4 + 3x^3 + 2x^2 + x + 1"));
        n.add(0, 0);
        System.out.println(n);
        printPassMsg(toStrEquals(n, "7x^7 + 6x^6 + 5x^5 + 4x^4 + 3x^3 + 2x^2 + x + 2"));
        n.clear();
        System.out.println(n);
        printPassMsg(toStrEquals(n, ""));
    }

    private static void printEvalResult(Polynomial n, double x) {
        System.out.println("x = " + x + ", " + "y = " + n.evaluate(x));
    }

    private static void testLinearRoot(Polynomial n) {
        n.add(1, 1);
        printEvalResult(n, 0);
        printEvalResult(n, 1);
        printEvalResult(n, 2);
        List<double[]> zeros = n.getRealZeros();
        double[] zero1 = zeros.getFirst();
        String solution = "(" + zero1[0] + ", " + zero1[1] + ")";
        boolean passed = solution.equals("(0.0, 0.0)");
        System.out.println(solution + "\n" + "Passed: " + passed);
        n.clear();
        n.add(1, 1).add(1, 0);
        zeros = n.getRealZeros();
        zero1 = zeros.getFirst();
        solution = "(" + zero1[0] + ", " + zero1[1] + ")";
        passed = solution.equals("(-1.0, 0.0)");
        System.out.println(solution + "\n" + "Passed: " + passed);
    }

    private static void testQuadraticSingleRoot(Polynomial n) {
        n.clear();
        n.add(1, 2).add(-1, 0); // x^2 - 1
        List<double[]> zeros = n.getRealZeros();
        System.out.println("Quadratic: x^2 - 1");
        for (double[] zero : zeros) {
            double x = zero[0];
            double y = n.evaluate(x);
            System.out.println("Zero: (" + x + ", " + y + ")");
            printPassMsg(Math.abs(y) < Polynomial.getAlmostZero());
        }
        n.clear();
        n.add(1, 2).add(2, 1).add(1, 0); // x^2 + 2x + 1 = 0, (x+1)^2 = 0, double root at -1
        zeros = n.getRealZeros();
        System.out.println("Quadratic: x^2 + 2x + 1");
        for (double[] zero : zeros) {
            double x = zero[0];
            double y = n.evaluate(x);
            System.out.println("Zero: (" + x + ", " + y + ")");
            printPassMsg(Math.abs(y) < Polynomial.getAlmostZero());
        }
    }

    private static void testCubicSingleRoot(Polynomial n) {
        n.clear();
        n.add(1, 3).add(-1, 0); // x^3 - 1 = 0, root at 1
        List<double[]> zeros = n.getRealZeros();
        System.out.println("Cubic: x^3 - 1");
        for (double[] zero : zeros) {
            double x = zero[0];
            double y = n.evaluate(x);
            System.out.println("Zero: (" + x + ", " + y + ")");
            printPassMsg(Math.abs(y) < Polynomial.getAlmostZero());
        }
    }

    private static void testCubicMultipleRoots(Polynomial n) {
        n.clear();
        // (x-1)(x-2)(x+3) = x^3 - 7x + 6 -> roots: 1, 2, -3
        n.add(1, 3).add(0, 2).add(-7, 1).add(6, 0);
        List<double[]> zeros = n.getRealZeros();
        System.out.println("Cubic (3 distinct roots): x^3 - 7x + 6");
        boolean found1 = false;
        boolean found2 = false;
        boolean foundNeg3 = false;
        for (double[] zero : zeros) {
            double x = zero[0];
            double y = n.evaluate(x);
            System.out.println("Zero: (" + x + ", " + y + ")");
            printPassMsg(Math.abs(y) < Polynomial.getAlmostZero());
            if (Math.abs(x - 1.0) <= Math.max(Polynomial.getAlmostZero(), 0.01))
                found1 = true;
            if (Math.abs(x - 2.0) <= Math.max(Polynomial.getAlmostZero(), 0.01))
                found2 = true;
            if (Math.abs(x + 3.0) <= Math.max(Polynomial.getAlmostZero(), 0.01))
                foundNeg3 = true;
        }
        printPassMsg(found1 && found2 && foundNeg3);
    }

    private static void testQuarticDoubleRoots(Polynomial n) {
        n.clear();
        // (x-1)^2 (x+2)^2 = x^4 + 2x^3 -3x^2 -4x +4 -> double roots at 1 and -2
        n.add(1, 4).add(2, 3).add(-3, 2).add(-4, 1).add(4, 0);
        List<double[]> zeros = n.getRealZeros();
        System.out.println("Quartic (double roots at 1 and -2): (x-1)^2 (x+2)^2");
        boolean found1 = false;
        boolean foundNeg2 = false;
        for (double[] zero : zeros) {
            double x = zero[0];
            double y = n.evaluate(x);
            System.out.println("Zero: (" + x + ", " + y + ")");
            printPassMsg(Math.abs(y) < Polynomial.getAlmostZero());
            if (Math.abs(x - 1.0) <= Math.max(Polynomial.getAlmostZero(), 0.01))
                found1 = true;
            if (Math.abs(x + 2.0) <= Math.max(Polynomial.getAlmostZero(), 0.01))
                foundNeg2 = true;
        }
        printPassMsg(found1 && foundNeg2);
    }

    private static void testQuadraticDoubleRoot(Polynomial n) {
        n.clear();
        // (x-2)^2 = x^2 - 4x + 4 -> double root at 2
        n.add(1, 2).add(-4, 1).add(4, 0);
        List<double[]> zeros = n.getRealZeros();
        System.out.println("Quadratic (double root at 2): (x-2)^2");
        boolean found2 = false;
        for (double[] zero : zeros) {
            double x = zero[0];
            double y = n.evaluate(x);
            System.out.println("Zero: (" + x + ", " + y + ")");
            printPassMsg(Math.abs(y) < Polynomial.getAlmostZero());
            if (Math.abs(x - 2.0) <= Math.max(Polynomial.getAlmostZero(), 0.01))
                found2 = true;
        }
        printPassMsg(found2);
    }

    public static void runAutoTests(Polynomial n) {
        testAdd(n);
        testLinearRoot(n);
        testQuadraticSingleRoot(n);
        testQuadraticDoubleRoot(n);
        testCubicSingleRoot(n);
        testCubicMultipleRoots(n);
        testQuarticDoubleRoots(n);
    }

    public static void main(String[] args) {
        Polynomial n = new Polynomial();
        runAutoTests(n);
    }
}