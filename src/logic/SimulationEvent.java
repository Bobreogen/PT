package logic;

import java.awt.event.ActionEvent;

public class SimulationEvent extends ActionEvent {
    private final Actions action;

    public enum Actions {
        SIMULATION_START,
        SIMULATION_PAUSE,
        SIMULATION_STOP,
    }
    public SimulationEvent(Actions action) {
        super(Habitat.instance(), action.ordinal(), "", System.currentTimeMillis(), 0);
        this.action = action;
    }

    public Actions getAction() {
        return action;
    }
}
