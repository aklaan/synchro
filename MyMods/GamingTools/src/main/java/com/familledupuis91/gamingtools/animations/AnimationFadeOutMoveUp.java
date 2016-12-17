package com.familledupuis91.gamingtools.animations;

import com.familledupuis91.gamingtools.components.GameObject;


public class AnimationFadeOutMoveUp extends Animation {


    public AnimationFadeOutMoveUp(GameObject animatedGameObject) {
        super(animatedGameObject);
        this.setSpeed(0.01f);
        launch();

    }


    @Override
    public void play() {
        this.getAnimatedGameObject().setY(this.getAnimatedGameObject().getY() + 1);
        if (this.getAnimatedGameObject().getAlpha() > 0.001) {
            this.getAnimatedGameObject().setAlpha(
                    this.getAnimatedGameObject().getAlpha() - (this.getSpeed()));
        } else {
            this.getAnimatedGameObject().setAlpha(1.0f);
            this.stop();

        }

    }


}
