package com.familledupuis91.spigracer;

import android.os.Bundle;

import com.familledupuis91.gamingtools.components.ColorRGBA;
import com.familledupuis91.gamingtools.components.OpenGLActivity;
import com.familledupuis91.gamingtools.components.Scene;
import com.familledupuis91.gamingtools.components.fonts.FontConsolas;
import com.familledupuis91.gamingtools.components.fonts.GlString;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;

import com.familledupuis91.gamingtools.enums.DrawingMode;
import com.familledupuis91.gamingtools.shaders.ProgramShader;
import com.familledupuis91.gamingtools.shaders.ShaderRef;;

public class MainActivity extends OpenGLActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setContentView(R.layout.activity_main);

        //je crée une nouvelle scène
        Scene myscene = new Scene(this);

        //j'ajoute la texture à la scène pour pouvoir l'utiliser sur mes objets
        myscene.addTexture(R.string.splashscreen);

        //je crée un rectangle
        Rectangle2D rect = new Rectangle2D(DrawingMode.FILL);
        rect.setWidth(100);
        rect.setHeight(100);
        //  rect.setVisibility(true);
        rect.setCoord(0f, 0f);
        rect.setTexture(myscene.getTexture(R.string.splashscreen));

        ColorRGBA red = new ColorRGBA(1.f, 0.0f, 0, 0.5f);

        rect.setAmbiantColor(red);

        Rectangle2D rect2 = new Rectangle2D(DrawingMode.FILL);
        rect2.setWidth(100);
        rect2.setHeight(100);

        rect2.setCoord(1200f, 500f);
        myscene.addToScene(rect2);

        GlString gl = new GlString(new FontConsolas());
        gl.setText("S P I G R A C E R");
        gl.setWidth(200f);
        gl.setAmbiantColor(new ColorRGBA(1f, 0f, 0f, 0.5f));
        //je place le rectangle dans la scène
        myscene.addToScene(rect);
       // myscene.addToScene(gl);


        mGLSurfaceView.setRenderer(myscene);

        int a = 1;

        ProgramShader ps = myscene.getPSManager().getShader(ShaderRef.SIMPLE_WITHOUT_TEXTURE);
        // myscene.getPSManager().getGameObjectShaderList().put(rect,myscene.getPSManager().getShaderByName("noTexture"));

    }

}


