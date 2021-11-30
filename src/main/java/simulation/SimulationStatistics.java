package simulation;

public record SimulationStatistics(
        int dayNumber,
        int animalsNo,
        int plantsNo,
        double meanEnergy,
        double meanLifeLength,
        double meanChildrenNumber
) {
}
