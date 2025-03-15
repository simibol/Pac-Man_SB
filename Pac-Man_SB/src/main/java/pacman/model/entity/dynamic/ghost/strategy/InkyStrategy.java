package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;



public class InkyStrategy implements GhostStrategy{
    

    @Override
    public Vector2D getTargetLocation(Vector2D ghostPosition, Vector2D pacmanPosition, Vector2D blinkyPosition, Direction pacmanDirection) {
        
        //Direction pacmanDirection = maze.getPacmaDirection();

        Vector2D twoAhead = pacmanPosition.add(getPacmanDirectionOffset(pacmanDirection, 2));
        Vector2D vectorToAhead = twoAhead.subtract(blinkyPosition);
        return blinkyPosition.add(vectorToAhead.multiply(2));

       // return pacmanPosition;
    }
    private Vector2D getPacmanDirectionOffset(Direction pacmanDirection, int offset){
        
        switch (pacmanDirection){
            case UP:
                return new Vector2D(0, -offset);
            case DOWN:
                return new Vector2D(0, offset);
            case LEFT:
                return new Vector2D(-offset, 0);
            case RIGHT:
                return new Vector2D(offset, 0);
            default:
                return new Vector2D(0, 0);
        }
        
    }

    
}
