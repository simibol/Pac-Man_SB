package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ClydeStrategy implements GhostStrategy{

    private static final int THRESHOLD_DISTANCE = 8;
    private static final Vector2D BOTTOM_LEFT_CORNER = new Vector2D(0, 448);

    @Override
    public Vector2D getTargetLocation(Vector2D ghostPosition, Vector2D pacmanPosition, Vector2D blinkyPosition, Direction pacmanDirection) {
        
        double distanceToPacman = Vector2D.calculateEuclideanDistance(ghostPosition, pacmanPosition);

        Vector2D target = distanceToPacman > THRESHOLD_DISTANCE ? pacmanPosition : BOTTOM_LEFT_CORNER;

        return target;
        
    }
    
}
