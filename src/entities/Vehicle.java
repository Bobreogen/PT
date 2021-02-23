package entities;

public class Vehicle extends Entity{
    int x, y;

    public Vehicle(String name) {
        super(name);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getPositionX() { return x; }
    public int getPositionY() { return y; }

    @Override
    public void onFrame(long dt) {

    }
}
