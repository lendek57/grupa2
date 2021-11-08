package simulation;

import java.util.Random;

public class WorldMap extends AbstractWorldMap {
    private Animal animal;
    private Random random;

    public WorldMap(int width, int height) {
        super(width, height);
        animal = new Animal(new Vector2D(0, 0));
        random = new Random();
    }

    @Override
    public void run() {
        animal.move(MapDirection.values()[random.nextInt(MapDirection.values().length)]);
    }
}
