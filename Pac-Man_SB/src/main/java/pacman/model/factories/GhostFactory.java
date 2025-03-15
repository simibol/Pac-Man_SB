package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.strategy.BlinkyStrategy;
import pacman.model.entity.dynamic.ghost.strategy.ClydeStrategy;
import pacman.model.entity.dynamic.ghost.strategy.GhostStrategy;
import pacman.model.entity.dynamic.ghost.strategy.InkyStrategy;
import pacman.model.entity.dynamic.ghost.strategy.PinkyStrategy;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.LevelConfigurationReader;
import pacman.model.maze.Maze;
import java.util.Map;

/**
 * Concrete renderable factory for Ghost objects
 */
public class GhostFactory implements RenderableFactory {

    private static final int RIGHT_X_POSITION_OF_MAP = 448;
    private static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    private static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

    private static final Image BLINKY_IMAGE = new Image("maze/ghosts/blinky.png");
    private static final Image INKY_IMAGE = new Image("maze/ghosts/inky.png");
    private static final Image CLYDE_IMAGE = new Image("maze/ghosts/clyde.png");
    private static final Image PINKY_IMAGE = new Image("maze/ghosts/pinky.png");

    private final char ghostType;
    private final Maze maze;
    private LevelConfigurationReader levelConfigurationReader;

    public GhostFactory(Maze maze, char ghostType, LevelConfigurationReader levelConfigurationReader){
        this.maze = maze;
        this.ghostType = ghostType;
        this.levelConfigurationReader = levelConfigurationReader;
    }

    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {


        try {
            position = position.add(new Vector2D(4, -4));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    getGhostImage().getHeight(),
                    getGhostImage().getWidth()
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .build();
            GhostStrategy strategy;
            

            switch (ghostType){
                case RenderableType.BLINKY:
                        strategy = new BlinkyStrategy();
                        break;
                case RenderableType.INKY:
                        strategy = new InkyStrategy();
                        break;
                case RenderableType.PINKY:
                        strategy = new PinkyStrategy();
                        break;
                case RenderableType.CLYDE:
                        strategy = new ClydeStrategy();
                        break;
                default:
                        strategy = new BlinkyStrategy();
                }
        
            
            GhostImpl ghost = new GhostImpl(
                    getGhostImage(),
                    boundingBox,
                    kinematicState,
                    GhostMode.SCATTER,
                    maze,
                    getScatterTarget(),
                    strategy, ghostType
            );
            Map<GhostMode, Double> ghostSpeeds = levelConfigurationReader.getGhostSpeeds();
            if (ghostSpeeds == null) {
                throw new ConfigurationParseException("Speeds configuration is missing for ghosts.");
            }
            ghost.setSpeeds(ghostSpeeds);

            ghost.setGhostMode(GhostMode.SCATTER);

            return ghost;

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }

    private Image getGhostImage(){
        switch (ghostType){
                case RenderableType.BLINKY:
                        return BLINKY_IMAGE;
                case RenderableType.PINKY:
                        return PINKY_IMAGE;
                case RenderableType.INKY:
                        return INKY_IMAGE;
                case RenderableType.CLYDE:
                        return CLYDE_IMAGE;
                default:
                        return BLINKY_IMAGE;
        }
    }
    private Vector2D getScatterTarget(){
        switch (ghostType){
                case RenderableType.BLINKY:
                        return new Vector2D(RIGHT_X_POSITION_OF_MAP, 0);
                case RenderableType.PINKY:
                        return new Vector2D(0, 0);
                case RenderableType.INKY:
                        return new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP);
                case RenderableType.CLYDE:
                        return new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP);
                default:
                        return new Vector2D(RIGHT_X_POSITION_OF_MAP, 0);

        }
    }


}
