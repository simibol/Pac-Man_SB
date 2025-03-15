package pacman.model.entity.dynamic.ghost;

/***
 * Represents the different modes of ghosts, which determines how ghosts choose their target locations
 */
public enum GhostMode {
    SCATTER,
    CHASE,
    FRIGHTENED,
    RESPAWN;

    /**
     * Ghosts alternate between SCATTER and CHASE mode normally
     *
     * @param ghostMode current ghost mode
     * @return next ghost mode
     */
    public static GhostMode getNextGhostMode(GhostMode ghostMode) {
        if(ghostMode == SCATTER){
            return CHASE;
        } else if (ghostMode == CHASE){
            return SCATTER;
        } else if (ghostMode == FRIGHTENED){
            return RESPAWN;
        } else if(ghostMode == RESPAWN){
            return SCATTER;
        }
        return ghostMode;
    }
}
