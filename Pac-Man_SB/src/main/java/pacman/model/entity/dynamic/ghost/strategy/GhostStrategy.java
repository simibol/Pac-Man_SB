package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface GhostStrategy {
    
    Vector2D getTargetLocation(Vector2D ghostPosition, Vector2D pacmanPosition, Vector2D blinkyPosition, Direction pacmanDirection);
}
