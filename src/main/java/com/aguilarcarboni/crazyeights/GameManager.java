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
import java.util.Map;
import java.util.HashMap;

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

        // Get list of users (excluding admin)
        Path usersFile = Paths.get(gameDirectory, "users.txt");
        List<String> userLines = Files.readAllLines(usersFile);
        List<String> usernames = userLines.stream().map(line -> line.split(",")[0]).filter(username -> !username.equals(ADMIN_USERNAME)).collect(Collectors.toList());

        if (usernames.isEmpty()) { throw new IllegalStateException("No users found in the game!"); }
        if (usernames.size() < 2) { throw new IllegalStateException("At least 2 users are required to start the game!"); }

        // Create a new deck and shuffle it
        Deck deck = new Deck();
        deck.shuffle();

        // Deal 5 cards to each user
        for (String username : usernames) {

            // Deal 5 cards to the user
            List<Card> userCards = deck.dealCards(5);
            Path userCardFile = Paths.get(gameDirectory, username + ".txt");
            List<String> cardStrings = userCards.stream().map(Card::toString).collect(Collectors.toList());

            // Write the cards to the user's card file
            Files.write(userCardFile, cardStrings);
            System.out.println("Dealt 5 cards to user: " + username);
        }

        // Deal one card to discard pile
        Card discardCard = deck.dealCard();
        Path discardFile = Paths.get(gameDirectory, "discard.txt");
        Files.write(discardFile, List.of(discardCard.toString()));
        System.out.println("Created discard pile with card: " + discardCard);

        // Put remaining cards in draw pile
        Path drawFile = Paths.get(gameDirectory, "draw.txt");
        Files.write(drawFile, deck.getCardStrings());
        System.out.println("Created draw pile with " + deck.remainingCards() + " cards");

        // Initialize and save game state
        GameState gameState = new GameState(gameDirectory, usernames);
        gameState.save();

        System.out.println("Game started successfully!\n");
    }

    public void showPlayOrder(String username) throws IOException {
        // Validate user's password
        String password = getPasswordSecurely("Enter password for " + username + ": ");
        validateUserPassword(username, password);

        // Load game state and show order
        GameState gameState = GameState.load(gameDirectory);
        if (gameState.isGameOver()) {
            throw new IllegalStateException("Game is over!");
        }

        List<String> order = gameState.getPlayOrder();
        System.out.println("\nCurrent turn order (starting with current player):");
        for (int i = 0; i < order.size(); i++) {
            System.out.println((i + 1) + ". " + order.get(i) + (i == 0 ? " (current turn)" : ""));
        }
        System.out.println();
    }

    public void showCards(String viewerUsername, String targetUsername) throws IOException {
        // Validate viewer's password
        String password = getPasswordSecurely("Enter password for " + viewerUsername + ": ");
        
        // Check if viewer is admin or the same user
        if (!viewerUsername.equals(ADMIN_USERNAME)) {
            validateUserPassword(viewerUsername, password);
            if (!viewerUsername.equals(targetUsername)) {
                throw new IllegalStateException("You can only view your own cards!");
            }
        } else {
            validateAdminPassword(password);
        }

        // Show target user's cards
        Path userCardFile = Paths.get(gameDirectory, targetUsername + ".txt");
        Path discardFile = Paths.get(gameDirectory, "discard.txt");

        if (!Files.exists(userCardFile)) {
            throw new IllegalStateException("User " + targetUsername + " has no cards!");
        }

        List<String> cards = Files.readAllLines(userCardFile);
        String topDiscard = Files.readAllLines(discardFile).get(0);

        System.out.println("\nCards for " + targetUsername + ":");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i));
        }
        System.out.println("\nTop card on discard pile: " + topDiscard + "\n");
    }

    public void playCard(String username, String card) throws IOException {
        // Validate user's password
        String password = getPasswordSecurely("Enter password for " + username + ": ");
        validateUserPassword(username, password);

        // Load game state
        GameState gameState = GameState.load(gameDirectory);
        if (gameState.isGameOver()) {
            throw new IllegalStateException("Game is over!");
        }

        // Check if it's the player's turn
        if (!gameState.isPlayerTurn(username)) {
            throw new IllegalStateException("It's not your turn! Current player: " + gameState.getCurrentPlayer());
        }

        // Check if the card exists in the player's hand
        Path userCardFile = Paths.get(gameDirectory, username + ".txt");
        List<String> cards = Files.readAllLines(userCardFile);
        if (!cards.contains(card)) {
            throw new IllegalStateException("You don't have the card " + card + "!");
        }

        // Check if the card can be played
        Path discardFile = Paths.get(gameDirectory, "discard.txt");
        String topCard = Files.readAllLines(discardFile).get(0);
        
        // Check if card matches top card (same rank, same suit, or eight)
        if (!canPlayCard(card, topCard)) {
            throw new IllegalStateException("Cannot play " + card + " on " + topCard + "!");
        }

        // Remove card from player's hand and add to discard pile
        cards.remove(card);
        Files.write(userCardFile, cards);
        Files.write(discardFile, List.of(card));

        // Check if player has won
        if (cards.isEmpty()) {
            gameState.setGameOver();
            System.out.println("\nGame Over! " + username + " wins!\n");
        } else {
            // Move to next player
            gameState.nextTurn();
            System.out.println("\nPlayed " + card + ". Next player: " + gameState.getCurrentPlayer() + "\n");
        }

        gameState.save();
    }

    public void drawCard(String username) throws IOException {
        // Validate user's password
        String password = getPasswordSecurely("Enter password for " + username + ": ");
        validateUserPassword(username, password);

        // Load game state
        GameState gameState = GameState.load(gameDirectory);
        if (gameState.isGameOver()) {
            throw new IllegalStateException("Game is over!");
        }

        // Check if it's the player's turn
        if (!gameState.isPlayerTurn(username)) {
            throw new IllegalStateException("It's not your turn! Current player: " + gameState.getCurrentPlayer());
        }

        // Check if player has already drawn this turn
        if (gameState.hasDrawnThisTurn(username)) {
            throw new IllegalStateException("You have already drawn a card this turn!");
        }

        // Draw a card from the draw pile
        Path drawFile = Paths.get(gameDirectory, "draw.txt");
        Path userCardFile = Paths.get(gameDirectory, username + ".txt");
        Path discardFile = Paths.get(gameDirectory, "discard.txt");

        List<String> drawPile = Files.readAllLines(drawFile);
        if (drawPile.isEmpty()) {
            // Draw pile is empty, determine winner by points
            String winner = determineWinnerByPoints();
            gameState.setGameOver();
            gameState.save();
            
            // Show final point totals and declare winner
            System.out.println("\nDraw pile is empty! Game ends with points:");
            showAllPlayerPoints();
            System.out.println("\nGame Over! " + winner + " wins with the least points!\n");
            return;
        }

        // Add card to player's hand
        String drawnCard = drawPile.remove(0);
        List<String> playerCards = Files.readAllLines(userCardFile);
        playerCards.add(drawnCard);

        // Update files
        Files.write(drawFile, drawPile);
        Files.write(userCardFile, playerCards);

        // Update game state
        gameState.setLastDrawnBy(username);
        gameState.save();

        // Show drawn card and current hand
        System.out.println("\nDrew card: " + drawnCard);
        System.out.println("Your cards:");
        for (int i = 0; i < playerCards.size(); i++) {
            System.out.println((i + 1) + ". " + playerCards.get(i));
        }
        
        // Show top of discard pile
        String topDiscard = Files.readAllLines(discardFile).get(0);
        System.out.println("\nTop card on discard pile: " + topDiscard + "\n");
    }

    private String determineWinnerByPoints() throws IOException {
        Map<String, Integer> playerPoints = new HashMap<>();
        int minPoints = Integer.MAX_VALUE;
        String winner = null;

        // Get list of players (excluding admin)
        Path usersFile = Paths.get(gameDirectory, "users.txt");
        List<String> userLines = Files.readAllLines(usersFile);
        List<String> players = userLines.stream()
            .map(line -> line.split(",")[0])
            .filter(username -> !username.equals(ADMIN_USERNAME))
            .collect(Collectors.toList());

        // Calculate points for each player
        for (String player : players) {
            int points = calculatePlayerPoints(player);
            playerPoints.put(player, points);
            
            // Update winner if this player has fewer points
            if (points < minPoints) {
                minPoints = points;
                winner = player;
            }
        }

        return winner;
    }

    private int calculatePlayerPoints(String username) throws IOException {
        Path userCardFile = Paths.get(gameDirectory, username + ".txt");
        if (!Files.exists(userCardFile)) {
            return 0; // Player has no cards
        }

        List<String> cardStrings = Files.readAllLines(userCardFile);
        int totalPoints = 0;

        for (String cardStr : cardStrings) {
            Card card = Card.fromString(cardStr);
            totalPoints += card.getPoints();
        }

        return totalPoints;
    }

    private void showAllPlayerPoints() throws IOException {
        // Get list of players (excluding admin)
        Path usersFile = Paths.get(gameDirectory, "users.txt");
        List<String> userLines = Files.readAllLines(usersFile);
        List<String> players = userLines.stream()
            .map(line -> line.split(",")[0])
            .filter(username -> !username.equals(ADMIN_USERNAME))
            .collect(Collectors.toList());

        // Show points for each player
        for (String player : players) {
            int points = calculatePlayerPoints(player);
            System.out.println(player + ": " + points + " points");
        }
    }

    public void passTurn(String username) throws IOException {
        // Validate user's password
        String password = getPasswordSecurely("Enter password for " + username + ": ");
        validateUserPassword(username, password);

        // Load game state
        GameState gameState = GameState.load(gameDirectory);
        if (gameState.isGameOver()) {
            throw new IllegalStateException("Game is over!");
        }

        // Check if it's the player's turn
        if (!gameState.isPlayerTurn(username)) {
            throw new IllegalStateException("It's not your turn! Current player: " + gameState.getCurrentPlayer());
        }

        // Check if player has drawn this turn
        if (!gameState.hasDrawnThisTurn(username)) {
            throw new IllegalStateException("You must draw a card before passing!");
        }

        // Move to next player
        gameState.nextTurn();
        gameState.save();

        System.out.println("\nTurn passed. Next player: " + gameState.getCurrentPlayer() + "\n");
    }

    private boolean canPlayCard(String playedCard, String topCard) {
        // Eight can be played on anything
        if (playedCard.startsWith("8")) {
            return true;
        }

        // Extract ranks and suits
        String playedRank = playedCard.substring(0, playedCard.length() - 1);
        String topRank = topCard.substring(0, topCard.length() - 1);
        char playedSuit = playedCard.charAt(playedCard.length() - 1);
        char topSuit = topCard.charAt(topCard.length() - 1);

        // Match rank or suit
        return playedRank.equals(topRank) || // Same rank
               playedSuit == topSuit;        // Same suit
    }

    private void validateUserPassword(String username, String password) throws IOException {
        Path usersFile = Paths.get(gameDirectory, "users.txt");
        List<String> lines = Files.readAllLines(usersFile);
        
        String userLine = lines.stream()
            .filter(line -> line.startsWith(username + ","))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("User not found"));

        String storedHash = userLine.split(",")[1];
        String inputHash = hashPassword(password);
        
        if (!storedHash.equals(inputHash)) {
            throw new IllegalStateException("Invalid password");
        }
    }
}