package simulation;

import java.util.*;

public class WorldMap extends AbstractWorldMap {
    private static final int ANIMALS_NO = 15, PLANTS_NO = 100;
    private Map<Vector2D, List<Animal>> animalsPositions = new HashMap<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private Map<Vector2D, Plant> plants = new HashMap<>();
    private Random random;

    public WorldMap(int width, int height) {
        super(width, height);
        random = new Random();
        for (int i = 0; i < ANIMALS_NO; i++) {
            Animal animal = new Animal(getRandomVector());
            animals.add(animal);
            placePlantOnMap();
        }
        for (int i = 0; i < PLANTS_NO; i++) {
            placePlantOnMap();
        }
    }

    private void placeAnimalOnMap(Animal animal) {
        List<Animal> animalsAtPosition = animalsPositions.get(animal.getPosition());
        if (animalsAtPosition == null) {
            animalsAtPosition = new LinkedList<>();
            animalsPositions.put(animal.getPosition(), animalsAtPosition);
        }
        animalsAtPosition.add(animal);
    }

    private void placePlantOnMap() {
        Vector2D position = getRandomVector();
        while (isOccupiedByPlant(position)) position = getRandomVector();
        plants.put(position, new Plant(position));
    }

    private boolean isOccupiedByPlant(Vector2D position) {
        return getPlantAtPosition(position) != null;
    }

    private Plant getPlantAtPosition(Vector2D position) {
        return plants.get(position);
    }

    private Vector2D getRandomVector() {
        return new Vector2D(random.nextInt(getWidth()), random.nextInt(getHeight()));
    }

    @Override
    public void run() {
        animalsPositions.clear();
        for (Animal animal: animals) {
            animal.move(MapDirection.values()[random.nextInt(MapDirection.values().length)]);
            placeAnimalOnMap(animal);
        }
    }

    public void eat() {
        for (Animal animal : animals) {
            if (isOccupiedByPlant(animal.getPosition())) {
                System.out.println("Animal ate plant at position " + animal.getPosition());
                plants.remove(animal.getPosition());
                placePlantOnMap();
            }
        }
    }
}