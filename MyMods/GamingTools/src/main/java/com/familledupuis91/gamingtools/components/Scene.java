package com.familledupuis91.gamingtools.components;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;

import com.familledupuis91.gamingtools.animations.AnimationManager;
import com.familledupuis91.gamingtools.components.fonts.FontConsolas;
import com.familledupuis91.gamingtools.components.fonts.GlFont;
import com.familledupuis91.gamingtools.components.shapes.Shape;
import com.familledupuis91.gamingtools.components.texture.Texture;
import com.familledupuis91.gamingtools.inputs.UserFinger;
import com.familledupuis91.gamingtools.providers.GameObjectManager;
import com.familledupuis91.gamingtools.shaders.ProgramShader;
import com.familledupuis91.gamingtools.shaders.ProgramShaderManager;
import com.familledupuis91.gamingtools.components.texture.TextureManager;
import com.familledupuis91.gamingtools.components.physics.ColliderManager;
import com.familledupuis91.gamingtools.shaders.ProgramShader_forLines;
import com.familledupuis91.gamingtools.shaders.ProgramShader_noTexture;
import com.familledupuis91.gamingtools.shaders.ProgramShader_simple;


import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Scene implements GLSurfaceView.Renderer {

    private int moffsetX = 0;
    private int moffsetY = 0;
    public static final String BUNDLE_FPS_VALUE = "BUNDLE_FPS";
    private long startDrawingTime = 0;
    private long startFpsTimeCounting=0;
    public enum VIEW_MODE {ORTHO, CAMERA}

    private long gameObjectIdCounter=0;
    public final static String TAG_ERROR = "CRITICAL ERROR";

    private VIEW_MODE mViewMode;
    private OpenGLActivity mActivity;


    private ColliderManager mColliderManager;
    private TextureManager mTextureManager;
    private AnimationManager animationManager;
    private GameObjectManager mGameObjectManager;
    private ProgramShaderManager mProgramShaderManager;

    private ArrayList<GlFont> glFontsList;

    public final float[] mVMatrix = new float[16];
    public final float[] mVMatrixORTH = new float[16];

    // Matrice de projection de la vue
    public float[] mProjectionView = new float[16];

    // matrice de projection orthogonale
    public float[] mProjectionORTH = new float[16];

    // public ShaderProvider mShaderProvider;
    private int frameCounter;
    public Camera mCamera;

    /**
     * @return
     */
    public TextureManager getTexManager() {
        return mTextureManager;
    }

    /**
     * @param mTextureManager
     */
    public void setTexManager(TextureManager mTextureManager) {
        this.mTextureManager = mTextureManager;
    }

    /**
     * @return
     */
    public GameObjectManager getGOManager() {
        return mGameObjectManager;
    }

    /**
     * @param mGameObjectManager
     */
    public void setGOManager(GameObjectManager mGameObjectManager) {
        this.mGameObjectManager = mGameObjectManager;
    }

    /**
     * @return
     */
    public ProgramShaderManager getPSManager() {
        return mProgramShaderManager;
    }

    /**
     * @param mProgramShaderManager
     */
    public void setPSManager(ProgramShaderManager mProgramShaderManager) {
        this.mProgramShaderManager = mProgramShaderManager;
    }

    /**
     * @param animationManager
     */
    public void setAnimationManager(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    /**
     * @return
     */
    public VIEW_MODE getViewMode() {
        return mViewMode;
    }

    /**
     * @param mViewMode
     */
    public void setViewMode(VIEW_MODE mViewMode) {
        this.mViewMode = mViewMode;
    }

    /**
     * @return
     */
    public AnimationManager getAnimationManager() {
        return this.animationManager;
    }

    /**
     * @return
     */
    public OpenGLActivity getActivity() {
        return this.mActivity;
    }

    public ColliderManager getColliderManager() {
        return mColliderManager;
    }

    public void setColliderManager(ColliderManager colliderManager) {
        this.mColliderManager = colliderManager;
    }

    /**
     * Constructeur
     *
     * @param activity
     */
    public Scene(OpenGLActivity activity) {

        this.mActivity = activity;
        //par defaut on est en mode de vue Orthogonale
        this.setViewMode(VIEW_MODE.ORTHO);

        setTexManager(new TextureManager(activity));
        setPSManager(new ProgramShaderManager());
        setAnimationManager(new AnimationManager());
        setGOManager(new GameObjectManager(this));
        setColliderManager(new ColliderManager());

        glFontsList = new ArrayList<GlFont>();

        this.mCamera = new Camera();
        this.mCamera.centerZ = 100;
        this.frameCounter = 0;

        this.preLoading();

        UserFinger userFinger = new UserFinger();

        // TODO : il faut sortir ce composant de la liste des objets pour
        // TODO : en faire un contrôleur
        this.addToScene(userFinger);

    }

public long generateGameObjectId(){
    this.gameObjectIdCounter++;
    return this.gameObjectIdCounter;
}
    /**
     * on préchage les éléménts qui ne nécéssitent pas d'avoir un contexte openGl
     * de créé.
     */
    private void preLoading() {

        initializeFont(new FontConsolas());
        // on charge les textures necessaires à la scène
        loadTextures();
        // on initialise la liste des objets qui serront contenus dans
        // la scène.
        loadGameObjects();
    }


    public void initializeFont(GlFont glFont) {
        //on charge la texture de la police d'écriture dans la liste des texture de la scène
        // et on en profite pour initialiser la texture de la font qui est static

        //on ajoute la font dans la liste des fonts de la scène
        glFontsList.add(glFont);
        //on mémorise la MAP de la font
        glFont.setMap(this.getTexManager().add(glFont.getMapPathId()));
        //on mémorise les données xml pour les coordonées de texture
        try {

            glFont.setXmlData(this.getActivity().getAssets().open(
                    this.getActivity().getString(glFont.getXmlDataPathId())));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param gl2
     * @param eglConfig
     */
    @Override
    public void onSurfaceCreated(GL10 gl2, EGLConfig eglConfig) {


        // on ne peux pas créer de programe Shader en dehors du contexte
        // opengl. donc le provider est à recréer à chaque fois que l'on contruit la scène
        // c'est à dire : au démarrage et à chaque fois que l'on incline l'écran

        loadProgramShader();

        //chargement des textures dans le contexte OpenGl
        this.getTexManager().initializeGLContext();

        //chargement des objets le contexte OpenGl
        this.getGOManager().initializeGLContext();

        //on initialise les boites de colision
        this.getColliderManager().initBoxes(this.getGOManager().getCollidable());

        // on défini la couleur de base pour initialiser le BUFFER de rendu
        // a chaque Frame, lorsque l'on fera un appel GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // on va remplir le back buffer avec la couleur pré-définie ici
        GLES20.glClearColor(1.0f, .5f, .5f, 1.0f);

        // Activattion de la gestion de l'Alpha
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        // spécifer l'orientation de la détermination des face avant/arrière
        // par dédaut c'est CCW (counterClockWiwe) l'inverse des aiguilles d'une
        // montre
        GLES20.glFrontFace(GL10.GL_CCW);


        // Activation du Culling
        // nb : je l'ai désactivé ici car on ne s'en sert pas
        //      c'est ça en moins à calculer donc c'est un gain de perf.
        //  GLES20.glEnable(GL10.GL_CULL_FACE);

        // on indique quelle face à oculter (par défaut c'est BACK)
        // GLES20.glCullFace(GL10.GL_BACK);

        // Taille des lignes pour l'affichage en mode GL_LINES
        GLES20.glLineWidth(1.f);


    }

    // @Override
    public void onSurfaceChanged(GL10 gl, int width, int hight) {
        // lorsqu'il y a une modification de la vue , on redéfinie la nouvelle
        // taille de la vue (par exemple quand on incline le téléphone et
        // que l'on passe de la vue portait à la vue paysage


        // le coin en bas à gauche est 0,0
        // la taille de la surface est la même que l'écran

        GLES20.glViewport(0, 0, width, hight);

        float ratio = (float) width / hight;
        // * pour un affichage Perspective *********************
        // le premier plan de clipping NEAR est défini par
        // 2 points : le point du bas à gauche et le point du haut à droite
        // le point du bas à gauche est à -ratio, -1
        // le point du haut à gauche est à ratio, 1
        // le plan de clipping NEAR est à 1 et le second plan est à 1000.

        /** avec cette version le centre est au milieu de l'écran */
        Matrix.frustumM(mProjectionView, 0, -ratio, ratio, -1, 1, 1, 1000);

        /** avec cette version le centre est en bas à gauche de l'écran */
        // Matrix.frustumM(mProjectionView, 0, 0, ratio, 0, 1, 1, 1000);

        // Set the camera position (View matrix)
        // le centre de la cam�ra est en 0,0,-200 (oeuil)
        // la cam�ra regarde le centre de l'�cran 0,0,0
        // le vecteur UP indique l'orientation de la cam�ra (on peu tourner la
        // cam�ra)
        // Matrix.setLookAtM(rm, rmOffset, eyeX, eyeY, eyeZ, centerX, centerY,
        // centerZ, upX, upY, upZ)

        // attentios, si le z est n�gatif, la cam�ra se retrouve derri�re l'axe
        // et donc le X est invers�
        Matrix.setLookAtM(mVMatrix, 0, mCamera.centerX, mCamera.centerY, mCamera.centerZ,
                mCamera.eyeX, mCamera.eyeY, mCamera.eyeZ,
                mCamera.orientX, mCamera.orientY, mCamera.orientZ);

        // * pour un affichage Orthogonal *********************
        // le point bas (0,0) du rectangle est en bas � gauche.
        // on définit le point haut à "width","height"
        // on définit la profondeur de rendu à +1 devant et -1 derrière
        //

        Matrix.orthoM(mProjectionORTH, 0, -0, width, 0, hight, -1.f, 1.f);

        Matrix.setIdentityM(mVMatrixORTH, 0);

    }

    public float[] getProjectionView() {
        if (this.getViewMode() == Scene.VIEW_MODE.ORTHO) {
            return this.mProjectionORTH;
        }
        return this.mProjectionView;

    }


    @Override
    public void onDrawFrame(GL10 gl) {

        /*********************************************************************************
         * Début du cycle de rendu
         ********************************************************************************/
        //on incrémente le compteur de frame traité
        frameCounter ++;

        if ((startFpsTimeCounting-SystemClock.elapsedRealtime() ) < -1000) {
            Message completeMessage =
                    this.getActivity().getFpsHandler().obtainMessage(OpenGLActivity.UPDATE_FPS);
            Bundle bundle = new Bundle();
            bundle.putInt(Scene.BUNDLE_FPS_VALUE, frameCounter);
            completeMessage.setData(bundle);
            completeMessage.sendToTarget();
            startFpsTimeCounting = SystemClock.elapsedRealtime();
           frameCounter = 0;
        }


        //on mémorise le moment où on commence le cycle
        float startDrawingTime = SystemClock.currentThreadTimeMillis();

        // on vide le buffer de rendu en remplacant tout le contenu par la couleur
        // qui a été prédéfinie.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        /***************************************************************************************
         * Mise à jour de la Matrice de projection en fonction de la position de la caméra
         *************************************************************************************/
        if (this.getViewMode() == VIEW_MODE.CAMERA) {
            Matrix.setLookAtM(mVMatrix, 0, mCamera.centerX, mCamera.centerY, mCamera.centerZ,
                    mCamera.eyeX, mCamera.eyeY, mCamera.eyeZ,
                    mCamera.orientX, mCamera.orientY, mCamera.orientZ);
        }


           //-----------------------------------
        switch (this.getActivity().getSurfaceView().getScreenEvent()) {

            case SCROLL_H_RIGHT:
                moffsetX = +20;
                break;

            case SCROLL_H_LEFT:
                moffsetX = -20;
                break;


            case SCROLL_V_UP:
                moffsetY = +20;
                break;

            case SCROLL_V_DOWN:
                moffsetY = -20;
                break;

            default:
                break;

        }

        Matrix.orthoM(mProjectionORTH, 0-moffsetY, -0-moffsetX, this.getWidth()-moffsetX, 0, this.getHeight()-moffsetY, -1.f, 1.f);

        //-----------------------------------




        //si on ne touche plus l'écran, on désactive la colision du touché
        if (this.getActivity().getSurfaceView().getScreenEvent() == MySurfaceView.ScreenEvent.UNKNOWN) {
            UserFinger uf = (UserFinger) this.getGOManager().getGameObjectByTag(UserFinger.USER_FINGER_TAG);
            uf.disableCollisionChecking();
            //on met à jour les colissions
            this.getColliderManager().updateCollisionsList();
        }

        /** on check les colissions entre tous les éléments de la scène
         * =============================================================================
         * lorsque l'on traite la première frame tous les éléments sont en 0,0
         * car on est pas encore passé dans les initialisations de positions
         * du coup tout le monde est en colision.
         * pour éviter le problème, on ne chek pas les colissions sur la première Frame
         */

        //on check les colissions toustes les 4 frames pour économiser de la CPU
        //if (frameCounter == 0 || frameCounter > 0) {
            this.getColliderManager().updateCollisionsList();
        //    frameCounter = 0;
        //}
      //frameCounter++;

        /** Mise à jour des objets*/
        this.getGOManager().update();
        //  Log.i("scene", "update des GO");

        /** jouer les animations si elles éxistent */
        this.animationManager.playAnimations();


        /**
         * Rendu de la scène*/
        this.getPSManager().renderScene(this);

        /****************************************************************************
         * Pour une Animation fluide, 60 FPS sont sufisants
         * si on va plus vite, on temporise le prochain cycle de rendu
         * pour éviter de surcharger le GPU et le CPU
         */

        //Calcul du temps écoulé pour rendre la Frame
        float drawTimeElaps = SystemClock.currentThreadTimeMillis() - startDrawingTime;

        //Si le temps de rendu est inférieur à 1/60 de seconde
        //on attend le temps restant à faire pour atteindre les 60 FPS
        if (drawTimeElaps < (16)) {
            SystemClock.sleep((long) ((16) - drawTimeElaps));
        }

        /**
         * Fin du Cycle de rendu
         * on passe firstFrame à faux pour qu'au pochain cycle, on prenne en compte les colisions
         */
//        firstFrame = false;
    }

    /**
     * fonction où on charge les objets de la scène dans la phase de Loading
     * game
     */
    //TODO : rendre des fonction obligatoire en override en passant pas une classe abstraite
    public void loadGameObjects() {
    }

    /**
     * Initialisation des program Shader
     */
    public void loadProgramShader() {

        //chargement des shader de base
        this.getPSManager().catalogShader.clear();
        this.getPSManager().shaderList.clear();
        ProgramShader ps = new ProgramShader_simple();
        this.getPSManager().add(ps);

        this.getPSManager().add(new ProgramShader_noTexture());
        this.getPSManager().add(new ProgramShader_forLines());

        //on défini le simple comme shader par defaut.
        this.getPSManager().setDefaultSader(ps);

    }

    /**
     * Chargement des Textures
     */
    public void loadTextures() {

    }

    /**
     * Ajouter un Shape dans la scène
     *
     * @param gameobject
     */
    public void addToScene(GameObject gameobject) {
        this.getGOManager().add(gameobject);

    }

    /**
     * Ajouter une texture dans la scène
     *
     * @param ressourceID
     */
    public void addTexture(int ressourceID) {
        this.getTexManager().add(ressourceID);

    }

    /**
     * Ajouter une texture dans la scène
     *
     * @param ressourceID
     */
    public Texture getTexture(int ressourceID) {
      return this.getTexManager().getTextureById(ressourceID);
    }

    /**
     * Retirer un Shape de la scène
     *
     * @param gameobject
     */
    public void removeFromScene(Shape gameobject) {
        gameobject.setScene(null);
        this.getGOManager().remove(gameobject);

    }


    /**
     * Récupérer l'Input UserFinger
     *
     * @return
     */
    public UserFinger getUserFinger() {

        return (UserFinger) this.getGOManager().getGameObjectByTag(UserFinger.USER_FINGER_TAG);
    }

    /**
     * Récupérer la Hauteur de la scène
     * nb : on récupère la hauteur de la surface et non celle de l'écran
     *
     * @return
     */

    public int getHeight() {
        DisplayMetrics metrics = this.getActivity().getResources()
                .getDisplayMetrics();
        return metrics.heightPixels;
    }

    public int getHeightDevice() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.heightPixels;
    }


    /**
     * Récupérer la largeur de la scène
     *
     * @return
     */
    public int getWidth() {
        DisplayMetrics metrics = this.getActivity().getResources()
                .getDisplayMetrics();
        return metrics.widthPixels;
    }


    private void updateCamera() {
        /**
         * ici on joue sur les évènements déterminés par la SurfaceView pour
         * contrôler la position de la caméra
         */
        float incX = 0;
        float incY = 0;

        switch (this.getActivity().getSurfaceView().getScreenEvent()) {

            case SCROLL_H_RIGHT:
                Log.e("screenevent","H_right");

                incX = +2f;
                break;

            case SCROLL_H_LEFT:
                Log.e("screenevent","H_left");

                incX = -2f;
                break;


            case SCROLL_V_UP:
                Log.e("screenevent","v_up");

                incY = +2f;
                break;

            case SCROLL_V_DOWN:
                Log.e("screenevent","v_down");

                incY = -2f;
                break;

            default:
                break;

        }

        float limitX_RIGHT = +80;
        float limitX_LEFT = -80;

        this.mCamera.centerX += incX;
        this.mCamera.centerX = (this.mCamera.centerX > limitX_RIGHT) ? limitX_RIGHT : this.mCamera.centerX;
        this.mCamera.centerX = (this.mCamera.centerX < limitX_LEFT) ? limitX_LEFT : this.mCamera.centerX;
        this.mCamera.eyeX = this.mCamera.centerX;

        float limitY_UP = +100;
        float limitY_DOWN = -100;

        this.mCamera.centerY = (this.mCamera.centerY > limitY_UP) ? limitY_UP : this.mCamera.centerY;
        this.mCamera.centerY = (this.mCamera.centerY < limitY_DOWN) ? limitY_DOWN : this.mCamera.centerY;


        this.mCamera.centerY += incY;
        this.mCamera.eyeY = this.mCamera.centerY;


    }
}
