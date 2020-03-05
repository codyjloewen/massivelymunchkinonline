package mmo;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class SafeScanner {
    private static final String INPUT_MISMATCH_ERROR = "Invalid input. Try again: ";
    final Scanner scanner;

    public SafeScanner() {
        scanner = new Scanner(System.in);
    }

    public int nextInt() {
        Integer input = null;
        while (Objects.isNull(input)) {
            try {
                input = scanner.nextInt();
            } catch (final InputMismatchException e) {
                System.out.println(INPUT_MISMATCH_ERROR);
                scanner.nextLine();
            }
        }
        return input;
    }

    public int nextInt(final int upperBoundInclusive) {
        int input = this.nextInt();
        while (input > upperBoundInclusive || input <= 0) {
            System.out.println(INPUT_MISMATCH_ERROR);
            scanner.nextLine();
            input = this.nextInt();
        }
        return input;
    }

    public String nextLine() {
        return scanner.nextLine();
    }
}
