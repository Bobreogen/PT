package AI;

import AI.AIBehavior;
import entities.Entity;

import static java.lang.Math.abs;

public class GoToPositionStrategy extends AIBehavior {
    private Entity owner;
    private int targetX;
    private int targetY;
    private int velocity;

    @Override
    public void onFrame(long dt) {
        if(abs(targetX - owner.getPositionX()) < 10 && abs(targetY - owner.getPositionY()) < 10)
            return;
        int dtInt = (int)dt;
        owner.setPosition(targetX > owner.getPositionX() ? owner.getPositionX() + velocity*dtInt/1000 : owner.getPositionX() - velocity*dtInt/1000
                , targetY > owner.getPositionY() ? owner.getPositionY() + velocity*dtInt/1000 : owner.getPositionY() - velocity*dtInt/1000);
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}
