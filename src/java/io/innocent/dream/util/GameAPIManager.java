package io.innocent.dream.util;

import org.gamejolt.GameJoltAPI;
import org.gamejolt.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameAPIManager {

    private static final int ID = 584899;
    private static final String KEY = "f6c12c748a1ad2951b888d255c0f6742";
    private final GameJoltAPI gameAPI;
    private boolean loggedIn = false;

    public GameAPIManager() {
        gameAPI = new GameJoltAPI(ID, KEY);
        File credentials = new File(".gj-credentials");
        if (credentials.exists()) {
            try {
                Scanner scanner = new Scanner(credentials);
                String number = scanner.nextLine();
                String username = scanner.nextLine();
                String id = scanner.nextLine();
                System.out.println("Number: " + number +
                        "\nUsername: " + username +
                        "\nId: " + id);
                System.out.println("Setting User: " + username);
                if (logIn(username, id)) {
                    System.out.println("Successfully Logged in as user: " + username);
                } else {
                    System.out.println("Could not log in");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean logIn(String username, String id) {
        User user = gameAPI.getUser(username);
        if (user == null) {
            System.out.println("No User with Username: " + username);
            return false;
        }
        if (!gameAPI.verifyUser(username, id)) {
            return false;
        }
        System.out.println("User Verified");
        loggedIn = true;
        return true;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
