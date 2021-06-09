package logic;

import entities.*;

import java.awt.event.ActionListener;
import java.util.*;

import static logic.SimulationEvent.Actions.*;

public class Habitat {

    private final HashMap<VehicleType, VehicleProperty> vehiclePropertyMap = new HashMap<>();
    private final HashMap<VehicleType, EntityFactory> vehicleFactoryMap = new HashMap<>();
    private final ArrayList<Vehicle> vehicleList = new ArrayList<>();
    private final TreeSet<UUID> vehicleIDSet = new TreeSet<>();
    private final TreeMap<Long, Vehicle> vehicleCreatedTimeMap = new TreeMap<>();

    private int workspaceX = 0;
    private int workspaceY = 0;

    private long simulationTime = 0;
    private enum state {
        STOP,
        PAUSE,
        START
    }

    state currentState = state.STOP;

    private final List<ActionListener> listeners = new ArrayList<>();

    private Habitat() {
        EntityManager.instance().addListener(e -> {
            if (e.getClass() == EntityEvent.class) {
                switch (((EntityEvent) e).getAction()) {
                    case ENTITY_ADD -> entityAdd(((EntityEvent) e).getEntity());
                    case ENTITY_REMOVE -> entityRemove(((EntityEvent) e).getEntity());
                }
            }
        });
        vehiclePropertyMap.put(VehicleType.CAR,new VehicleProperty(VehicleType.CAR,1000,1,1000));
        vehiclePropertyMap.put(VehicleType.TRUCK,new VehicleProperty(VehicleType.TRUCK,1000,1,1000));
    }

    private void entityAdd(Entity entity) {
        Vehicle vehicle = entity instanceof Vehicle ? ((Vehicle) entity) : null;
        if (vehicle != null) {
            vehicle.setPositionX((int)(Math.random()*workspaceX));
            vehicle.setPositionY((int)(Math.random()*workspaceY));
            vehicle.setCreatedTime(simulationTime);

            vehicleList.add(vehicle);
            vehicleIDSet.add(vehicle.getId());

            long createdTime = vehicle.getCreatedTime();
            while(vehicleCreatedTimeMap.containsKey(createdTime))
            {
                createdTime++;
            }
            vehicleCreatedTimeMap.put(createdTime, vehicle);

            Main.printLog("Vehicle added: " + vehicle.getName());
            Main.printLog("Vehicle position: " + vehicle.getPositionX() + " " + vehicle.getPositionY());
        }
    }

    private void entityRemove(Entity entity) {
        Vehicle vehicle = entity instanceof Vehicle ? ((Vehicle) entity) : null;
        if (vehicle != null) {
            vehicleList.remove(vehicle);
            vehicleIDSet.remove(vehicle.getId());
            vehicleCreatedTimeMap.remove(vehicle.getCreatedTime(), vehicle);
        }
    }

    private enum Instances {
        INSTANCE(new Habitat());
        Habitat h;
        Instances(Habitat h) {
            this.h = h;
        }
    }

    public void setVehicleProperty(VehicleProperty vp) {
        vehiclePropertyMap.remove(vp.vehicleType);
        vehiclePropertyMap.put(vp.vehicleType, vp);
    }

    public void setWorkspacePosition(int x, int y) {
        workspaceX = x;
        workspaceY = y;
    }

    public int getWorkspacePositionX() { return workspaceX; }
    public int getWorkspacePositionY() { return workspaceY; }

    public void startSimulation(){
        if(currentState == state.START)
            return;

        currentState = state.START;

        for(var vehicleProperty : vehiclePropertyMap.entrySet()) {
            EntityFactory factory = new EntityFactory(vehicleProperty.getValue());
            vehicleFactoryMap.put(vehicleProperty.getKey(),factory);
        }

        SimulationEvent event = new SimulationEvent(SIMULATION_START);
        listeners.forEach(x -> x.actionPerformed(event));
        Main.printLog("Simulation start");
    }

    public void stopSimulation(){
        if(currentState == state.STOP)
            return;

        currentState = state.STOP;
        SimulationEvent event = new SimulationEvent(SIMULATION_STOP);
        listeners.forEach(x -> x.actionPerformed(event));
        simulationTime = 0;

        var it = vehicleList.iterator();
        while(it.hasNext()) {
            var vehicle = it.next();
            it.remove();
            EntityManager.instance().removeEntity(vehicle);
        }

        Main.printLog("Simulation stop");
    }

    public void pauseSimulation(){
        if(currentState == state.PAUSE)
            return;

        currentState = state.PAUSE;
        SimulationEvent event = new SimulationEvent(SIMULATION_PAUSE);
        listeners.forEach(x -> x.actionPerformed(event));
        Main.printLog("Simulation pause");
    }

    public long getSimulationTime(){
        return simulationTime;
    }

    public void onFrame(long dt){
        if (currentState == state.STOP || currentState == state.PAUSE)
            return;

        vehicleFactoryMap.forEach((vehicleType, entityFactory) -> entityFactory.onFrame(dt));

        simulationTime += dt;

        processLifeTime(dt);
    }

    private void processLifeTime(long dt) {
        var it = vehicleList.iterator();
        while(it.hasNext()) {
            var vehicle = it.next();
            vehicle.setLifeTime(vehicle.getLifeTime() - dt);
            if (vehicle.getLifeTime() <= 0) {
                it.remove();
                EntityManager.instance().removeEntity(vehicle);
            }
        }
    }

    public ArrayList<Vehicle> getVehicleList() { return vehicleList; }

    public boolean IsPauseSimulation() { return currentState == state.PAUSE;}
    public long getTimeGenerationByType(VehicleType type) { return vehiclePropertyMap.get(type).timeGeneration; }
    public void setTimeGenerationByType(VehicleType type, long timeGeneration) { vehiclePropertyMap.get(type).timeGeneration = timeGeneration; }
    public long getLifeTimeByType(VehicleType type) { return vehiclePropertyMap.get(type).lifeTime; }
    public void setLifeTimeByType(VehicleType type, long lifeTime) { vehiclePropertyMap.get(type).lifeTime = lifeTime; }
    public int getGenerateProbabilityByType(VehicleType type) { return vehiclePropertyMap.get(type).generateProbability; }
    public void setGenerateProbabilityByType(VehicleType type, int generateProbability) { vehiclePropertyMap.get(type).generateProbability = generateProbability; }

    public TreeMap<Long, Vehicle> getVehicleCreatedTimeMap() {
        return vehicleCreatedTimeMap;
    }

    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ActionListener listener) {
        listeners.remove(listener);
    }

    public static Habitat instance(){
        return Instances.INSTANCE.h;
    }
}

