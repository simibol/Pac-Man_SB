package pacman.model.entity.dynamic.ghost.strategy;



import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

public class PinkyStrategy implements GhostStrategy{

    

    @Override
    public Vector2D getTargetLocation(Vector2D ghostPosition, Vector2D pacmanPosition, Vector2D blinkyPosition, Direction pacmanDirection) {
        int offset = 4;
        
        return pacmanPosition.add(getPacmanDirectionOffset(pacmanDirection, offset));
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
