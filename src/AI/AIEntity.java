package AI;

import AI.AIBehavior;
import entities.Entity;

public class AIEntity {
    private Entity entity;
    private AIBehavior behavior;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public AIBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(AIBehavior behavior) {
        this.behavior = behavior;
    }
}
