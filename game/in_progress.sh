cd ..
mvn clean package

# Initialize the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --init --game in_progress

# Add a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Marco --game in_progress

# Try to add a user that already exists
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Marco --game in_progress

# Add another user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Esteban --game in_progress

# Add a third user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Juan --game in_progress

# Remove a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user Marco --game in_progress

# Try to remove a user that doesn't exist
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user CrazyEights --game in_progress

# Try to remove the admin user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user admin --game in_progress

# Start the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --start --game in_progress

# Simulate a game
echo "\n=== Starting Game Simulation ===\n"

# Check the turn order
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --order --user Esteban --game in_progress

# Admin checks everyone's cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Esteban --user admin --game in_progress
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Juan --user admin --game in_progress

# Esteban checks his cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Esteban --user Esteban --game in_progress

# Juan tries to play out of turn (should fail)
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --play 4H --user Juan --game in_progress

# Esteban tries to play a card
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --play 2D --user Esteban --game in_progress

# Esteban draws a card
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --draw --user Esteban --game in_progress

# Esteban tries to draw again
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --draw --user Esteban --game in_progress

# Esteban passes his turn
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --pass --user Esteban --game in_progress

# Juan checks his cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Juan --user Juan --game in_progress