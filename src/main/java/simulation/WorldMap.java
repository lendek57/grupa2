package simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class WorldMap extends AbstractWorldMap {
    private static final int ANIMALS_NO = 15, PLANTS_NO = 100;
    private ArrayList<Animal> animals = new ArrayList<>();
    private LinkedList<Plant> plants = new LinkedList<>();
    private Random random;

    public WorldMap(int width, int height) {
        super(width, height);
        random = new Random();
        for (int i = 0; i < ANIMALS_NO; i++) {
            animals.add(new Animal(getRandomPosition()));
        }
    }

    private Vector2D getRandomPosition() {
        return new Vector2D(random.nextInt(getWidth()), random.nextInt(getHeight()));
    }

    @Override
    public void run() {
        for (Animal animal : animals) {
            animal.move(MapDirection.values()[random.nextInt(MapDirection.values().length)]);
        }
    }
}
