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
    @Override
    public void onFrame(long dt) {

    }

}
