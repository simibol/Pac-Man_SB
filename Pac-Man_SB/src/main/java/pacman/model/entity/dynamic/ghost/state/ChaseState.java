package pacman.model.entity.dynamic.ghost.state;

import java.util.Map;

//import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
//import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ChaseState implements GhostState{

    @Override
    public void enter(GhostImpl ghost) {
        System.out.println("Entering CHASE mode for ghost: " + ghost.getGhostType());

        // Map<GhostMode, Double> speeds = ghost.getSpeeds();
        ghost.setSpeed(ghost.getSpeeds().get(GhostMode.CHASE));
        ghost.configureChaseStrategy();
    }

    @Override
    public void execute(GhostImpl ghost) {
        System.out.println("CHASE mode active for ghost: " + ghost.getGhostType());

        Vector2D target = ghost.getTargetLocation();
        ghost.updateDirectionTowards(target);
    }

    @Override
    public void exit(GhostImpl ghost) {
        System.out.println("Exited CHASE mode for ghost: " + ghost.getGhostType());

        
    }

    
    
}
