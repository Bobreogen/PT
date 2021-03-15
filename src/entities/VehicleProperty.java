package entities;

public class VehicleProperty {
    public VehicleType vehicleType;
    public long timeGeneration;
    public int generateProbability;
    public long lifeTime;

    public VehicleProperty(VehicleType vehicleType, long timeGeneration, int generateProbability, long lifeTime) {
        this.vehicleType = vehicleType;
        this.timeGeneration = timeGeneration;
        this.generateProbability = generateProbability;
        this.lifeTime = lifeTime;
    }
}
