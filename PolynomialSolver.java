import java.util.List;
import java.util.Scanner;

public class PolynomialSolver {
    private static boolean isRunning = false;

    private static void printMenu() {
        System.out.println("Polynomial Solver");
        System.out.println("1) Manual: create polynomial and find zeros");
        System.out.println("2) Run auto tests (PolynomialTest)");
        System.out.println("3) Exit");
        System.out.print("Choose an option: ");
    }

    private static boolean isValidNumber(String line) {
        try {
            Integer.valueOf(line);
        } catch (NumberFormatException _) {
            System.out.println("Invalid number; returning to menu.");
            return false;
        }
        return true;
    }

    private static boolean addedValidNumbers(Polynomial p, String[] parts) {
        try {
            int coeff = Integer.parseInt(parts[0]);
            int degree = Integer.parseInt(parts[1]);
            p.add(coeff, degree);
        } catch (NumberFormatException _) {
            System.out.println("Invalid numbers; try again.");
            return false;
        }
        return true;
    }

    private static void manualMode(Scanner input) {
        Polynomial p = new Polynomial();
        System.out.print("How many terms will the polynomial have? ");
        String line = input.nextLine().trim();
        int terms;
        if (!isValidNumber(line)) {
            return;
        }
        terms = Integer.parseInt(line);
        int i = 0;
        while (i < terms) {
            System.out.print("Enter coefficient and degree separated by space (e.g. '3 2'): ");
            String entry = input.nextLine().trim();
            if (entry.isEmpty()) {
                continue;
            }
            String[] parts = entry.split("\\s+");
            if (parts.length < 2) {
                System.out.println("Invalid input; try again.");
                continue;
            }
            if (!addedValidNumbers(p, parts)) {
                continue;
            }
            i++;
        }
        System.out.println("Polynomial: " + p);
        List<double[]> zeros = p.getRealZeros();
        if (zeros.isEmpty()) {
            System.out.println("No real zeros found.");
            return;
        }
        System.out.println("Possible real zeros (x, f(x)):");
        for (double[] z : zeros) {
            double x = z[0];
            double y = p.evaluate(x);
            System.out.printf("(%.3f, %.3f)%n", x, y);
        }
    }

    private static void handleInput(Scanner input) {
        printMenu();
        String choice = input.nextLine().trim();
        switch (choice) {
            case "1" -> manualMode(input);
            case "2" -> PolynomialTest.runAutoTests(new Polynomial());
            case "3" -> {
                System.out.println("Exiting.");
                isRunning = false;
            }
            default -> System.out.println("Invalid choice.");
        }
        System.out.println();
    }

    private static void run(Scanner input) {
        while (isRunning) {
            handleInput(input);
        }
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            isRunning = true;
            run(input);
        }
    }
}
