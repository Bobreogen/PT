package entities;

import java.io.Serializable;

public abstract class Vehicle extends Entity implements Serializable {
    private long createdTime = 0;
    private long lifeTime = 5000;

    public Vehicle() {
        super();
    }

    public Vehicle(String name, long createdTime, long lifeTime) {
        super(name);
        this.createdTime = createdTime;
        this.lifeTime = lifeTime;
    }

    public Vehicle(String name) {
        super(name);
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public VehicleType getVehicleType() { return VehicleType.NONE; }
}
