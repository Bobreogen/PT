package gui;

import entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsEntity extends JPanel {
    private Entity entity;
    private BufferedImage image;

    public GraphicsEntity(Entity entity, BufferedImage image) {
        this.entity = entity;
        this.image = image;
        setSize(100, 100);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(image, entity.getPositionX(), entity.getPositionY(), 30, 30, null);
    }

}
