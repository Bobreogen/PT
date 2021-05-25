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
        float dtInt = (float)dt/1000;
        if(abs(targetX - owner.getPositionX()) < velocity*dtInt)
            owner.setPositionX(targetX);
        else
            owner.setPositionX(targetX > owner.getPositionX() ? owner.getPositionX() + (int)(velocity*dtInt) : owner.getPositionX() - (int)(velocity*dtInt));
        if(abs(targetY - owner.getPositionY()) < velocity*dtInt)
            owner.setPositionY(targetY);
        else
            owner.setPositionY(targetY > owner.getPositionY() ? owner.getPositionY() + (int)(velocity*dtInt) : owner.getPositionY() - (int)(velocity*dtInt));
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
