package com.example.apcproj_springcore.games;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Scanner;

@Component
public class PlinkoGame {

    private final int rows = 10;
    private final Random random = new Random();

    public double play(Scanner sc, double bet) throws InterruptedException {
        System.out.println("\n=== Plinko ===");
        System.out.println("You placed a bet: " + String.format("%.2f", bet));
        System.out.print("Press Enter to drop the ball...");
        sc.nextLine();

        int position = 0;
        for (int r = 0; r < rows; r++) {
            drawPyramid(r, position);
            Thread.sleep(300);

            if (r < rows - 1) {
                double bias = 0.7;
                int middle = r / 2;
                if (position > 0 && random.nextDouble() < bias) position--;
                else if (position < r) position++;
            }
        }

        drawPyramid(rows - 1, position);

        double multiplier = getMultiplier(position);
        double winnings = bet * multiplier;
        double net = winnings - bet;

        System.out.println("\nBall landed at position " + position + " → Multiplier: x" + String.format("%.2f", multiplier));
        System.out.println("You won: " + String.format("%.2f", winnings) + " (net " + String.format("%.2f", net) + ")");

        return net;
    }

    private void drawPyramid(int currentRow, int ballPos) {
        if (currentRow > 0) System.out.print("\033[" + (currentRow + 1) + "A");

        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            int spaces = rows - r;

            for (int s = 0; s < spaces; s++) sb.append(" ");

            for (int c = 0; c <= r; c++) {
                if (r == currentRow && c == ballPos) sb.append("○ ");
                else sb.append("● ");
            }
            System.out.println(sb);
        }
    }

    private double getMultiplier(int position) {
        int middle = rows / 2;
        int distance = Math.abs(position - middle);
        return 1 + distance * 2;
    }
}
