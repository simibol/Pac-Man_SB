package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;


public class RespawnState implements GhostState {

    private static final int RESPAWN_DELAY_TICKS = 30;
    private int respawnTimer;

    @Override
    public void enter(GhostImpl ghost) {

        respawnTimer = RESPAWN_DELAY_TICKS;
        ghost.resetNormalSprite();
        ghost.setTargetLocation(ghost.getStartingPosition());
        ghost.setSpeed(0);
        ghost.setPosition(ghost.getStartingPosition());
    }

    @Override
    public void execute(GhostImpl ghost) {
        
        if(respawnTimer > 0){
            respawnTimer--;
        } else {

            
            ghost.setGhostMode(GhostMode.SCATTER);
            ghost.setSpeed(ghost.getSpeeds().get(GhostMode.SCATTER));
            ghost.updateDirectionTowards(ghost.getTargetLocation());
            
        }

    }

    @Override
    public void exit(GhostImpl ghost) {
        System.out.println("Exited RESPAWN mode for ghost: " + ghost.getGhostType());

        ghost.setGhostMode(GhostMode.SCATTER);
        ghost.setSpeed(ghost.getSpeeds().get(GhostMode.SCATTER));
        ghost.updateDirectionTowards(ghost.getTargetLocation());
        ghost.resetNormalSprite();
    }
    
}
