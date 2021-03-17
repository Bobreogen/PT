package logic;

import entities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class RenderManager {

    private final ArrayList<GraphicsEntity> graphicsEntityList = new ArrayList<>();

    private enum Instances {
        INSTANCE(new RenderManager());
        RenderManager renderManager;

        Instances(RenderManager renderManager) {
            this.renderManager = renderManager;
        }
    }

    private RenderManager() {
        EntityManager.instance().addListener(e -> {
            if (e.getClass() == EntityEvent.class) {
                switch (((EntityEvent) e).getAction()) {
                    case ENTITY_ADD -> entityAdd(((EntityEvent) e).getEntity());
                    case ENTITY_REMOVE -> entityRemove(((EntityEvent) e).getEntity());
                }
            }
        });
    }

    public void render(Graphics g) {
        graphicsEntityList.forEach(graphicsEntity -> {
            graphicsEntity.paint(g);
        });

        g.dispose();
    }

    private void entityAdd(Entity entity) {
        BufferedImage image = findImageForEntity(entity);
        GraphicsEntity graphicsEntity = null;
        if(image != null) {
            graphicsEntity = new GraphicsEntity(entity, image);
            graphicsEntityList.add(graphicsEntity);
        }
    }

    private void entityRemove(Entity entity) {
        graphicsEntityList.removeIf(graphicsEntity -> graphicsEntity.getEntity() == entity);
    }

    private BufferedImage findImageForEntity(Entity entity) {
        BufferedImage image = null;
        try {
            if (entity instanceof Car)
                image = ImageIO.read(new File("resourses/car.png"));
            if (entity instanceof Truck)
                image = ImageIO.read(new File("resourses/truck.png"));
        } catch (Exception e) {}

        return image;
    }

    public static RenderManager instance() { return Instances.INSTANCE.renderManager; }
}
