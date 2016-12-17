package com.familledupuis91.gamingtools.components;

import java.util.ArrayList;

/**
 * Created by rodol on 14/12/2015.
 */
public class GroupOfGameObject extends GameObject implements Composition {

    public ArrayList<Composition> getList() {
        return mList;
    }

    public void setList(ArrayList<Composition> list) {
        this.mList = list;
    }

    private ArrayList<Composition> mList;

    public void add(GameObject gameObject) {
          this.mList.add(gameObject);
    }

    public void add(int index, GameObject gameObject) {
        this.mList.add(index, gameObject);
    }

    @Override
    public ArrayList<Composition> getComponent() {
        ArrayList<Composition> result = new ArrayList<Composition>();

        for (Composition composition : this.mList) {
            result.addAll(composition.getComponent());
        }
        return result;


    }

    @Override
    public void setX(float x) {
        //calcul du déplacement à éffectuer en X
        float offsetX = x - this.getX();
        //on applique le même déplacement à tous les composants
        this.updateX(offsetX);
        //on mémorise la nouvelle valeur de X
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        float offsetY = y - this.getY();
        this.updateY(offsetY);
        super.setY(y);
    }

    @Override
    public void setAngleRADZ(float radz) {
        float offsetRadZ = radz - this.getAngleRADZ();
        this.updateAngleZ(offsetRadZ);
        super.setY(radz);
    }


    @Override
    public void setAlpha(float alpha) {
        float ratio = alpha / getAlpha();
        this.updateAlpha(ratio);
        super.setAlpha(alpha);
    }

    @Override
    public void setAmbiantColor(ColorRGBA color) {
        this.updateRGB(color);
        super.setAmbiantColor(color);
    }


    /****************************************************************
     * Getter & setter
     ***************************************************************/
    public ArrayList<GameObject> getGameObjectsList() {
        ArrayList<GameObject> result = new ArrayList<GameObject>();
        for (Composition composition : this.mList) {
            GameObject gameObject = (GameObject) composition;
            result.add(gameObject);
        }
        return result;
    }


    @Override
    public void setIdOnScene(Scene scene){
       //on met à jour l'ID de l'objet "Groupe"
        super.setIdOnScene(scene);
        //on met à jour les ID des composants
               for (GameObject gameObject : this.getGameObjectsList()) {
            gameObject.setIdOnScene(scene);

        }
    }
    /**
     * public void setShapeList(ArrayList<Component> shapeList) {
     * this.mShapeList = shapeList;
     * }
     */

    @Override
    public void setHeight(float height) {
        float ratio = height / this.getHeight();
        updateHeight(ratio);
        super.setHeight(height);
    }

    @Override
    public void setWidth(float width) {
        float ratio = width / this.getWidth();
        //on applique le ratio a tous les enfants
        this.updateWidth(ratio);
        //on mémorise la nouvelle valeur
        super.setWidth(width);
    }


    /******************************************************************
     * Constructeur
     ***************************************************************/
    public GroupOfGameObject() {
        this.mList = new ArrayList<Composition>();
        this.setWidth(1);
        this.setHeight(1);
        this.setVisibility(true);
        this.setAlpha(1.f);
    }

    @Override
    public void update() {
        for (GameObject gameObject : this.getGameObjectsList()) {
            gameObject.update();
        }
    }


    public void updateX(float offsetX) {
        //TODO pur chaque objet sur X du même écart

        for (GameObject gameObject : this.getGameObjectsList()) {
            float newX = gameObject.getX() + offsetX;
            gameObject.setX(newX);
        }
    }


    public void updateY(float offsetY) {
        //TODO pur chaque objet on applique le ratio necessaire

        for (GameObject gameObject : this.getGameObjectsList()) {
            float newY = gameObject.getY() + offsetY;
            gameObject.setY(newY);
        }
    }


    public void updateAngleZ(float offsetRadZ) {
        //TODO pur chaque objet on applique le ratio necessaire

        for (GameObject gameObject : this.getGameObjectsList()) {
            float radZ = gameObject.getAngleRADZ() + offsetRadZ;
            gameObject.setAngleRADZ(radZ);
        }
    }


    public void updateWidth(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire

        for (GameObject gameObject : this.getGameObjectsList()) {
            float newWidth = gameObject.getWidth() * ratio;
            gameObject.setWidth(newWidth);
        }
    }

    public void updateHeight(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire
        for (GameObject gameObject : this.getGameObjectsList()) {
            float newHeight = gameObject.getHeight() * ratio;
            gameObject.setHeight(newHeight);
        }
    }

    public void updateAlpha(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire
        //     Log.e("GrpOfGameobj:updAlpha:", String.valueOf(ratio));
        for (GameObject gameObject : this.getGameObjectsList()) {
            float newAlpha = gameObject.getAlpha() * ratio;
            //       Log.e("GrpOfGameobj:getAlpha:", String.valueOf(gameObject.getAlpha()));
            gameObject.setAlpha(newAlpha);

        }
    }

    @Override
    public void setScene(Scene scene) {
        //TODO pur chaque objet on applique le ratio necessaire
        for (GameObject gameObject : this.getGameObjectsList()) {
            gameObject.setScene(scene);
        }
        super.setScene(scene);
    }


    public void updateModelView() {
        for (GameObject gameObject : this.getGameObjectsList()) {
            gameObject.updateModelView();
        }
    }


    public void updateRGB(ColorRGBA color) {
        for (GameObject gameObject : this.getGameObjectsList()) {

            float newcolor = gameObject.getAmbiantColor().getRed() + this.getAmbiantColor().getRed();
            gameObject.getAmbiantColor().setRed(newcolor);

            float newGreen = gameObject.getAmbiantColor().getGreen() + this.getAmbiantColor().getGreen();
            gameObject.getAmbiantColor().setGreen(newGreen);

            float newBlue = gameObject.getAmbiantColor().getBlue() + this.getAmbiantColor().getBlue();
            gameObject.getAmbiantColor().setGreen(newBlue);


        }
    }


    public Composition getComponentById(Long searchedId) {

        Composition result = null;

        for (Composition composition : this.mList) {

            if (composition.getIdOnScene() == searchedId) {
                result = composition;
            }
        }
        return result;


    }
}


