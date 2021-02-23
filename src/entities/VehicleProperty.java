package entities;

public class VehicleProperty {
    public VehicleType vehicleType;
    public long timeGeneration;
    public int generateProbability;

    public VehicleProperty(VehicleType vehicleType, long timeGeneration, int generateProbability) {
        this.vehicleType = vehicleType;
        this.timeGeneration = timeGeneration;
        this.generateProbability = generateProbability;
    }
}
