package io.innocent.dream.util;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.worldBuilder.WorldSaver;
import org.gamejolt.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Scanner;

public class GameAPIManager {

    private static final int ID = 584899;
    private static final String KEY = "f6c12c748a1ad2951b888d255c0f6742";
    private final GameJoltAPI gameAPI;
    private boolean loggedIn = false;
    private String username;
    private String number;
    private String key;
    private User currentUser;
    private JSONObject object;
    private int score = 0;

    public GameAPIManager() {
        gameAPI = new GameJoltAPI(ID, KEY);
        File credentials = new File(".gj-credentials");
        if (credentials.exists()) {
            try {
                Scanner scanner = new Scanner(credentials);
                number = scanner.nextLine();
                username = scanner.nextLine();
                key = scanner.nextLine();
                System.out.println("Number: " + number +
                        "\n\tUsername: " + username +
                        "\n\tKey: " + key);
                System.out.println("Setting User: " + username);
                if (logIn(username, key)) {
                    System.out.println("Successfully Logged in as user: " + username);
                } else {
                    System.out.println("Could not log in");
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No .gj-credentials file found, looking for accounts.json now...");
            credentials = new File(InnocentDream.path + "\\account.json");
            if (credentials.exists()) {
                System.out.println("account.json file found. Reading credentials...");
                try (FileReader reader = new FileReader(credentials)) {
                    JSONObject user = (JSONObject) new JSONParser().parse(reader);
                    reader.close();
                    String username = (String) user.get("username");
                    String token = (String) user.get("token");
                    if (logInFromFile(username, token, credentials)) {
                        System.out.println("Successfully Logged in as user: " + username);
                    } else {
                        System.out.println("Could not log in");
                        return;
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                System.out.println("No accounts.json file found. Will not log in.");
                return;
            }
        }
        gameAPI.sessionOpen();
        String data = "";
        try {
            System.out.println("Loading player data...");
            data = gameAPI.getDataStore(DataStore.DataStoreType.GAME, username).getData();
            object = (JSONObject) WorldSaver.PARSER.parse(data);
            String strScore = String.valueOf(object.get("score"));
            score = Integer.parseInt(strScore);
        } catch (Exception e) {
            System.out.println("Player data doesn't exist, creating new player data...");
            object = new JSONObject();
            object.put("playerName", currentUser.getName());
            score = 0;
            object.put("score", score);
        }
    }
    
    public boolean logInFromFile(String username, String token, File file) {
    	boolean success = logIn(username, token);
    	if (!success) {
    		file.delete();
    	}
    	return success;
    }
    
    public boolean logIn(String username, String token) {
        User user = gameAPI.getUser(username);
        if (user == null) {
            System.out.println("No User with Username: " + username);
            return false;
        }
        if (!gameAPI.verifyUser(username, token)) {
            return false;
        }
        System.out.println("User Verified");
        loggedIn = true;
        this.currentUser = user;
        this.username = username;
        JSONObject userObj = new JSONObject();
        userObj.put("username", username);
        userObj.put("token", token);
        File creds = new File(InnocentDream.path + "\\account.json");
        if (!creds.exists()) {
            try {
                creds.createNewFile();
                FileWriter writer = new FileWriter(creds);
                userObj.writeJSONString(writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameAPI.sessionOpen();
        String data = "";
        try {
            System.out.println("Loading player data...");
            data = gameAPI.getDataStore(DataStore.DataStoreType.GAME, username).getData();
            object = (JSONObject) WorldSaver.PARSER.parse(data);
            String strScore = String.valueOf(object.get("score"));
            score = Integer.parseInt(strScore);
        } catch (Exception e) {
            System.out.println("Player data doesn't exist, creating new player data...");
            object = new JSONObject();
            object.put("playerName", currentUser.getName());
            score = 0;
            object.put("score", score);
        }
        return true;
    }

    public void score(int score, int id) {
        this.score += score;
        gameAPI.addHighscore(id, String.valueOf(this.score), this.score);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void stop() {
        try {
            System.out.println("Saving player data and closing session...");
            object.put("score", score);
            String playerData = object.toJSONString();
            System.out.println(playerData);
            gameAPI.setDataStore(DataStore.DataStoreType.GAME, username, playerData);
            gameAPI.sessionClose();
            System.out.println("Successfully closed session");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

}
