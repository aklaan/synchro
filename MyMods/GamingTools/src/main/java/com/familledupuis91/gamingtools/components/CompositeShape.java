package com.familledupuis91.gamingtools.components;

import com.familledupuis91.gamingtools.components.shapes.Shape;

import java.util.ArrayList;

/**
 * Created by rodol on 14/12/2015.
 */
public class CompositeShape extends GameObject {

    private ArrayList<Shape> mShapeList;

    @Override
    public void setX(float x) {
        //calcul du déplacement à éffectuer en X
        float offsetX = this.getX() - x;
        //on applique le même déplacement à tous les composants
        this.updateX(offsetX);
        //on mémorise la nouvelle valeur de X
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        float offsetY = this.getY() - y;
        this.updateY(offsetY);
        super.setY(y);
    }

    public void setAlpha(float alpha) {
        float ratio = alpha / getAlpha();
        this.updateAlpha(ratio);
        this.getAmbiantColor().setAlpha(alpha);
    }

    @Override
    public void setAmbiantColor(ColorRGBA color) {
        this.updateRGB(color);
        super.setAmbiantColor(color);
    }


    /****************************************************************
     * Getter & setter
     ***************************************************************/
    public ArrayList<Shape> getShapeList() {
        return mShapeList;
    }

    public void setShapeList(ArrayList<Shape> shapeList) {
        this.mShapeList = shapeList;
    }

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
    public CompositeShape() {
        this.setShapeList(new ArrayList<Shape>());
        this.setWidth(10);
        this.setHeight(10);
        this.setVisibility(true);
        this.setAlpha(1.f);
    }


    public void update() {
        for (Shape shape : this.getShapeList()) {
            shape.update();
        }
    }


    public void updateX(float offsetX) {
        //TODO pur chaque objet sur X du même écart

        for (Shape shape : this.getShapeList()) {
            float newX = shape.getX() + offsetX;
            shape.setX(newX);
        }
    }


    public void updateY(float offsetY) {
        //TODO pur chaque objet on applique le ratio necessaire

        for (Shape shape : this.getShapeList()) {
            float newY = shape.getX() + offsetY;
            shape.setY(newY);
        }
    }

    public void updateWidth(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire

        for (Shape shape : this.getShapeList()) {
            float newWidth = shape.getWidth() * ratio;
            shape.setWidth(newWidth);
        }
    }

    public void updateHeight(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire
        for (Shape shape : this.getShapeList()) {
            float newHeight = shape.getHeight() * ratio;
            shape.setWidth(newHeight);
        }
    }

    public void updateAlpha(float ratio) {
        //TODO pur chaque objet on applique le ratio necessaire
        for (Shape shape : this.getShapeList()) {
            float newAlpha = shape.getAlpha() * ratio;
            shape.setAlpha(newAlpha);
        }
    }


    public void updateModelView() {
        for (Shape shape : this.getShapeList()) {
            shape.updateModelView();
        }
    }



    public void updateRGB(ColorRGBA color) {
        for (Shape shape : this.getShapeList()) {

            float newcolor = shape.getAmbiantColor().getRed() + this.getAmbiantColor().getRed();
            shape.getAmbiantColor().setRed(newcolor);

            float newGreen = shape.getAmbiantColor().getGreen() + this.getAmbiantColor().getGreen();
            shape.getAmbiantColor().setGreen(newGreen);

            float newBlue = shape.getAmbiantColor().getBlue() + this.getAmbiantColor().getBlue();
            shape.getAmbiantColor().setGreen(newBlue);


        }
    }


}
