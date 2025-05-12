cd ..
mvn clean package

# Initialize the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --init --game edge_cases

# Initialize the game again
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --init --game edge_cases

# Add a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Marco --game edge_cases

# Try to add a user that already exists
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Marco --game edge_cases

# Add another user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Esteban --game edge_cases

# Add a third user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Juan --game edge_cases

# Remove a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user Marco --game edge_cases

# Try to remove a user that doesn't exist
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user CrazyEights --game edge_cases

# Try to remove the admin user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user admin --game edge_cases

# Start the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --start --game edge_cases

# Simulate a game
echo "\n=== Starting Game Simulation ===\n"

# Check the turn order
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --order --user Esteban --game edge_cases

# Juan tries to check Esteban's cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Esteban --user Juan --game edge_cases

# Juan tries to play out of turn (should fail)
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --play 4H --user Juan --game edge_cases

# Esteban tries to play a card which is not in his hand
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --play 2D --user Esteban --game edge_cases

# Esteban tries to play a card which cannot be played
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --play 2H --user Esteban --game edge_cases

# Esteban draws a card twice
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --draw --user Esteban --game edge_cases
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --draw --user Esteban --game edge_cases