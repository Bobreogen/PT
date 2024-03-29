package entities;

public class Car extends Vehicle {
    private static int carCreationCount = 0;

    public Car() {
        super("Car_" + ++carCreationCount);
    }

    public Car(String name) {
        super(name);
        carCreationCount++;
    }

    public VehicleType getVehicleType() { return VehicleType.CAR; }
}
