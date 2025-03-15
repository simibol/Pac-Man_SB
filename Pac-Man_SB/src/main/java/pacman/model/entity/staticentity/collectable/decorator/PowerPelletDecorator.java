package pacman.model.entity.staticentity.collectable.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.staticentity.collectable.Pellet;

public class PowerPelletDecorator extends Pellet{
    private static final int POWER_PELLET_POINTS = 50;
    private static final double SCALE_FACTOR = 2.0;

    private Pellet pellet;

    public PowerPelletDecorator(Pellet pellet){
        super(
            new BoundingBoxImpl(pellet.getBoundingBox().getTopLeft(), 
            pellet.getBoundingBox().getHeight() * SCALE_FACTOR, 
            pellet.getBoundingBox().getWidth() * SCALE_FACTOR),
            pellet.getLayer(),
            pellet.getImage(),
            POWER_PELLET_POINTS
        );
        this.pellet = pellet;
    }
    @Override
    public int getPoints(){
        return POWER_PELLET_POINTS;
    }
    @Override
    public Image getImage(){
        return pellet.getImage();
    }

    @Override
    public void collect(){
        super.collect();
    }
    @Override
    public boolean isPowerPellet(){
        return true;
    }
}
