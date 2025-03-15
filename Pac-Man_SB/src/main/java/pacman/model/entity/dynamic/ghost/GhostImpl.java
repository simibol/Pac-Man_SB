package pacman.model.entity.dynamic.ghost;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.state.ChaseState;
import pacman.model.entity.dynamic.ghost.state.FrightenedState;
import pacman.model.entity.dynamic.ghost.state.GhostState;
import pacman.model.entity.dynamic.ghost.state.RespawnState;
import pacman.model.entity.dynamic.ghost.state.ScatterState;
import pacman.model.entity.dynamic.ghost.strategy.BlinkyStrategy;
import pacman.model.entity.dynamic.ghost.strategy.ClydeStrategy;
import pacman.model.entity.dynamic.ghost.strategy.GhostStrategy;
import pacman.model.entity.dynamic.ghost.strategy.InkyStrategy;
import pacman.model.entity.dynamic.ghost.strategy.PinkyStrategy;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.factories.RenderableType;
import pacman.model.level.Level;
import pacman.model.maze.Maze;
import pacman.model.entity.dynamic.player.Pacman;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {

    private static final int minimumDirectionCount = 8;
    private final Layer layer = Layer.FOREGROUND;
    private Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private Map<GhostMode, Double> speeds;
    private int currentDirectionCount = 0;
    private Maze maze;
    private char ghostType;

    private GhostState currentState;
    private GhostStrategy chaseStrategy;
    

    private static final Image FRIGHTENED_IMAGE = new Image("maze/ghosts/frightened.png");
    

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Maze maze, Vector2D targetCorner, GhostStrategy chaseStrategy, char ghostType) {
        // System.out.println("GhostImpl instantiated: speeds should be set via setSpeeds() before any usage.");
        this.image = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.currentDirection = null;
        this.maze = maze;
        this.chaseStrategy = chaseStrategy;
        this.ghostType = ghostType;
       
        
    }
    public char getGhostType(){
        return this.ghostType;
    }
    public GhostMode getGhostMode(){
        return this.ghostMode;
    }
    public void setFrightenedSprite(){
        this.image = FRIGHTENED_IMAGE;
    }

    public void respawnInScatterMode(){
        this.setPosition(startingPosition);
        setGhostMode(GhostMode.RESPAWN);
    }


    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds = speeds;
    }
    public void setSpeed(double speed){
        this.kinematicState.setSpeed(speed);
        System.out.println("Speed set to " + speed + " for ghost: " + this.getGhostType());  //See if its setting it for specifically clyde
    }
    public Map<GhostMode, Double> getSpeeds(){
        return this.speeds;
    }

    @Override
    public Image getImage() {
        return image;
    }

    

    @Override
    public void update() {

        if(this.currentState != null){
            this.currentState.execute(this);
        }
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());

        if(this.ghostType == RenderableType.BLINKY){
            maze.setBlinkyPosition(this.getPosition());
        }

    }
    @Override
    public void setGhostMode(GhostMode ghostMode) {

        if(this.ghostMode == GhostMode.FRIGHTENED || this.ghostMode != GhostMode.RESPAWN){
            resetNormalSprite();
        }
        this.ghostMode = ghostMode;
        // ensure direction is switched

        if (this.speeds != null && this.speeds.containsKey(ghostMode)) {
            this.kinematicState.setSpeed(speeds.get(ghostMode));
        }
        switch (ghostMode){
            case CHASE:
                this.currentState = new ChaseState();
                configureChaseStrategy();
                this.targetLocation = getChaseTarget();
                // this.currentState.enter(this);
                break;
            case SCATTER:
                this.currentState = new ScatterState();
                this.targetLocation = targetCorner;
                // this.currentState.enter(this);
                break;
            case FRIGHTENED:
                this.currentState = new FrightenedState();
                // this.currentState.enter(this);
                break;
            case RESPAWN:
                this.currentState = new RespawnState();
                // this.currentState.enter(this);
                break;
            
            default:
                System.out.println("Unkown Ghost mode");
                break;
        }
        this.currentState.enter(this);
        this.currentDirectionCount = minimumDirectionCount;
    }

    public boolean reachedRespawnPoint(){
        return Vector2D.calculateEuclideanDistance(this.getPosition(), startingPosition) < 0.5;
    }
    public void setTargetLocation(Vector2D targetLocation){
        this.targetLocation = targetLocation;
    }

    public void updateDirectionTowards(Vector2D target){
        Vector2D currentPosition = this.getPosition();
        double shortestDistance = Double.MAX_VALUE;
        Direction bestDirection = null;

        for (Direction direction : possibleDirections){
            Vector2D newPosition = currentPosition.add(direction.toVector());
            double distanceToTarget = Vector2D.calculateEuclideanDistance(newPosition, target);

            if(distanceToTarget < shortestDistance){
                shortestDistance = distanceToTarget;
                bestDirection = direction;
            }
        }
        if(bestDirection != null){
            this.kinematicState.setDirection(bestDirection);
        }
    }

    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(this.possibleDirections)) {
            this.targetLocation = getTargetLocation();
        }

        Direction newDirection = selectDirection(possibleDirections);

        // Ghosts have to continue in a direction for a minimum time before changing direction
        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }

    public Vector2D getTargetLocation() {
        return switch (this.ghostMode) {
            case CHASE -> this.playerPosition;
            case SCATTER -> this.targetCorner;
            case FRIGHTENED -> getRandomFrightenedTarget();
            case RESPAWN -> this.startingPosition;
        };
    }

    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        // ghosts have to continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            currentDirectionCount++;
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            // ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        // only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        // select the direction that will reach the target location fastest
        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

   
    public void configureChaseStrategy(){

        switch (this.ghostType){
            case RenderableType.BLINKY:
                this.chaseStrategy = new BlinkyStrategy();
                break;
            case RenderableType.INKY:
                
                this.chaseStrategy = new InkyStrategy();
                break;
            case RenderableType.PINKY:
                this.chaseStrategy = new PinkyStrategy();
                break;
            case RenderableType.CLYDE:
                this.chaseStrategy = new ClydeStrategy();
                break;
            default:
                System.out.println("unknown ghost");
                break;
        }
        if(this.chaseStrategy == null){
            System.out.println("chase strategy null");
        }
    }


    // private void setRespawnMode(){
    //     this.kinematicState.setSpeed(speeds.get(GhostMode.SCATTER));
    //     this.targetLocation = startingPosition; // set target to respawn point
    //     this.resetNormalSprite();
    // }
    public void resetNormalSprite(){
        switch (this.ghostType) {
            case RenderableType.BLINKY:
                this.image = new Image("maze/ghosts/blinky.png");
                break;
            case RenderableType.INKY:
                this.image = new Image("maze/ghosts/inky.png");
                break;
            case RenderableType.PINKY:
                this.image = new Image("maze/ghosts/pinky.png");
                break;
            case RenderableType.CLYDE:
                this.image = new Image("maze/ghosts/clyde.png");
                break;
            
            default:
                System.out.println("Unknowbn ghost type");
                break;
        }
    }

    public Vector2D getRandomFrightenedTarget(){
        System.out.println("RandommmmDirectionnnnnnnnnnnnnnnnnnn");
        Random random = new Random();
        int x = random.nextInt(448);
        int y = random.nextInt(448);
        return new Vector2D(x, y);
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    public Vector2D getStartingPosition(){
        return this.startingPosition;
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)){
            Pacman pacman = (Pacman) renderable;

            switch(this.ghostMode){
                case FRIGHTENED:
                    pacman.handleGhostEaten(level, this);
                    break;
                case RESPAWN:
                    System.out.println("Ignoring collision in respawn mode");
                    break;
                default:
                    level.handleLoseLife();
                    break;
            }
        }
    }

    @Override
    public void updatePlayerPosition(Vector2D playerPosition) {
        this.playerPosition = playerPosition;
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        // return ghost to starting position
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(startingPosition);
        this.ghostMode = GhostMode.SCATTER;
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    private Vector2D getChaseTarget(){

        Vector2D blinkyPosition = maze.getBlinkyPosition();
        Direction pacmanDirection = maze.getPacmanDirection();

        return chaseStrategy.getTargetLocation(getPosition(), playerPosition, blinkyPosition, pacmanDirection);
    }
    @Override
    public Maze getMaze(){
        // if(this.maze == null){
        //     System.out.println("Maze is null");
        // }
        return this.maze;
    }
}
