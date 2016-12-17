package com.familledupuis91.gamingtools.shaders;

import java.util.ArrayList;
import java.util.HashMap;

import com.familledupuis91.gamingtools.components.physics.CollisionBox;
import com.familledupuis91.gamingtools.components.shapes.Shape;
import com.familledupuis91.gamingtools.components.Scene;
import com.familledupuis91.gamingtools.interfaces.Drawable;


import android.opengl.GLES20;
import android.util.Log;

/**
 * le shader provider va référencer les program Shader a utiliser
 *
 * @author NC10
 */
public class ProgramShaderManager {


    public Scene mScene;
    public ArrayList<ProgramShader> shaderList;
    public HashMap<Byte, Integer> catalogShader;

    public HashMap<Shape, ProgramShader> gameObjectShaderList;

    public ProgramShader getCurrentActiveShader() {
        return mCurrentActiveShader;
    }

    private ProgramShader mCurrentActiveShader;

    // déclaration des attributs du shader : default
    public final String DEFAULT_VSH_ATTRIB_VERTEX_COORD = "aPosition";
    public final String DEFAULT_VSH_ATTRIB_COLOR = "aColor";
    public final String DEFAULT_VSH_ATTRIB_TEXTURE_COORD = "aTexCoord";

    public final String DEFAULT_VSH_UNIFORM_MVP = "uMvp";
    public final String DEFAULT_FSH_UNIFORM_TEXTURE = "tex0";

    public ProgramShader getDefaultShader() {
        return mDefaultShader;
    }

    public void setDefaultSader(ProgramShader mDefaultShader) {
        this.mDefaultShader = mDefaultShader;
    }

    private ProgramShader mDefaultShader;


    /**
     * getter & setter
     */
    public HashMap<Shape, ProgramShader> getGameObjectShaderList() {
        return gameObjectShaderList;
    }

    public void setGameObjectShaderList(HashMap<Shape, ProgramShader> gameObjectShaderList) {
        this.gameObjectShaderList = gameObjectShaderList;
    }


    /***
     * Constructeur
     */
    public ProgramShaderManager() {

        this.mCurrentActiveShader = null;
        catalogShader = new HashMap<Byte, Integer>();
        shaderList = new ArrayList<ProgramShader>();
        this.gameObjectShaderList = new HashMap<Shape, ProgramShader>();
    }


    /***
     * Ajouter un shader dans le catalogue
     */
    public void add(ProgramShader shader) {
        shader.getClass().getName();
        int newindex = catalogShader.size() + 1;
        catalogShader.put(shader.getRefId(), newindex);
        shaderList.add(shader);
    }

    /**
     * récupérer un shader via son nom
     *
     * @param shaderRef
     * @return
     */
    public ProgramShader getShader(byte shaderRef) {
        ProgramShader result = null;
        if (catalogShader.get(shaderRef) == null) {
            Log.e(this.getClass().getName(), "Shader " + shaderRef
                    + " unknow on Catalog");
        } else {
            result = shaderList.get(catalogShader.get(shaderRef) - 1);
        }
        return result;
    }

    /**
     * @param shaderRef
     */
    public void use(byte shaderRef) {
        ProgramShader sh = this.getShader(shaderRef);
        this.use(sh);

    }

    /**
     * Activer l'utilisation d'un shader
     *
     * @param shader
     */
    public void use(ProgramShader shader) {

        // use program
        if (this.mCurrentActiveShader != shader
                || this.mCurrentActiveShader == null) {

            if (this.mCurrentActiveShader != null) {
                this.mCurrentActiveShader.disableAttribs();
            }
            GLES20.glUseProgram(shader.mGLSLProgram_location);
            this.mCurrentActiveShader = shader;

        }


    }

    /**
     * effectuer le rendu de la scene
     */
    public void renderScene(Scene scene) {

        for (Drawable drawable : scene.getGOManager().getDrawable()) {

            //Dessiner les objets visibles
            if (drawable.getVisibility()) {

                //Appel au shader de l'objet s'il en requiert un en particulier.
                //sinon on utilise le shader par defaut.
                this.use(this.getDefaultShader());

                if (this.getGameObjectShaderList().get(drawable) != null) {
                    this.use(this.getGameObjectShaderList().get(drawable));
                } ;

                if (drawable instanceof CollisionBox)
                {
                    this.use(this.getShader(ShaderRef.SHADER_FOR_LINES));
                }


                this.getCurrentActiveShader().draw(drawable, scene.getProjectionView());



            }
        }
    }


}
