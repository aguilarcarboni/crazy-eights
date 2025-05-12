cd ..
mvn clean package

# Initialize the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --init --game in_progress

# Add a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Robert --game in_progress

# Add another user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user George --game in_progress

# Add a third user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Frank --game in_progress

# Add a fourth user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Josh --game in_progress

# Start the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --start --game in_progress

# Simulate a game
echo "\n=== Starting Game Simulation ===\n"

# Check the turn order
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --order --user Josh --game in_progress

# Admin checks everyone's cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Josh --user admin --game in_progress
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Frank --user admin --game in_progress
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards George --user admin --game in_progress
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Robert --user admin --game in_progress

# Robert checks his cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Robert --user Robert --game in_progress

# George tries to play out of turn (should fail)
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --play 4H --user George --game in_progress

# Robert draws a card
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --draw --user Robert --game in_progress

# Robert passes his turn
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --pass --user Robert --game in_progress

# George checks his cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards George --user George --game in_progress

# George passes his turn without drawing a card
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --pass --user George --game in_progress

# George then draws a card
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --draw --user George --game in_progress

# George passes his turn
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --pass --user George --game in_progress

# Frank checks his cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Frank --user Frank --game in_progress

# Frank plays 6H
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --play 6H --user Frank --game in_progress

# Josh checks his cards
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --cards Josh --user Josh --game in_progress
