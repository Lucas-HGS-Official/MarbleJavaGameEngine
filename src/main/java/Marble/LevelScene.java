package Marble;

import java.awt.event.KeyEvent;

public class LevelScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.f;

    public LevelScene() {
        
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}