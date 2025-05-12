package com.aguilarcarboni.crazyeights;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class GameState {
    private final List<String> players;
    private int currentPlayerIndex;
    private final String gameDirectory;
    private boolean gameOver;
    private String lastDrawnBy;
    private static final String STATE_FILE = "gamestate.txt";

    public GameState(String gameDirectory, List<String> players) {
        this.gameDirectory = gameDirectory;
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.lastDrawnBy = null;
    }

    public String getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<String> getPlayOrder() {
        List<String> order = new ArrayList<>();
        int index = currentPlayerIndex;
        for (int i = 0; i < players.size(); i++) {
            order.add(players.get(index));
            index = (index + 1) % players.size();
        }
        return order;
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        lastDrawnBy = null; // Reset the draw state for the new turn
    }

    public boolean isPlayerTurn(String username) {
        return getCurrentPlayer().equals(username);
    }

    public void setLastDrawnBy(String username) {
        lastDrawnBy = username;
    }

    public boolean hasDrawnThisTurn(String username) {
        return username.equals(lastDrawnBy);
    }

    public void setGameOver() {
        this.gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    // Save the current game state to a file
    public void save() throws IOException {
        Path statePath = Paths.get(gameDirectory, STATE_FILE);
        List<String> stateData = new ArrayList<>();
        stateData.add("currentPlayer=" + currentPlayerIndex);
        stateData.add("gameOver=" + gameOver);
        stateData.add("lastDrawnBy=" + (lastDrawnBy != null ? lastDrawnBy : "null"));
        stateData.add("players=" + String.join(",", players));
        Files.write(statePath, stateData);
    }

    // Load the game state from a file
    public static GameState load(String gameDirectory) throws IOException {
        Path statePath = Paths.get(gameDirectory, STATE_FILE);
        if (!Files.exists(statePath)) {
            throw new IllegalStateException("Game has not been started!");
        }

        List<String> stateData = Files.readAllLines(statePath);
        Map<String, String> stateMap = stateData.stream()
            .map(line -> line.split("=", 2))
            .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));

        List<String> players = Arrays.asList(stateMap.get("players").split(","));
        GameState state = new GameState(gameDirectory, players);
        state.currentPlayerIndex = Integer.parseInt(stateMap.get("currentPlayer"));
        state.gameOver = Boolean.parseBoolean(stateMap.get("gameOver"));
        state.lastDrawnBy = "null".equals(stateMap.get("lastDrawnBy")) ? null : stateMap.get("lastDrawnBy");

        return state;
    }
} 