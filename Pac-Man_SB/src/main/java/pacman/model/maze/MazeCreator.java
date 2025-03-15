package pacman.model.maze;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.decorator.PowerPelletDecorator;
import pacman.model.factories.RenderableFactoryRegistry;
import pacman.model.factories.RenderableType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.System.exit;

/**
 * Responsible for creating renderables and storing it in the Maze
 */
public class MazeCreator {

    public static final int RESIZING_FACTOR = 16;
    private final String fileName;
    private final RenderableFactoryRegistry renderableFactoryRegistry;

    public MazeCreator(String fileName,
                       RenderableFactoryRegistry renderableFactoryRegistry) {
        this.fileName = fileName;
        this.renderableFactoryRegistry = renderableFactoryRegistry;
    }

    public Maze createMaze() {
        File f = new File(this.fileName);
        Maze maze = new Maze();

        try {
            Scanner scanner = new Scanner(f);

            int y = 0;

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                char[] row = line.toCharArray();

                for (int x = 0; x < row.length; x++) {
                    Vector2D position = new Vector2D(x * RESIZING_FACTOR, y * RESIZING_FACTOR);

                    char renderableType = row[x];

                    Renderable renderable = renderableFactoryRegistry.createRenderable(renderableType, position);

                    if(renderableType == RenderableType.BLINKY){
                        maze.setBlinkyPosition(renderable.getPosition());
                        
                    }
                    switch (renderableType) {
                        case RenderableType.BLINKY:
                            maze.setBlinkyInitialPosition(position);
                            break;
                        case RenderableType.INKY:
                            maze.setInkyInitialPosition(position);
                            break;
                        case RenderableType.PINKY:
                            maze.setPinkyInitialPosition(position);
                            break;
                        case RenderableType.CLYDE:
                            maze.setClydeInitialPosition(position);
                            break;
                    
                        default:
                            break;
                    }

                    if(renderableType == RenderableType.POWER_PELLET && renderable instanceof Pellet){
                        Vector2D offsetPosition = position.add(new Vector2D(-8, -8));
                        renderable = new PowerPelletDecorator((Pellet) renderable);
                        ((Pellet) renderable).getBoundingBox().setTopLeft(offsetPosition);
                        System.out.println("Power Pellet created at: " + position);
                        
                    }

                    maze.addRenderable(renderable, renderableType, x, y);
                }

                y += 1;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No maze file was found.");
            exit(0);
        }

        return maze;
    }
}
