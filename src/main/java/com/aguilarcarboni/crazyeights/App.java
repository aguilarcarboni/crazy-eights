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
        options.addOption("o", "order", false, "Show the current turn order");
        options.addOption("u", "user", true, "Username for the current operation");
        options.addOption("p", "play", true, "Play a card (format: rank+suit, e.g. 4C)");
        options.addOption("c", "cards", true, "Show cards for the specified user");
        options.addOption("d", "draw", false, "Draw a card from the deck");
        options.addOption("t", "pass", false, "Pass the turn after drawing");

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
            else if (cmd.hasOption("order")) {
                if (!cmd.hasOption("user")) {
                    throw new ParseException("Username is required for viewing turn order (--user)");
                }
                gameManager.showPlayOrder(cmd.getOptionValue("user"));
            }
            else if (cmd.hasOption("play")) {
                if (!cmd.hasOption("user")) {
                    throw new ParseException("Username is required for playing a card (--user)");
                }
                gameManager.playCard(cmd.getOptionValue("user"), cmd.getOptionValue("play"));
            }
            else if (cmd.hasOption("cards")) {
                if (!cmd.hasOption("user")) {
                    throw new ParseException("Username is required for viewing cards (--user)");
                }
                gameManager.showCards(cmd.getOptionValue("user"), cmd.getOptionValue("cards"));
            }
            else if (cmd.hasOption("draw")) {
                if (!cmd.hasOption("user")) {
                    throw new ParseException("Username is required for drawing a card (--user)");
                }
                gameManager.drawCard(cmd.getOptionValue("user"));
            }
            else if (cmd.hasOption("pass")) {
                if (!cmd.hasOption("user")) {
                    throw new ParseException("Username is required for passing turn (--user)");
                }
                gameManager.passTurn(cmd.getOptionValue("user"));
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
