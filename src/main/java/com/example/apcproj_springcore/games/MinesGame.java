package com.example.apcproj_springcore.games;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Scanner;

@Component
public class MinesGame {

    private final int SIZE = 5;
    private final int MINES = 5;
    private final boolean[][] mines = new boolean[SIZE][SIZE];
    private final boolean[][] revealed = new boolean[SIZE][SIZE];
    private final String[][] display = new String[SIZE][SIZE];

    public MinesGame() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) display[r][c] = "■";
        }
        placeMines();
    }

    private void placeMines() {
        Random r = new Random();
        int placed = 0;
        while (placed < MINES) {
            int a = r.nextInt(SIZE);
            int b = r.nextInt(SIZE);
            if (!mines[a][b]) {
                mines[a][b] = true;
                placed++;
            }
        }
    }

    public double play(Scanner sc, double bet) {
        double multiplier = 1.0;
        System.out.println("\n=== Mines Game ===");
        System.out.println("Grid: " + SIZE + "x" + SIZE + ", Mines: " + MINES);
        System.out.println("You placed a bet: " + String.format("%.2f", bet));

        while (true) {
            drawGrid();
            System.out.print("Choose row (0-" + (SIZE - 1) + "): ");
            int r = readInt(sc, 0, SIZE - 1);
            System.out.print("Choose col (0-" + (SIZE - 1) + "): ");
            int c = readInt(sc, 0, SIZE - 1);

            if (revealed[r][c]) {
                System.out.println("Cell already chosen. Pick another.");
                continue;
            }

            revealed[r][c] = true;

            if (mines[r][c]) {
                display[r][c] = "💣";
                drawGrid();
                System.out.println("💥 Boom! You hit a mine. You lose your bet of " + String.format("%.2f", bet));
                return -bet;
            } else {
                display[r][c] = "💎";
                multiplier += 0.5;
                drawGrid();
                System.out.print("✅ Safe! Current potential multiplier: x" + String.format("%.2f", multiplier));
                System.out.print("\nDo you want to cash out now? (Y/N): ");
                String ans = sc.nextLine().trim();
                if (ans.equalsIgnoreCase("Y")) {
                    double winnings = bet * multiplier;
                    double net = winnings - bet;
                    System.out.println("You cashed out! Winnings: " + String.format("%.2f", winnings) +
                            " (net +" + String.format("%.2f", net) + ")");
                    return net;
                } else {
                    System.out.println("Continuing... pick another cell");
                }
            }
        }
    }

    private void drawGrid() {
        System.out.println();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(display[r][c] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private int readInt(Scanner sc, int min, int max) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    System.out.print("Out of range. Enter (" + min + "-" + max + "): ");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}
