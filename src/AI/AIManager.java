package AI;
import entities.Car;
import entities.Entity;
import entities.Truck;
import entities.VehicleType;
import logic.ApplicationManager;
import logic.EntityEvent;
import logic.EntityManager;
import logic.Habitat;

import java.util.ArrayList;

public class AIManager {
    private final ArrayList<AIEntity> AICarList = new ArrayList<>();
    private final ArrayList<AIEntity> AITruckList = new ArrayList<>();
    private final Thread AICar;
    private final Thread AITruck;
    private boolean isTruckAISleep = false;
    private boolean isCarAISleep = false;

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
        AICar = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        while (true) {
                            AICarList.forEach(AIEntity -> AIEntity.getBehavior().onFrame(ApplicationManager.getFrameTime()));
                            this.wait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        AITruck = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        while (true) {
                            AITruckList.forEach(AIEntity -> AIEntity.getBehavior().onFrame(ApplicationManager.getFrameTime()));
                            this.wait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        AICar.start();
        AITruck.start();
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
        if(entity.getPositionX() <= Habitat.instance().getWorkspacePositionX()/2 && entity.getPositionY() <= Habitat.instance().getWorkspacePositionY()/2) {
            aiBehavior.setTargetX(entity.getPositionX());
            aiBehavior.setTargetY(entity.getPositionY());
        }
        else {
            aiBehavior.setTargetX((int) (Math.random() / 2 * Habitat.instance().getWorkspacePositionX()));
            aiBehavior.setTargetY((int) (Math.random() / 2 * Habitat.instance().getWorkspacePositionY()));
        }
        aiBehavior.setVelocity(100);
        aiEntity.setBehavior(aiBehavior);
        AICarList.add(aiEntity);
    }

    private void CreateAIEntityTruck(Entity entity) {
        var aiEntity = new AIEntity();
        var aiBehavior = new GoToPositionStrategy();
        aiBehavior.setOwner(entity);
        if(entity.getPositionX() >= Habitat.instance().getWorkspacePositionX()/2 && entity.getPositionY() >= Habitat.instance().getWorkspacePositionY()/2) {
            aiBehavior.setTargetX(entity.getPositionX());
            aiBehavior.setTargetY(entity.getPositionY());
        }
        else {
            aiBehavior.setTargetX((int) ((0.5 + Math.random() / 2) * Habitat.instance().getWorkspacePositionX()));
            aiBehavior.setTargetY((int) ((0.5 + Math.random() / 2) * Habitat.instance().getWorkspacePositionY()));
        }
        aiBehavior.setVelocity(150);
        aiEntity.setBehavior(aiBehavior);
        AITruckList.add(aiEntity);
    }

    public void StartAIForVehicleType(VehicleType vt) {
        switch (vt) {
            case CAR -> isCarAISleep = false;
            case TRUCK -> isTruckAISleep = false;

        }
    }

    public void StopAIForVehicleType(VehicleType vt) {
        switch (vt) {
            case CAR -> isCarAISleep = true;
            case TRUCK -> isTruckAISleep = true;

        }
    }

    public int GetAIPriorityForVehicleType(VehicleType vt) {
        int priority = 0;
        switch (vt) {
            case CAR -> priority = AICar.getPriority();
            case TRUCK -> priority = AITruck.getPriority();
        }
        return priority;
    }

    public void SetAIPriorityForVehicleType(VehicleType vt, int priority) {
        switch (vt) {
            case CAR -> AICar.setPriority(priority);
            case TRUCK -> AITruck.setPriority(priority);
        }
    }

    private void entityRemove(Entity entity)
    {
        AICarList.removeIf(AIEntity -> AIEntity.getEntity() == entity);
        AITruckList.removeIf(AIEntity -> AIEntity.getEntity() == entity);
    }

    public synchronized void onFrame(long dt) {
        if(!isCarAISleep) synchronized(AICar) { AICar.notify(); }
        if(!isTruckAISleep) synchronized(AITruck) { AITruck.notify(); }
    }

    public static AIManager instance() { return AIManager.Instances.INSTANCE.aIManager; }

}
