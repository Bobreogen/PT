package entities;

public class Truck extends Vehicle {
    private static int truckCreationCount = 0;

    public Truck() {
        super("Truck_" + ++truckCreationCount);
    }

    public Truck(String name) {
        super(name);
        truckCreationCount++;
    }
}
