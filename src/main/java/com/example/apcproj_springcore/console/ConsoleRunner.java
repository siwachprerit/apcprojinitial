package com.example.apcproj_springcore.console;

import com.example.apcproj_springcore.games.MinesGame;
import com.example.apcproj_springcore.games.PlinkoGame;
import com.example.apcproj_springcore.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleRunner {

    private final UserService userService;
    private final MinesGame minesGame;
    private final PlinkoGame plinkoGame;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleRunner(UserService userService, MinesGame minesGame, PlinkoGame plinkoGame) {
        this.userService = userService;
        this.minesGame = minesGame;
        this.plinkoGame = plinkoGame;
    }

    public void run() {
        while (true) {
            showWelcomeMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": handleLogin(); break;
                    case "2": handleSignup(); break;
                    case "3":
                        System.out.println("Thanks for visiting Console Casino! Goodbye.");
                        return;
                    default: System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showWelcomeMenu() {
        System.out.println("\n=== Welcome to Console Casino ===");
        System.out.println("1. Log in");
        System.out.println("2. Sign up");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private void handleLogin() throws Exception {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        userService.loginOrRegister(username, false);
        showCasinoMenu();
    }

    private void handleSignup() throws Exception {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();
        userService.loginOrRegister(username, true);
        showCasinoMenu();
    }

    private void showCasinoMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Casino Menu ===");
            System.out.println("1. Check balance");
            System.out.println("2. Play Mines Game");
            System.out.println("3. Play Plinko Game");
            System.out.println("4. Log out");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.println("Your balance: " + userService.getCurrentUser().getBalance());
                    break;
                case "2":
                    playGame(minesGame);
                    break;
                case "3":
                    playGame(plinkoGame);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    userService.logout();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void playGame(Object game) {
        System.out.print("Enter bet amount: ");
        double bet;
        try {
            bet = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }

        if (!userService.canBet(bet)) {
            System.out.println("Insufficient balance or invalid amount.");
            return;
        }

        double net = 0.0;
        try {
            if (game instanceof MinesGame) net = ((MinesGame) game).play(scanner, bet);
            else if (game instanceof PlinkoGame) net = ((PlinkoGame) game).play(scanner, bet);
            userService.applyBalanceChange(net);
            System.out.println("Updated balance: " + userService.getCurrentUser().getBalance());
        } catch (Exception e) {
            System.out.println("Game error: " + e.getMessage());
        }
    }
}
