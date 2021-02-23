package logic;

import entities.Entity;

import java.awt.event.ActionEvent;

public class EntityEvent extends ActionEvent {
    private final Actions action;
    private final Entity entity;

    public enum Actions {
        ENTITY_ADD,
        ENTITY_REMOVE,
    }
    public EntityEvent(Actions action, Entity entity) {
        super(EntityManager.instance(), action.ordinal(), "", System.currentTimeMillis(), 0);
        this.action = action;
        this.entity = entity;
    }

    public Actions getAction() {
        return action;
    }
    public Entity getEntity() { return entity; }
}