package com.familledupuis91.gamingtools.components.numericpad;

import android.os.Bundle;

import com.familledupuis91.gamingtools.components.GameObject;
import com.familledupuis91.gamingtools.components.GroupOfGameObject;
import com.familledupuis91.gamingtools.components.Scene;
import com.familledupuis91.gamingtools.components.button.ButtonWithText;
import com.familledupuis91.gamingtools.components.button.GLButtonListener;
import com.familledupuis91.gamingtools.components.fonts.GlFont;
import com.familledupuis91.gamingtools.components.texture.Texture;
import com.familledupuis91.gamingtools.enums.NumericPadKey;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Created by rodol on 04/02/2016.
 */
public class GLNumericPadForDate extends GroupOfGameObject {

    private final ArrayList<GLNumericPadListener> eventListenerList = new ArrayList<GLNumericPadListener>();
    public static String KEY_PRESSED = "KEY";

    private final static String TAG_NUMPAD = "NUMPAD:";
    private Texture mTextureUP;
    private Texture mTextureDown;
    private GlFont mGlFont;
    public EnumMap<NumericPadKey, Long> objectIdList;


    //pavé numérique dédié à la saisie des date
    //on affiche uniquement des chiffres possibles

    public GLNumericPadForDate(float x, float y, float width, float height, Texture texUp, Texture texDown, GlFont glFont) {
        super();
        //initialisation de base pour le clavier
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.mGlFont = glFont;
        this.mTextureDown = texDown;
        this.mTextureUP = texUp;

        //Initialisation de la liste des id des objets qui vont composer le clavier
        this.objectIdList = new EnumMap<NumericPadKey, Long>(NumericPadKey.class);

        //initialisation de la position de la première touche
        float spaceX = this.getX();
        float spaceY = this.getY();

        float button_size = width / 3;

        //première ligne du bas
        addOkButton(spaceX, spaceY, button_size);

        //seconde ligne
        spaceY += button_size;
        this.addCharKey(0, spaceX, spaceY, button_size);

        spaceX += button_size;
        addDotButton(spaceX, spaceY, button_size);

        spaceX += button_size;
        addClearButton(spaceX, spaceY, button_size);

        //seconde ligne
        spaceX = this.getX();
        spaceY += button_size;
        for (int i = 1; i < 4; i++) {
            this.addCharKey(i, spaceX, spaceY, button_size);
            spaceX += button_size;
        }

        //Troisième  ligne
        spaceX = this.getX();
        spaceY += button_size;
        for (int i = 4; i < 7; i++) {
            this.addCharKey(i, spaceX, spaceY, button_size);
            spaceX += button_size;
        }

        //Quatrième ligne
        spaceX = this.getX();
        spaceY += button_size;
        for (int i = 7; i < 10; i++) {
            this.addCharKey(i, spaceX, spaceY, button_size);
            spaceX += button_size;
        }


    }


    public void addGLNumericPadListener(GLNumericPadListener glNumericPadListener) {
        this.eventListenerList.add(glNumericPadListener);
    }


    private void addCharKey(int value, float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize, buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText(String.valueOf(value));

        switch (value) {
            case 0:
                bt.setTagName(NumericPadKey.ZERO.name());
                break;
            case 1:
                bt.setTagName(NumericPadKey.ONE.name());
                break;
            case 2:
                bt.setTagName(NumericPadKey.TWO.name());
                break;
            case 3:
                bt.setTagName(NumericPadKey.THREE.name());
                break;
            case 4:
                bt.setTagName(NumericPadKey.FOUR.name());
                break;
            case 5:
                bt.setTagName(NumericPadKey.FIVE.name());
                break;
            case 6:
                bt.setTagName(NumericPadKey.SIX.name());
                break;
            case 7:
                bt.setTagName(NumericPadKey.SEVEN.name());
                break;
            case 8:
                bt.setTagName(NumericPadKey.HEIGHT.name());
                break;
            case 9:
                bt.setTagName(NumericPadKey.NINE.name());
                break;
        }

               //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                //je récupère le texte du bouton qui est présent dans le Bundle
                String button_Value = bundle.getString(ButtonWithText.TEXT_VALUE);

                //comme ce texte est censé être seulement une lettre, je ne garde que cette
                //lettre en char et j'appelle le onClick pour avertir
                //tous les listeners avec la lettre en paramètre.
                GLNumericPadForDate.this.onClick(button_Value.charAt(0));
            }

