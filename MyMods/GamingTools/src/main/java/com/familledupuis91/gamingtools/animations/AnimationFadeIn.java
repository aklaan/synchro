package com.familledupuis91.gamingtools.animations;

import android.os.SystemClock;

import com.familledupuis91.gamingtools.components.GameObject;

public class AnimationFadeIn extends Animation {


    /**
     * dans l'idéal, si on faisait une frame  toutes les 1 ms
     * alors pour une animation de 2000 ms
     * il faudrait faire 2000 frame, soit un speed de 1/2000
     * mais en réalité, il faut plus de temps pour afficher une frame
     * par exemple en 40fps, on a pour une frame, une durée de traitement de 1000ms/40= 25ms
     *
     * si on veut être rigpoureux, il faut varier la vitesse a chaque fois que l'on passe dans le PLAY
     * pour compenser la variation des FPS
     *
     * @param animatedGameObject
     * @param duration
     */
    public AnimationFadeIn(GameObject animatedGameObject,int duration) {
        super(animatedGameObject);
        this.setDuration(duration);

      this.launch();
    }



    @Override
    public void play() {
        //on commence à calculer le délai de l'animation au moment où on traite la première frame.
        if (this.getStartTime()<0){
            this.setStartTime(SystemClock.elapsedRealtime());}

        //je récupère le temp courant
        long currentTime = SystemClock.elapsedRealtime();

        //le met à jour le temps écoulé depuis le début de l'animation
        this.setElapsedTime(currentTime- this.getStartTime());

        float alpha = (float)(this.getElapsedTime()/this.getDuration());
        this.getAnimatedGameObject().setAlpha(alpha);

//        Log.e("alpha in",String.valueOf(alpha));
        if (this.getElapsedTime() > this.getDuration()) {

            this.stop();
        }

    }
}
