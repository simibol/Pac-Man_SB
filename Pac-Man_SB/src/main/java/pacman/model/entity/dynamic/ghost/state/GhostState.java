package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.player.Pacman;

public interface GhostState {
    
    void enter(GhostImpl ghost);
    void execute(GhostImpl ghost);
    void exit(GhostImpl ghost);
}
