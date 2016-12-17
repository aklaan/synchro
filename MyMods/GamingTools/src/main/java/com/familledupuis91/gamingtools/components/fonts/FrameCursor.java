package com.familledupuis91.gamingtools.components.fonts;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rodol on 04/03/2016.
 */
public class FrameCursor {

    private final String ns = null;
    private int charValue;
    private int x;
    private int y;
    private int height;
    private int width;
    private float xOffset;
    private float yOffset;
    private float xSpace;
    private float textureRatio;
    private float base2heightRatio ;

    public float getBase2AdvanceRatio() {
        return base2AdvanceRatio;
    }

    public void setBase2AdvanceRatio(float base2AdvanceRatio) {
        this.base2AdvanceRatio = base2AdvanceRatio;
    }

    private float base2AdvanceRatio;


    /**
     * getter & setter
     */


    public int getCharValue() {
        return charValue;
    }

    public void setCharValue(int charValue) {
        this.charValue = charValue;
    }

    public int getX() {
        return x;
    }

    public void setX(int newx) {
        x = newx;
    }

    public int getY() {
        return y;
    }

    public void setY(int newy) {
        y = newy;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int newheight) {
        height = newheight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int newwitdh) {
        width = newwitdh;
    }

    public float getRatio() {
        return textureRatio;
    }

    public void setRatio(float newratio) {
        this.textureRatio = newratio;
    }

    public void setXoffset(float value){this.xOffset = value;}
    public float getXoffset(){return this.xOffset;}

    public void setYoffset(float value){this.yOffset = value;}
    public float getYoffset(){return this.yOffset;}

    public void setXspace(float value){this.xSpace = value;}
    public float getXspace(){return this.xSpace;}

    public float getBase2HeightRatio(){
        return this.base2heightRatio;
    }

    public FrameCursor(int charValue) {
        this.charValue = charValue;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.textureRatio = 0.f;
    }

//throws XmlPullParserException, IOException
    private void loadValue(int value, InputStream xmlData)  {

        //il n'est pas necessaire de relire les données XML si jamais on est déjà sur le même char
        if (this.getCharValue() != value) {
            try {
                /**
                 *  <char id="0" x="130" y="79" width="3" height="1" xoffset="-1" yoffset="79" xadvance="38" page="0" chnl="15" />
                 */
                xmlData.reset();

                //   InputStream in = new ByteArrayInputStream(getXmlData().getBytes(StandardCharsets.UTF_8));

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(xmlData, null);
                    // parser.setInput(in, null);
                    parser.nextTag();

                    parser.require(XmlPullParser.START_TAG, ns, "char");

                    while (parser.next() != XmlPullParser.END_DOCUMENT) {

                        if (Integer.parseInt(parser.getAttributeValue(0)) == value) {
                            this.setX(Integer.parseInt(parser.getAttributeValue(1)));
                            this.setY(Integer.parseInt(parser.getAttributeValue(2)));
                            this.setWidth(Integer.parseInt(parser.getAttributeValue(3)));
                            this.setHeight(Integer.parseInt(parser.getAttributeValue(4)));

                            this.setXoffset(Integer.parseInt(parser.getAttributeValue(5)));
                            this.setYoffset(Integer.parseInt(parser.getAttributeValue(6)));
                            this.setXspace(Integer.parseInt(parser.getAttributeValue(7)));
                            this.setRatio((float) this.getWidth() / (float) this.getHeight() );
                            this.base2heightRatio = this.getHeight()/235f;
                            this.setBase2AdvanceRatio(this.xSpace / 235f);
                            break;
                        }
                    }
                } finally {

                    //in.close();
                }

            } catch (XmlPullParserException e) {
                Log.e("problème", "Xmlpull");
            } catch (IOException e) {
                Log.e("problème", "IO");
            }
        }
    }


    private Boolean newValue(int value) {
        return (this.getCharValue() != value);
    }


    public float[] getCharTextCoord(int value, GlFont glFont) {

        //on repositionne si necessaire le frameCursor sur la lettre voulue
               if (newValue(value)){this.loadValue(value,glFont.getXmlData());}

        float[] result = new float[8];

        // il faut ajouter le cast en fload, sinon java tranforme le resultat en int car geWidth()
        // et getHeight() retournent un int.
        //upLeft
        result[0] = this.getX() / (float) glFont.getMap().getWidth();
        result[1] = this.getY() / (float) glFont.getMap().getHeight();
        //downLeft
        result[2] = this.getX() / (float) glFont.getMap().getWidth();
        result[3] = (this.getY() + this.getHeight()) / (float) glFont.getMap().getHeight();
        //downRight
        result[4] = (this.getX() + this.getWidth()) / (float) glFont.getMap().getWidth();
        result[5] = (this.getY() + this.getHeight()) / (float) glFont.getMap().getHeight();
        //upRight
        result[6] = (this.getX() + this.getWidth()) / (float) glFont.getMap().getWidth();
        result[7] = this.getY() / (float) glFont.getMap().getHeight();
        return result;
    }


    public float getCharRatio(int charValue,GlFont glFont) {

        if (newValue(charValue)){this.loadValue(charValue,glFont.getXmlData());}
        return this.getRatio();
    }


}

