package pacman.model.maze;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.factories.RenderableType;
import pacman.model.level.LevelConfigurationReader;

import java.util.*;


/**
 * Stores and manages the renderables for the Pac-Man game
 */
public class Maze {

    private static final int MAX_CENTER_DISTANCE = 4;
    private final List<Renderable> renderables;
    private final List<Renderable> ghosts;
    private final List<Renderable> pellets;
    private final Map<String, Boolean> isWall;
    private Renderable pacman;
    private int numLives;
    private Direction pacmanDirection = Direction.RIGHT;
    
    private Vector2D blinkyPosition;

    private Vector2D blinkyInitialPosition;
    private Vector2D inkyInitialPosition;
    private Vector2D pinkyInitialPosition;
    private Vector2D clydeInitialPosition;

    private LevelConfigurationReader levelConfigurationReader;

    public Maze() {
        this.renderables = new ArrayList<>();
        this.ghosts = new ArrayList<>();
        this.pellets = new ArrayList<>();
        this.isWall = new HashMap<>();
    }

    private static String formatCoordinates(int x, int y) {
        return String.format("(%d, %d)", x, y);
    }

    /**
     * Returns true if possible directions indicates entity is at an intersection (i.e. can turn in at least 2 adjacent directions)
     *
     * @param possibleDirections possible directions of entity
     * @return true, if entity is at intersection
     */
    public static boolean isAtIntersection(Set<Direction> possibleDirections) {
        // can turn
        if (possibleDirections.contains(Direction.LEFT) || possibleDirections.contains(Direction.RIGHT)) {
            return possibleDirections.contains(Direction.UP) ||
                    possibleDirections.contains(Direction.DOWN);
        }

        return false;
    }

    /**
     * Adds the renderable to maze
     *
     * @param renderable     renderable to be added
     * @param renderableType the renderable type
     * @param x              grid X position
     * @param y              grid Y position
     */
    public void addRenderable(Renderable renderable, char renderableType, int x, int y) {
        if (renderable != null) {
            if (renderableType == RenderableType.PACMAN) {
                this.pacman = renderable;

            } else if (renderableType == RenderableType.BLINKY){
                this.ghosts.add(renderable);
            } else if (renderableType == RenderableType.INKY ||
                        renderableType == RenderableType.PINKY ||
                        renderableType == RenderableType.CLYDE) {
                this.ghosts.add(renderable);
            } else if (renderableType == RenderableType.PELLET || renderableType == RenderableType.POWER_PELLET) {
                this.pellets.add(renderable);
            } else {
                this.isWall.put(formatCoordinates(x, y), true);
            }

            this.renderables.add(renderable);
        }
    }
    public void setBlinkyPosition(Vector2D position){
        this.blinkyPosition = position;
    }

    public Vector2D getBlinkyPosition(){
        return blinkyPosition;
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

    public Renderable getControllable() {
        return pacman;
    }

    public List<Renderable> getGhosts() {
        return ghosts;
    }

    public List<Renderable> getPellets() {
        return pellets;
    }

    private int getCenterOfTile(int index) {
        return index * MazeCreator.RESIZING_FACTOR + MazeCreator.RESIZING_FACTOR / 2;
    }
    public Direction getPacmanDirection(){
        return this.pacmanDirection;
    }
    public void setPacmanDirection(Direction direction){
        this.pacmanDirection = direction;
    }

    /**
     * Updates the possible directions of the dynamic entity based on the maze configuration
     */
    public void updatePossibleDirections(DynamicEntity dynamicEntity) {
        int xTile = (int) Math.floor(dynamicEntity.getCenter().getX() / MazeCreator.RESIZING_FACTOR);
        int yTile = (int) Math.floor(dynamicEntity.getCenter().getY() / MazeCreator.RESIZING_FACTOR);

        Set<Direction> possibleDirections = new HashSet<>();

        if (Math.abs(getCenterOfTile(xTile) - dynamicEntity.getCenter().getX()) < MAX_CENTER_DISTANCE &&
                Math.abs(getCenterOfTile(yTile) - dynamicEntity.getCenter().getY()) < MAX_CENTER_DISTANCE) {

            String aboveCoordinates = formatCoordinates(xTile, yTile - 1);
            if (isWall.get(aboveCoordinates) == null) {
                possibleDirections.add(Direction.UP);
            }

            String belowCoordinates = formatCoordinates(xTile, yTile + 1);
            if (isWall.get(belowCoordinates) == null) {
                possibleDirections.add(Direction.DOWN);
            }

            String leftCoordinates = formatCoordinates(xTile - 1, yTile);
            if (isWall.get(leftCoordinates) == null) {
                possibleDirections.add(Direction.LEFT);
            }

            String rightCoordinates = formatCoordinates(xTile + 1, yTile);
            if (isWall.get(rightCoordinates) == null) {
                possibleDirections.add(Direction.RIGHT);
            }
        } else {
            possibleDirections.add(dynamicEntity.getDirection());
            possibleDirections.add(dynamicEntity.getDirection().opposite());
        }

        dynamicEntity.setPossibleDirections(possibleDirections);
    }

    public int getNumLives() {
        return numLives;
    }

    public void setNumLives(int numLives) {
        this.numLives = numLives;
    }

    /**
     * Resets all renderables to starting state
     */
    public void reset() {
        for (Renderable renderable : renderables) {
            renderable.reset();
        }
    }


    public Vector2D getBlinkyInitialPosition(){
        return blinkyInitialPosition;
    }
    public Vector2D getInkyInitialPosition(){
        return inkyInitialPosition;
    }
    public Vector2D getPinkyInitialPosition(){
        return pinkyInitialPosition;
    }
    public Vector2D getClydeInitialPosition(){
        return clydeInitialPosition;
    }

    public void setBlinkyInitialPosition(Vector2D position){
        this.blinkyInitialPosition = position;
    }
    public void setInkyInitialPosition(Vector2D position){
        this.inkyInitialPosition = position;
    }
    public void setPinkyInitialPosition(Vector2D position){
        this.pinkyInitialPosition = position;
    }
    public void setClydeInitialPosition(Vector2D position){
        this.clydeInitialPosition = position;
    }



    public void setLevelConfigurationReader(LevelConfigurationReader reader){
        this.levelConfigurationReader = reader;
    }
    public LevelConfigurationReader getLevelConfigurationReader(){
        return this.levelConfigurationReader;
    }

}
