package io.github.oshan96.openfighting.inputs;

import com.jogamp.newt.event.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @author oshan
 */
public class KeyEventListener implements com.jogamp.newt.event.KeyListener {

    private static final Logger logger = LoggerFactory.getLogger(KeyEventListener.class);

    private static Set<Short> registeredKeys = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        short code = keyEvent.getKeyCode();
        if (!registeredKeys.contains(code)) {
            logger.debug("Key pressed: {} ({})", keyEvent.getKeyChar(), code);
        }
        registeredKeys.add(code);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        short code = keyEvent.getKeyCode();
        logger.debug("Key released: {} ({})", keyEvent.getKeyChar(), code);
        registeredKeys.remove(code);
    }

    public static boolean isRegisteredKey(short keyCode) {
        return registeredKeys.contains(keyCode);
    }

    public static void clearKeys() {
        if (!registeredKeys.isEmpty()) {
            logger.debug("Clearing {} registered keys", registeredKeys.size());
        }
        registeredKeys.clear();
    }
}