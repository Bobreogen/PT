package entities;

public abstract class Entity implements IBehaviour {
    protected String name;
    public String getName() {return name; }

    public void setName(String name) {
        this.name = name;
    }

    Entity(String name) {
        this.name = name;
    }
}
