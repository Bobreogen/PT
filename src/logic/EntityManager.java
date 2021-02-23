package logic;

import entities.Entity;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityManager {
    private final HashMap<String, Entity> entityList = new HashMap<>();

    private final List<ActionListener> listeners = new ArrayList<>();

    private enum Instances {
        INSTANCE(new EntityManager());
        EntityManager entityManager;

        Instances(EntityManager entityManager) {
            this.entityManager = entityManager;
        }
    }

    public void addEntity(Entity entity) {
        entityList.put(entity.getName(), entity);
        EntityEvent entityEvent = new EntityEvent(EntityEvent.Actions.ENTITY_ADD, entity);
        listeners.forEach(x -> x.actionPerformed(entityEvent));
    }

    public void removeEntity(Entity entity) {
        if (entityList.get(entity.getName()) == entity) {
            entityList.remove(entity.getName());
            EntityEvent entityEvent = new EntityEvent(EntityEvent.Actions.ENTITY_REMOVE, entity);
            listeners.forEach(x -> x.actionPerformed(entityEvent));
        }
    }

    public void addListener(ActionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ActionListener listener) {
        listeners.remove(listener);
    }

    public static EntityManager instance() {
        return Instances.INSTANCE.entityManager;
    }
}
