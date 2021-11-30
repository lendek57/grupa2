package simulation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorldMap extends AbstractWorldMap {
    private static final String  STATS_FILENAME = "stats.json";

    private final int animalEnergy;
    private final int plantEnergy;
    private final int noOfPlants;
    private int dayNumber = 1;

    private List<Animal> animals = new ArrayList<>();
    private final Map<Vector2D, List<Animal>> animalsPositions = new HashMap<>();
    private final Map<Vector2D, Plant> plants = new HashMap<>();
    private final Random random = new Random();

    public WorldMap(int width, int height, int noOfAnimals, int noOfPlants, int animalEnergy, int plantEnergy) {
        super(width, height);
        this.animalEnergy = animalEnergy;
        this.plantEnergy = plantEnergy;
        this.noOfPlants = noOfPlants;
        for (int i = 0; i < noOfAnimals; i++) {
            addNewAnimal(new Animal(getRandomVector(), noOfAnimals));
        }
        for (int i = 0; i < noOfPlants; i++) {
            placePlantOnMap();
        }
    }

    private void placeAnimalOnMap(Animal animal) {
        animalsPositions.computeIfAbsent(animal.getPosition(), pos -> new LinkedList<>()).add(animal);
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
        System.out.println("Today is day number " + dayNumber);
        animalsPositions.clear();
        animals.forEach(animal -> {
            animal.moveBasedOnGenome();
            placeAnimalOnMap(animal);
        });
    }

    public void eat() {
        animalsPositions.forEach((position, animals) -> {
            if (isOccupiedByPlant(position)) {
                animals.stream().max(Animal::compareTo).ifPresent(this::eatPlant);
            }
        });
        IntStream.range(1, new Random().nextInt(noOfPlants / 10) + 1).forEach(i -> placePlantOnMap());
    }

    private void eatPlant(Animal animal) {
        System.out.println("Animal ate plant at position " + animal.getPosition());
        animal.setEnergy(animal.getEnergy() + plantEnergy);
        plants.remove(animal.getPosition());
    }

    @Override
    public void atTheEndOfDay() {
        dayNumber++;
        animals = animals.stream()
                .map(Animal::aging)
                .map(animal -> animal.setEnergy(animal.getEnergy() - animalEnergy / 4))
                .filter(animal -> animal.getEnergy() > 0)
                .collect(Collectors.toList());
        createStatistics();
    }

    @Override
    public void reproduce() {
        List<Animal> children = new LinkedList<>();
        animalsPositions.forEach((position, animals) -> {
            List<Animal> parents = animals.stream()
                    .filter(a -> a.getEnergy() > animalEnergy / 2)
                    .sorted(Collections.reverseOrder())
                    .limit(2)
                    .collect(Collectors.toList());
            if (parents.size() == 2) {
                Animal child = new Animal(parents.get(0), parents.get(1));
                System.out.println("Animal " + child.getAnimalId() + " was born on position " + position);
                children.add(child);
            }
        });
        children.forEach(this::addNewAnimal);
    }

    private void addNewAnimal(Animal animal) {
        animals.add(animal);
        placeAnimalOnMap(animal);
    }

    private void createStatistics() {
        SimulationStatistics statistics = new SimulationStatistics(
                dayNumber,
                animals.size(),
                plants.size(),
                animals.stream().mapToInt(Animal::getEnergy).average().orElse(0),
                animals.stream().mapToInt(Animal::getAge).average().orElse(0),
                animals.stream().mapToInt(Animal::getNumberOfChildren).average().orElse(0)
        );
        System.out.println(statistics);
        JsonParser.dumpStatisticsToJsonFile(STATS_FILENAME, statistics);
    }
}