package com.aguilarcarboni.crazyeights;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {

    private static final String ADMIN_USERNAME = "admin";
    private final String gameDirectory;

    public GameManager(String gameName) {
        this.gameDirectory = "game/" + gameName;
    }

    public void initializeGame() throws IOException {

        System.out.println("Initializing game...");

        // Check if game already exists
        File gameDir = new File(gameDirectory);
        if (gameDir.exists()) { throw new IllegalStateException("Game already exists!"); }
        if (!gameDir.mkdirs()) { throw new IOException("Failed to create game directory"); }

        // Create users.txt file
        File usersFile = new File(gameDirectory + "/users.txt");
        if (!usersFile.createNewFile()) { throw new IOException("Failed to create users.txt file"); }

        // Get admin password from console
        String adminPassword = getPasswordSecurely("Enter admin password: ");
        if (adminPassword == null || adminPassword.trim().isEmpty()) { throw new IllegalArgumentException("Admin password cannot be empty"); }

        // Hash the admin password
        String hashedPassword = hashPassword(adminPassword);

        // Write the admin user to the file
        try (FileWriter writer = new FileWriter(usersFile)) {
            writer.write(ADMIN_USERNAME + "," + hashedPassword + "\n");
        }

        System.out.println("Game initialized successfully!\n");
    }

    public void addUser(String username) throws IOException {

        System.out.println("Adding user " + username + " to game " + gameDirectory);
        
        // Validate game exists
        File gameDir = new File(gameDirectory);
        if (!gameDir.exists()) { throw new IllegalStateException("Game does not exist!"); }

        // Validate username is not admin
        if (ADMIN_USERNAME.equals(username)) { throw new IllegalArgumentException("Cannot add user with reserved admin username"); }

        // Get admin password and validate
        String adminPassword = getPasswordSecurely("Enter admin password: ");
        if (!validateAdminPassword(adminPassword)) { throw new IllegalStateException("Invalid admin password"); }

        // Get new user's password
        String userPassword = getPasswordSecurely("Enter password for " + username + ": ");
        if (userPassword == null || userPassword.trim().isEmpty()) { throw new IllegalArgumentException("User password cannot be empty"); }

        // Hash the new user's password
        String hashedPassword = hashPassword(userPassword);

        // Add user to users.txt
        Path usersFile = Paths.get(gameDirectory, "users.txt");
        List<String> lines = Files.readAllLines(usersFile);
        
        // Check if user already exists
        if (lines.stream().anyMatch(line -> line.startsWith(username + ","))) { throw new IllegalStateException("User already exists!"); }

        // Append new user
        // Use StandardOpenOption.APPEND to add to the end of the file
        Files.write(usersFile, (username + "," + hashedPassword + "\n").getBytes(), StandardOpenOption.APPEND);

        System.out.println("User " + username + " added successfully!\n");
    }

    public void removeUser(String username) throws IOException {

        System.out.println("Removing user " + username + " from game " + gameDirectory);

        // Validate game exists
        File gameDir = new File(gameDirectory);
        if (!gameDir.exists()) { throw new IllegalStateException("Game does not exist!"); }

        // Cannot remove admin
        if (ADMIN_USERNAME.equals(username)) { throw new IllegalArgumentException("Cannot remove admin user"); }

        // Get admin password and validate
        String adminPassword = getPasswordSecurely("Enter admin password: ");
        if (!validateAdminPassword(adminPassword)) { throw new IllegalStateException("Invalid admin password"); }

        // Remove user from users.txt
        Path usersFile = Paths.get(gameDirectory, "users.txt");
        List<String> lines = Files.readAllLines(usersFile);
        List<String> updatedLines = lines.stream().filter(line -> !line.startsWith(username + ",")) .collect(Collectors.toList());

        // Check if user exists
        if (lines.size() == updatedLines.size()) { throw new IllegalStateException("User does not exist!"); }

        // Rewrite the entire file
        Files.write(usersFile, updatedLines);

        // Remove user's card file if it exists
        Path userCardFile = Paths.get(gameDirectory, username + ".txt");
        Files.deleteIfExists(userCardFile);

        System.out.println("User " + username + " removed successfully!\n");
    }

    private boolean validateAdminPassword(String password) throws IOException {
        Path usersFile = Paths.get(gameDirectory, "users.txt");
        List<String> lines = Files.readAllLines(usersFile);

        // Check if admin user exists
        String adminLine = lines.stream().filter(line -> line.startsWith(ADMIN_USERNAME + ",")).findFirst().orElseThrow(() -> new IllegalStateException("Admin user not found"));

        // Check if the password is correct
        String storedHash = adminLine.split(",")[1];
        String inputHash = hashPassword(password);
        return storedHash.equals(inputHash);
    }

    // Use the console to read a password and not show it on the screen
    private String getPasswordSecurely(String prompt) {
        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("Console not available");
        }
        
        // Use readPassword to read the password without showing it on the screen
        char[] passwordChars = console.readPassword(prompt);
        return passwordChars != null ? new String(passwordChars) : null;
    }

    // Hash the password using SHA-3
    private String hashPassword(String password) {
        SHA3.DigestSHA3 sha3 = new SHA3.Digest256();
        byte[] hashBytes = sha3.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    public void startGame() throws IOException {

        System.out.println("Starting game " + gameDirectory);

        // Validate game exists
        File gameDir = new File(gameDirectory);
        if (!gameDir.exists()) { 
            throw new IllegalStateException("Game does not exist!"); 
        }

        // Get admin password and validate
        String adminPassword = getPasswordSecurely("Enter admin password: ");
        if (!validateAdminPassword(adminPassword)) { 
            throw new IllegalStateException("Invalid admin password"); 
        }

        // Create a new deck and shuffle it
        Deck deck = new Deck();
        deck.shuffle();

        // Create draw.txt with all cards
        Path drawFile = Paths.get(gameDirectory, "draw.txt");
        Files.write(drawFile, deck.getCardStrings());

        System.out.println("Game started successfully! All cards are in draw.txt\n");
    }
} 