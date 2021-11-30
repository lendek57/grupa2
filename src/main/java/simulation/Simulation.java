package simulation;

public class Simulation {
    private static final WorldMap worldMap;

    static {
        worldMap = new WorldMap(
                SimulationParams.getField("width"),
                SimulationParams.getField("height"),
                SimulationParams.getField("noOfAnimals"),
                SimulationParams.getField("noOfPlants"),
                SimulationParams.getField("animalEnergy"),
                SimulationParams.getField("plantEnergy")
        );
    }

    public static WorldMap getWorldMap() {
        return worldMap;
    }

    public static void simulateDay() {
        worldMap.run();
        worldMap.eat();
        worldMap.reproduce();
        worldMap.atTheEndOfDay();
    }
}
