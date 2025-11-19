package io.github.oshan96.openfighting.engine;

import io.github.oshan96.openfighting.graphics.Renderer;
import io.github.oshan96.openfighting.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author oshan
 */
public class GameLoop extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(GameLoop.class);

    private static int fps_cap = 60;
    private static long targetTime = 1000000000 / fps_cap;   //frames per sec

    private int updateCount = 0;
    private long lastUpdateTime = 0;
    private final static int MAX_UPDATES = 5;

    private static GameLoop gameLoop = null;

    private GameLoop() {
        this.setName("Game Loop");
    }

    @Override
    public void run() {

        logger.info("GameLoop started (FPS cap: {} | targetTime: {}ns)", fps_cap, targetTime);

        lastUpdateTime = System.nanoTime();

        while(this.isAlive()) {

            //poll inputs

            updateCount = 0;
            long curretTime = System.nanoTime();

            //handle the lagging
            while (curretTime - lastUpdateTime >= targetTime) {
                //update world
                World.update();
                updateCount++;

                if (updateCount > 1) {
                    logger.debug("Lag compensation: {} updates in one frame", updateCount);
                }

                if (updateCount >= MAX_UPDATES) {
                    logger.warn("Max updates ({}) reached — frame skipped to catch up!", MAX_UPDATES);
                    break;
                }
                lastUpdateTime += targetTime;

                //detect collisions
                World.detectCollision();
            }

            //render
            long renderStart = System.nanoTime();
            Renderer.render();
            long renderTime = System.nanoTime() - renderStart;

            long startTime = System.nanoTime();
            long takenTime = System.nanoTime() - startTime;

            //fps capping
            if(takenTime < targetTime){
                try {
                    Thread.sleep((targetTime - takenTime) / 1000000);
                } catch (InterruptedException e) {
                    logger.error("GameLoop sleep interrupted", e);
                    Thread.currentThread().interrupt();
                }
            } else {
                logger.debug("Slow frame detected ({} ms)", renderTime / 1_000_000.0);
            }
        }

        logger.info("GameLoop stopped.");

    }

    public static GameLoop getInstance() {
        if(gameLoop == null) {
            logger.debug("Creating new GameLoop instance");
            gameLoop = new GameLoop();
        }

        return gameLoop;
    }

    public void setFPS(int fps) {
        fps_cap = fps;
        targetTime = 1_000_000_000L / fps;
        logger.info("FPS cap changed to {} (targetTime = {}ns)", fps_cap, targetTime);
    }  

    public int getFPS() {
        return fps_cap;
    }

    public static float getDelta() {
        return 1.0f * targetTime / 1000000000;
    }
}

