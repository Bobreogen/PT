package logic;

import entities.*;

public class EntityFactory {
    VehicleProperty vp;
    long lastGenerateTime;
    long currentTime;

    public EntityFactory(VehicleProperty vp) {
        this.vp = vp;
    }

    public Entity createEntity() {
        Entity entity = null;

        switch (vp.vehicleType) {
            case CAR -> entity = new Car();
            case TRUCK -> entity = new Truck();
        }

        return entity;
    }

    public void onFrame(long dt) {
        currentTime += dt;

        if(currentTime - lastGenerateTime > vp.timeGeneration) {
            lastGenerateTime = currentTime;

            int probability = (int)(Math.random() * 100);
            if(probability <= vp.generateProbability) {
                Entity entity = createEntity();
                if(entity != null) {
                    EntityManager.instance().addEntity(entity);
                }
            }
        }
    }


}
