package com.familledupuis91.gamingtools.components;

import com.familledupuis91.gamingtools.components.shapes.Shape;

import java.util.ArrayList;


public class GameObject implements Composition {

    //Tag de l'objet
    private String mTagName;

    //Id de l'objet dans la scène
    private long mIdOnScene;

    public long getIdOnScene() {
        return mIdOnScene;
    }

    public void setIdOnScene(Scene scene) {
        this.mIdOnScene = scene.generateGameObjectId();
    }

    //top pour activer/désactiver le rendu de l'objet
    public Boolean mVisibility;

    //scène auquel appartient l'objet
    private Scene mScene;

    //Couleur ambiante de l'objet R,G,B,A
    private ColorRGBA ambiantColor = new ColorRGBA();


    // coordonnées du centre de l'objet
    private float X = 0;
    private float Y = 0;
    private float Z = 0;

    //Taille de l'objet
    private float width;
    private float height;

    //liste des objets à écouter
    public ArrayList<Shape> mShapeToListenList;

    private float angleRADX = 0.0f;

    public float getAngleRADX() {
        return angleRADX;
    }

    public void setAngleRADX(float angleRADX) {
        this.angleRADX = angleRADX;
    }

    private float angleRADY = 0.0f;

    public float getAngleRADY() {
        return angleRADY;
    }

    public void setAngleRADY(float angleRADY) {
        this.angleRADY = angleRADY;
    }

    private float angleRADZ = 0.0f;

    public float getAngleRADZ() {
        return angleRADZ;
    }

    public void setAngleRADZ(float angleRADZ) {
        this.angleRADZ = angleRADZ;
    }



    /******************************************************************
     * getter & setter
     ****************************************************************/

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {

        this.width = width;
    }

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }

    public Scene getScene() {
        return this.mScene;
    }

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String tagid) {
        mTagName = tagid;
    }

    public boolean getVisibility() {
        return mVisibility;
    }

    public void setVisibility(Boolean mVisibility) {
        this.mVisibility = mVisibility;
    }

    public ColorRGBA getAmbiantColor() {
        return ambiantColor;
    }

    public float[] getRGBA() {
        float[] result = new float[4];
        result[0] = this.getAmbiantColor().getRed();
        result[1] = this.getAmbiantColor().getGreen();
        result[2] = this.getAmbiantColor().getBlue();
        result[3] = this.getAmbiantColor().getAlpha();


        return result;
    }

    public void setAmbiantColor(ColorRGBA ambiantColor) {
        this.ambiantColor = ambiantColor;
    }

    public float getAlpha() {
        return this.getAmbiantColor().getAlpha();
    }

    public void setCoord(float x, float y) {
        this.X = x;
        this.Y = y;
    }

    /**
     * @param x
     * @param y
     * @param z
     */
    public void setCoord(float x, float y, float z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        this.X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        this.Y = y;
    }

    public void setZ(float z) {
        this.Z = z;
    }

    public float getZ() {
        return Z;
    }

    /**
     * @return
     */
    public ArrayList<Shape> getGameObjectToListenList() {
        return this.mShapeToListenList;
    }


    /********************************************************************
     * Constructeur
     *******************************************************************/
    public GameObject() {

        //visible par défaut
        setVisibility(true);

    }


    public void updateModelView() {

    }


    /**
     * getter & setter
     *
     * @return
     */


    public void update() {

    }


    public ArrayList<Composition> getComponent() {
        ArrayList<Composition> result = new ArrayList<Composition>();
        result.add(this);
        return result;
    }


    /**
     * @return
     * @throws CloneNotSupportedException
     */
    public Shape clone() throws CloneNotSupportedException {
        Shape gameobject = (Shape) super.clone();

        gameobject.mShapeToListenList = new ArrayList<Shape>();


        // on r�initialise le lien de parent� avec l'animation
        /**
         if (gameobject.getAnimation() != null) {
         Animation anim = (Animation) gameobject.getAnimation().clone();

         anim.setParent(gameobject);

         gameobject.setAnimation(anim);
         }

         // si l'objet source peu entrer en collision on
         // red�fini un nouvelle boite de colision pour la cible
         // sinon elle va avoir la m�me que la source
         if (gameobject.canCollide) {
         gameobject.enableColision();

         }
         */
        return gameobject;
    }

    public void setAlpha(float alpha) {

        this.getAmbiantColor().setAlpha(alpha);
    }


    /**
     * Mise à jour de la ModelView pour prendre en compte les
     * modification apportées à l'ojet
     * taille - position - rotation
     * <p/>
     * /!\ l'ordre où on applique les transformation et hyper important
     * il faut toujours faire : translation*rotation*scale
     */


}
