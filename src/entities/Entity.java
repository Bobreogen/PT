package entities;

import java.util.UUID;

public class Entity {
    private String name;
    private final UUID id;
    private int x, y;
    private int sizeX;
    private int sizeY;

    public UUID getId() {
        return id;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }


    public String getName() {return name; }


    public void setPositionX(int x) { this.x = x; }
    public void setPositionY(int y) { this.y = y; }

    public int getPositionX() { return x; }
    public int getPositionY() { return y; }

    public void setName(String name) {
        this.name = name;
    }

    Entity(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }
}
