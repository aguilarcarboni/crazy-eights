cd ..
mvn clean package

# Initialize the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --init --game initial_setup

# Add a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Marco --game initial_setup

# Try to add a user that already exists
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Marco --game initial_setup

# Add another user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Esteban --game initial_setup

# Add a third user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Juan --game initial_setup

# Remove a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user Marco --game initial_setup

# Try to remove a user that doesn't exist
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user CrazyEights --game initial_setup

# Try to remove the admin user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --remove-user admin --game initial_setup

# Start the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --start --game initial_setup