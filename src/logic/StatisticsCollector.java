package logic;

import entities.*;

import java.util.HashMap;

public class StatisticsCollector {

    private final HashMap<VehicleType, VehicleStatistic> vehicleStatisticMap = new HashMap<>();

    private enum Instances {
        INSTANCE(new StatisticsCollector());
        StatisticsCollector statisticsCollector;

        Instances(StatisticsCollector statisticsCollector) {
            this.statisticsCollector = statisticsCollector;
        }
    }

    public StatisticsCollector() {
        EntityManager.instance().addListener(e -> {
            if (e.getClass() == EntityEvent.class) {
                switch (((EntityEvent) e).getAction()) {
                    case ENTITY_ADD -> entityAdd(((EntityEvent) e).getEntity());
                    case ENTITY_REMOVE -> entityRemove(((EntityEvent) e).getEntity());
                }
            }
        });

        for(VehicleType vt : VehicleType.values()) {
            vehicleStatisticMap.put(vt, new VehicleStatistic());
        }
    }

    public String getStatistics() {
        return "Car creation count: " + vehicleStatisticMap.get(VehicleType.CAR).creationCount
                + "\nTruck creation count: " + vehicleStatisticMap.get(VehicleType.TRUCK).creationCount
                + "\nTime of simulation: " + Habitat.instance().getSimulationTime()/1000 + " sec.";
    }

    private void entityAdd(Entity entity) {
        if(entity instanceof Car) {
            vehicleStatisticMap.get(VehicleType.CAR).creationCount++;
        } else if(entity instanceof Truck) {
            vehicleStatisticMap.get(VehicleType.TRUCK).creationCount++;
        }
    }

    private void entityRemove(Entity entity) {

    }

    public void reset() {
        for(VehicleType vt : VehicleType.values()) {
            vehicleStatisticMap.put(vt, new VehicleStatistic());
        }
    }

    public static StatisticsCollector instance() { return Instances.INSTANCE.statisticsCollector; }

    class VehicleStatistic {
        public int creationCount = 0;
    }
}