            public void onLongClick() {
                //Nothing to do
            }

        });


        this.add(bt);


    }


    private void addOkButton(float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize * 3, buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText("OK");
        bt.setTagName(NumericPadKey.OK.name());
        //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                GLNumericPadForDate.this.onClickOk();
            }

            public void onLongClick() {
                //Nothing to do
            }

        });


        this.add(bt);


    }


    private void addDotButton(float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize, buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText(".");
        bt.setTagName(NumericPadKey.DOT.name());
        //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                //je récupère le texte du bouton qui est présent dans le Bundle
                String button_Value = bundle.getString(ButtonWithText.TEXT_VALUE);

                //comme ce texte est censé être seulement une lettre, je ne garde que cette
                //lettre en char et j'appelle le onClick pour avertir
                //tous les listeners avec la lettre en paramètre.
                GLNumericPadForDate.this.onClick(button_Value.charAt(0));
            }

            public void onLongClick() {
                //Nothing to do
            }

        });


        this.add(bt);


    }


    /**
     * Création du bouton CLEAR
     *
     * @param spaceX
     * @param spaceY
     * @param buttonSize
     */
    private void addClearButton(float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize, buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText("C");
        bt.setTagName(NumericPadKey.CLEAR.name());
        //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                GLNumericPadForDate.this.onClickClear();

            }

            public void onLongClick() {
                //Nothing to do
            }

        });


        this.add(bt);


    }

    /**
     * pour tous les objets qui écoutent le onClick(), on leur passe
     * l'info
     */
    public void onClick(char value) {
        for (GLNumericPadListener listener : eventListenerList) {
            Bundle bundle = new Bundle();
            bundle.putChar(GLNumericPadForDate.KEY_PRESSED, value);
            listener.onClick(bundle);

        }
    }

    public void onClickOk() {
        for (GLNumericPadListener listener : eventListenerList) {
            listener.onClickOk();

        }
    }

    public void onClickClear() {
        for (GLNumericPadListener listener : eventListenerList) {
            listener.onClickClear();

        }
    }


    private ArrayList<ButtonWithText> getButtonList() {
        ArrayList<ButtonWithText> result = new ArrayList<ButtonWithText>();
        for (GameObject gameObject : this.getGameObjectsList()) {
            if (gameObject instanceof ButtonWithText) {
                ButtonWithText button = (ButtonWithText) gameObject;
                result.add(button);
            }
        }
        return result;
    }


    public ButtonWithText getButton(NumericPadKey keyName) {
        long buttonId = this.objectIdList.get(keyName);
        return (ButtonWithText) this.getComponentById(buttonId);
    }


    @Override
    public void setIdOnScene(Scene scene) {
        super.setIdOnScene(scene);
        initButtonId();
    }


    private void initButtonId() {
        for (ButtonWithText button : this.getButtonList()) {

            NumericPadKey typeOfButton = NumericPadKey.valueOf(button.getTagName());

            switch (typeOfButton) {
                case ZERO:
                    this.objectIdList.put(NumericPadKey.ZERO, button.getIdOnScene());
                    break;
                case ONE:
                    this.objectIdList.put(NumericPadKey.ONE, button.getIdOnScene());
                    break;
                case TWO:
                    this.objectIdList.put(NumericPadKey.TWO, button.getIdOnScene());
                    break;
                case THREE:
                    this.objectIdList.put(NumericPadKey.THREE, button.getIdOnScene());
                    break;
                case FOUR:
                    this.objectIdList.put(NumericPadKey.FOUR, button.getIdOnScene());
                    break;
                case FIVE:
                    this.objectIdList.put(NumericPadKey.FIVE, button.getIdOnScene());
                    break;
                case SIX:
                    this.objectIdList.put(NumericPadKey.SIX, button.getIdOnScene());
                    break;
                case SEVEN:
                    this.objectIdList.put(NumericPadKey.SEVEN, button.getIdOnScene());
                    break;
                case HEIGHT:
                    this.objectIdList.put(NumericPadKey.HEIGHT, button.getIdOnScene());
                    break;
                case NINE:
                    this.objectIdList.put(NumericPadKey.NINE, button.getIdOnScene());
                    break;
                case OK:
                    this.objectIdList.put(NumericPadKey.OK, button.getIdOnScene());
                    break;
                case DOT:
                    this.objectIdList.put(NumericPadKey.DOT, button.getIdOnScene());
                    break;

                default:
            }


        }


    }


}