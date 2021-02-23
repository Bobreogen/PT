package logic;

import entities.Entity;
import entities.VehicleProperty;
import entities.VehicleType;

public class EntityFactory {
    VehicleProperty vp;
    long lastGenerateTime;

    public EntityFactory(VehicleProperty vp) {
        this.vp = vp;
    }

    public Entity createEntity(VehicleType vt) {

        return null;
    }
}
