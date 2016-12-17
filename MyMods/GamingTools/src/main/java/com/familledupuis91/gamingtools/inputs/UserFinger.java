package com.familledupuis91.gamingtools.inputs;

import android.opengl.Matrix;

import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.enums.DrawingMode;

public class UserFinger extends Rectangle2D {

    float worldX;
    float worldY;
    float histoX;
    float histoY;

    public final static String USER_FINGER_TAG = "USER_FINGER_TAG";

    public UserFinger() {
        super(DrawingMode.EMPTY);
        this.setHeight(20);
        this.setWidth(20);
        this.enableCollisions();
        this.enableCollisionChecking();
        this.setTagName(USER_FINGER_TAG);

        // on fait exprès de définir le premier point loin de l'écran
        //pour éviter les colision au premier cycle
        this.setX(10000);

    }

    @Override
    public void update() {
        //   Log.e("userFinger","on Update");

        //on réinitialise les coordonées
        this.setCoord(0f, 0f);
        //on désactive la gestion des colisions
        this.disableCollisionChecking();

        // si l'utilisateur touche l'écran
        if (this.getScene().getActivity().mGLSurfaceView.touched) {

            int toto = this.getScene().getHeightDevice();
            float yy = this.getScene().getActivity().mGLSurfaceView.touchY;


            /**
             *

             le point 0,0 est en haut à gauche pour android, mais en bas à gauche pour OpenGL
             on doit donc transformer la coordonée Y provenant de la surfaveView Android
             vers un Y Opengl.

             Pour cela, il suffit de faire (Taille de l'écran - position du toucher
             Ainsi, si le touché est en bas à 1024 et que l'écran fait 1024, Alors
             pour opengl le Y serra 1024-1024=0, donc bien en bas de l'écran pour OpenGL.


             ecran  barre  Surface  OpenGl
             |1     |
             |2            |1       |5     <-- le 1 de la surface n'est plus en  haut de l'écran. la barre occupe 1
             |3            |2       |4
             |4            |3       |3     <- on touche 4, ce qui correspond à (4-taille barre) sur la surface = 4-1 = 3
             |5            |4       |2        du coup la coordonnée opengl est
             |6            |5       |1        taille de la surface - coordonnée Y ajusté = 5-3 = 2

             */

            //on calcule la différence entre le haut de l'écran et le haut de la surface
            float offset = this.getScene().getHeightDevice() - this.getScene().getActivity().mGLSurfaceView.getHeight();

            //on calcule la coordonne Y ajustée
            float adjustedY = this.getScene().getActivity().mGLSurfaceView.touchY + offset;

            //on calcule la coordonne Y Opengl
            float openGL_Y = this.getScene().getHeightDevice() - adjustedY;

            // on met à jour les coordonées
            this.setCoord(
                    this.getScene().getActivity().mGLSurfaceView.touchX, openGL_Y);

            //this.histoX = this.getScene().getActivity().mGLSurfaceView.histoX;
            //this.histoY = this.getScene().getActivity().mGLSurfaceView.histoY;

            // on active les colissions pour cette frame
            this.enableCollisionChecking();
            //  Log.e("userfinger", "colision enabled");
            //getWorldCoord();


            // Log.i("UserFinger", String.valueOf(this.X) + "/" + String.valueOf(this.Y));
            /**
             * Log.i("UserFinger World", String.valueOf(this.worldX) + "/" +
             * String.valueOf(this.worldY)); Log.i("--",
             * "----------------------------------------------------------------"
             * );
             */
        }

    }

    public float[] getHomogenicCoord() {
        float[] homCoord = new float[4];

        homCoord[0] = (2.0f * this.getX()) / this.getScene().getWidth() - 1.0f;
        homCoord[1] = (2.0f * this.getY()) / this.getScene().getHeight() - 1.0f;
        homCoord[2] = 1.0f;
        homCoord[3] = 1.0f;
        return homCoord;
    }

    // pour retourver les coordonées du pointeur dans le monde, il faut faire le
    // cheminement inverse
    // ecran -> coorconées homogènes -> coordonée projection -> coordonées model
    public void getWorldCoord() {
        float[] projCoord = new float[4];
        float[] viewCoord = new float[4];

        float[] reverseProjectionView = new float[16];
        float[] reverseVMatrix = new float[16];


        Matrix.invertM(reverseProjectionView, 0, this.getScene()
                .getProjectionView(), 0);
        Matrix.invertM(reverseVMatrix, 0, this.getScene().mVMatrix, 0);

        // je suis obligé de passer par un vecteur 4 pour la multiplication
        // calcul du point placé sur le premier plan de clipping

        // les coordonnée du pointeur doivent être normalisée
        // attention les coordonée ANDROID sont inversées par rapport à OpenGL
        // le 0,0 est en haut a gauche pour android tandis qu'il est en bas à
        // droite pour GL

        Matrix.multiplyMV(projCoord, 0, reverseProjectionView, 0,
                this.getHomogenicCoord(), 0);

        Matrix.multiplyMV(viewCoord, 0, reverseVMatrix, 0, projCoord, 0);

        // on divise par W pour revenir en coordonées World
        // le W correspond à la mise en perspective effectuée par la matrice de
        // projection.
        // comme on a utilisé la matrice inverse on a inversé aussi la
        // perspective. du coup il faut la remettre dans le bon sens.
        viewCoord[0] = viewCoord[0] / viewCoord[3];
        viewCoord[1] = viewCoord[1] / viewCoord[3];
        viewCoord[2] = viewCoord[2] / viewCoord[3];

        this.worldX = viewCoord[0];
        this.worldY = viewCoord[1];

    }

}
