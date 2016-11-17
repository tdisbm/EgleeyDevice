import kraken.Kraken;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Kraken kraken = new Kraken();

        kraken
            .sink(new File("src/main/resources/config/parameters.yml"))
            .sink(new File("src/main/resources/config/sensors.yml"))
            .sink(new File("src/main/resources/config/tasks.yml"))
            .sink(new File("src/main/resources/config/services.yml"))
        .dive();
    }
}
