package simulation;

public class Simulation {
    private static final int WIDTH = 30, HEIGHT = 50;
    private static final WorldMap worldMap = new WorldMap(WIDTH, HEIGHT);

    public static WorldMap getWorldMap() {
        return worldMap;
    }

    public static void simulateDay() {
        worldMap.run();
    }
}
