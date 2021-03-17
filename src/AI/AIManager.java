package AI;
import entities.Car;
import entities.Entity;
import entities.Truck;
import logic.EntityEvent;
import logic.EntityManager;

import java.util.ArrayList;

public class AIManager {
    private final ArrayList<AIEntity> AIEntityList = new ArrayList<>();

    private enum Instances {
        INSTANCE(new AIManager());
        AIManager aIManager;

        Instances(AIManager aIManager) {
            this.aIManager = aIManager;
        }
    }

    private AIManager() {
        EntityManager.instance().addListener(e -> {
            if (e.getClass() == EntityEvent.class) {
                switch (((EntityEvent) e).getAction()) {
                    case ENTITY_ADD -> entityAdd(((EntityEvent) e).getEntity());
                    case ENTITY_REMOVE -> entityRemove(((EntityEvent) e).getEntity());
                }
            }
        });
    }

    private void entityAdd(Entity entity) {
        if(entity instanceof Car)
            CreateAIEntityCar(entity);
        if(entity instanceof Truck)
            CreateAIEntityTruck(entity);
    }

    private void CreateAIEntityCar(Entity entity) {
        var aiEntity = new AIEntity();
        var aiBehavior = new GoToPositionStrategy();
        aiBehavior.setOwner(entity);
        aiBehavior.setTargetX(0);
        aiBehavior.setTargetY(0);
        aiBehavior.setVelocity(100);
        aiEntity.setBehavior(aiBehavior);
        AIEntityList.add(aiEntity);
    }

    private void CreateAIEntityTruck(Entity entity) {
        var aiEntity = new AIEntity();
        var aiBehavior = new GoToPositionStrategy();
        aiBehavior.setOwner(entity);
        aiBehavior.setTargetX(550);
        aiBehavior.setTargetY(550);
        aiBehavior.setVelocity(1000);
        aiEntity.setBehavior(aiBehavior);
        AIEntityList.add(aiEntity);
    }

    private void entityRemove(Entity entity) {
        AIEntityList.removeIf(AIEntity -> AIEntity.getEntity() == entity);
    }

    public void onFrame(long dt) {
        AIEntityList.forEach(AIEntity -> AIEntity.getBehavior().onFrame(dt));
    }

    public static AIManager instance() { return AIManager.Instances.INSTANCE.aIManager; }

}
