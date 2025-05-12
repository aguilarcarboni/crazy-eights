cd ..
mvn clean package

# Initialize the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --init --game initial_setup

# Add a user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Marco --game initial_setup

# Add another user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Esteban --game initial_setup

# Add a third user
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --add-user Juan --game initial_setup

# Start the game
java -cp target/crazy-eights-1.0-SNAPSHOT-jar-with-dependencies.jar com.aguilarcarboni.crazyeights.App --start --game initial_setup