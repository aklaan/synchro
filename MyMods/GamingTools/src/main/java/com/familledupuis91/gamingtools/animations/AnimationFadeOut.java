package com.familledupuis91.gamingtools.animations;

import com.familledupuis91.gamingtools.components.GameObject;

public class AnimationFadeOut extends Animation {

    public AnimationFadeOut(GameObject animatedGameObject) {
        super(animatedGameObject);
        launch();
    }

    @Override
    public void launch() {
        super.launch();
        this.setStatus(AnimationStatus.PLAYING);
        this.setSpeed(0.01f);

    }

    @Override
    public void play() {

        if (this.getAnimatedGameObject().getAlpha() > 0.001) {
            this.getAnimatedGameObject().setAlpha(
                    this.getAnimatedGameObject().getAlpha() - (this.getSpeed()));
        } else {
            this.getAnimatedGameObject().setAlpha(1.0f);
            this.stop();

        }

    }


}
