package io.github.oshan96.openfighting.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLoopTest {

    @Test
    void testSingletonInstance() {
        GameLoop instance1 = GameLoop.getInstance();
        GameLoop instance2 = GameLoop.getInstance();
        assertSame(instance1, instance2, "getInstance() should always return the same instance");
    }

    @Test
    void testFPSGetterSetter() {
        GameLoop gameLoop = GameLoop.getInstance();
        gameLoop.setFPS(120);
        assertEquals(120, gameLoop.getFPS(), "FPS should be set and retrieved correctly");
    }

    @Test
    void testDeltaCalculation() {
        GameLoop gameLoop = GameLoop.getInstance();
        gameLoop.setFPS(100);
        float expectedDelta = 1.0f * (1000000000 / 100) / 1000000000;
        assertEquals(expectedDelta, GameLoop.getDelta(), 0.00001, "Delta should reflect FPS setting");
    }
}