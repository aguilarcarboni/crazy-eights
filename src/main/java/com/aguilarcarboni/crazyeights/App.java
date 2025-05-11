package com.aguilarcarboni.crazyeights;
import org.apache.commons.cli.*;

public class App {
    public static void main(String[] args) {

        // Use the options library to parse the command line arguments using flags
        Options options = new Options();
        options.addOption("i", "init", false, "Initialize a new game");
        options.addOption("g", "game", true, "Game name");
        options.addOption("a", "add-user", true, "Add a new user to the game");
        options.addOption("r", "remove-user", true, "Remove a user from the game");
        options.addOption("s", "start", false, "Start a new game and create the initial deck");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            String gameName;
            GameManager gameManager;

            // Check if game name is provided for any operation
            if (cmd.hasOption("game")) {
                gameName = cmd.getOptionValue("game");
                gameManager = new GameManager(gameName);
            } else {
                throw new ParseException("Game name is required (--game)\n");
            }

            // Handle different commands
            if (cmd.hasOption("init")) {
                gameManager.initializeGame();
            } 
            else if (cmd.hasOption("add-user")) {
                String username = cmd.getOptionValue("add-user");
                gameManager.addUser(username);
            }
            else if (cmd.hasOption("remove-user")) {
                String username = cmd.getOptionValue("remove-user");
                gameManager.removeUser(username);
            }
            else if (cmd.hasOption("start")) {
                gameManager.startGame();
            }
            else {
                formatter.printHelp("CrazyEights", options);
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage() + "\n");
            formatter.printHelp("CrazyEights", options);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n");
            System.exit(1);
        }
    }
}
