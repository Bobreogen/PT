package logic;

import AI.AIManager;
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
    }

    private void entityAdd(Entity entity) {
        Vehicle vehicle = entity instanceof Vehicle ? ((Vehicle) entity) : null;
        if (vehicle != null) {
            vehicle.setPosition((int)(Math.random()*workspaceX), (int)(Math.random()*workspaceY));
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

        AIManager.instance().onFrame(dt);

        simulationTime += dt;

        checkLifeTime();
    }

    private void checkLifeTime() {
        var it = vehicleList.iterator();
        while(it.hasNext()) {
            var vehicle = it.next();
            if (vehicle.getCreatedTime() + vehicle.getLifeTime() < simulationTime) {
                it.remove();
                EntityManager.instance().removeEntity(vehicle);
            }
        }
    }

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

