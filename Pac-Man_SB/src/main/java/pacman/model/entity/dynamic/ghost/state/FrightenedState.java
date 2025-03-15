package pacman.model.entity.dynamic.ghost.state;

import java.util.Random;
import java.util.Vector;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.maze.Maze;

public class FrightenedState implements GhostState{
    private int frightenedTimer;

    @Override
    public void enter(GhostImpl ghost) {
        frightenedTimer = ghost.getMaze().getLevelConfigurationReader().getGhostModeLengths().get(GhostMode.FRIGHTENED);
        ghost.setSpeed(ghost.getSpeeds().get(GhostMode.FRIGHTENED));
        ghost.setFrightenedSprite();
    }

    @Override
    public void execute(GhostImpl ghost) {
        if(frightenedTimer > 0){
            frightenedTimer--;
            Vector2D randomTarget = ghost.getRandomFrightenedTarget();
            ghost.updateDirectionTowards(randomTarget);
        } else {
            ghost.setGhostMode(GhostMode.SCATTER);
            ghost.resetNormalSprite();
        }

        
    }

    @Override
    public void exit(GhostImpl ghost) {

        ghost.resetNormalSprite();
    }

}
