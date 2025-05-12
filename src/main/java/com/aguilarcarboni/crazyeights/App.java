package com.aguilarcarboni.crazyeights;
import org.apache.commons.cli.*;

/*
Hey, help me quickstart this project by creating a Java CLI my Crazy Eights proyect. Let's start with the basic creation of an admin user and initializing a game, so:

Once an admin is created for a game, it will be possible to call:
java CrazyEights --init --game <game>

If we were to call the program with --init new_game then it would:
- Validate that the game actually exists
- Request the admin password for the first time
- Create the users.txt file with the admin user and the hashed password, for instance:
admin,E9jj+T1lo/5olM+JF9FbqX2oX46b6+jClUD6TMldBkU=

In general, privileged operations like adding a user (or others referenced later) will require the admin password to be provided. Gameplay operations related to a single user will require that user's password be provided via command line. Note that it is possible to manage multiple games at once. Different directories with different game names should be created, but admin passwords and users would be different.

Lets now get this done!
*/

public class App {
    public static void main(String[] args) {

        // Use the options library to parse the command line arguments using flags
        Options options = new Options(); //
        options.addOption("i", "init", false, "Initialize a new game"); //
        options.addOption("g", "game", true, "Game name"); //
        options.addOption("a", "add-user", true, "Add a new user to the game"); //
        options.addOption("r", "remove-user", true, "Remove a user from the game"); //
        options.addOption("s", "start", false, "Start a new game and create the initial deck"); //
        options.addOption("o", "order", false, "Show the current turn order"); //
        options.addOption("u", "user", true, "Username for the current operation"); //
        options.addOption("p", "play", true, "Play a card (format: rank+suit, e.g. 4C)"); //
        options.addOption("c", "cards", true, "Show cards for the specified user"); //
        options.addOption("d", "draw", false, "Draw a card from the deck"); //
        options.addOption("t", "pass", false, "Pass the turn after drawing"); //

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args); //
            String gameName; //
            GameManager gameManager; //

            // Check if game name is provided for any operation
            if (cmd.hasOption("game")) { //
                gameName = cmd.getOptionValue("game"); //
                gameManager = new GameManager(gameName); //
            } else { //
                throw new ParseException("Game name is required (--game)\n"); //
            }

            // Handle different commands
            if (cmd.hasOption("init")) { //
                gameManager.initializeGame();
            } 
            else if (cmd.hasOption("add-user")) { //
                String username = cmd.getOptionValue("add-user");
                gameManager.addUser(username);
            }
            else if (cmd.hasOption("remove-user")) { //
                String username = cmd.getOptionValue("remove-user");
                gameManager.removeUser(username);
            }
            else if (cmd.hasOption("start")) { //
                gameManager.startGame();
            }
            else if (cmd.hasOption("order")) { //
                if (!cmd.hasOption("user")) { //
                    throw new ParseException("Username is required for viewing turn order (--user)"); //
                }
                gameManager.showPlayOrder(cmd.getOptionValue("user"));
            }
            else if (cmd.hasOption("play")) { //
                if (!cmd.hasOption("user")) { //
                    throw new ParseException("Username is required for playing a card (--user)"); //
                }
                gameManager.playCard(cmd.getOptionValue("user"), cmd.getOptionValue("play")); 
            }
            else if (cmd.hasOption("cards")) { //
                if (!cmd.hasOption("user")) { //
                    throw new ParseException("Username is required for viewing cards (--user)"); //
                }
                gameManager.showCards(cmd.getOptionValue("user"), cmd.getOptionValue("cards"));
            }
            else if (cmd.hasOption("draw")) { //
                if (!cmd.hasOption("user")) { //
                    throw new ParseException("Username is required for drawing a card (--user)"); //
                }
                gameManager.drawCard(cmd.getOptionValue("user"));
            }
            else if (cmd.hasOption("pass")) { //
                if (!cmd.hasOption("user")) { //
                    throw new ParseException("Username is required for passing turn (--user)"); //
                }
                gameManager.passTurn(cmd.getOptionValue("user"));
            }
            else { //
                formatter.printHelp("CrazyEights", options); //
            } //
        } catch (ParseException e) { //
            System.err.println("Error parsing command line arguments: " + e.getMessage() + "\n"); //
            formatter.printHelp("CrazyEights", options); //
            System.exit(1); //
        } catch (Exception e) { //
            System.err.println("Error: " + e.getMessage() + "\n"); //
            System.exit(1); //
        } //
    }
}
