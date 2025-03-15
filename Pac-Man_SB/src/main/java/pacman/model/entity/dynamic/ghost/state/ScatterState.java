package pacman.model.entity.dynamic.ghost.state;

import java.util.Vector;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ScatterState implements GhostState{

    @Override
    public void enter(GhostImpl ghost) {
        ghost.setSpeed(ghost.getSpeeds().get(GhostMode.SCATTER));
    }

    @Override
    public void execute(GhostImpl ghost) {
        Vector2D target = ghost.getTargetLocation();
        ghost.updateDirectionTowards(target);
    }

    @Override
    public void exit(GhostImpl ghost) {
        
    }


    
}
