package com.familledupuis91.gamingtools.shaders;


import android.opengl.GLES20;
import android.opengl.Matrix;

import com.familledupuis91.gamingtools.components.Vertex;
import com.familledupuis91.gamingtools.interfaces.Drawable;
import com.familledupuis91.gamingtools.utils.CONST;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ProgramShader_forLines extends ProgramShader {

    public static final String SHADER_FOR_LINES = "SHDR_FOR_LINES";

    public ProgramShader_forLines() {
        super();
        this.setRefId(ShaderRef.SHADER_FOR_LINES);

    }

    @Override
    public void initCode() {

        this.fragmentShaderSource =
                "#ifdef GL_ES \n"
                        + " precision highp float; \n"
                        + " #endif \n"

                        // + " varying vec2 vTexCoord; "
                        + " varying vec4 vColor;"
                        //            + " varying vec3 pos;"
                        + " void main() {"
                        //                    + "    gl_FragColor =  vColor;"
                        + "    gl_FragColor =  vec4(1.,0.,1.,1.0);"

                        + "}";

        this.vertexShaderSource =
                "uniform mat4 " + this.VSH_UNIFORM_MVP + ";"
                        + "attribute vec3 " + this.VSH_ATTRIB_VERTEX_COORD + ";"
                        + "attribute vec4 " + this.VSH_ATTRIB_VERTEX_COLOR + ";"
                        + "varying vec4 vColor;"

                        + "void main() {"
                        // on calcule la position du point via la matrice de projection
                        + " vec4 position = " + this.VSH_UNIFORM_MVP + " * vec4(" + this.VSH_ATTRIB_VERTEX_COORD + ".xyz, 1.);"
                        //  + "vec4 position = vec4(aPosition.xyz, 1.);"
                      // + " vColor = " + this.VSH_ATTRIB_VERTEX_COLOR + ";"
                        //gl_PointSize ne fonctionne qu'avec le mode GL_POINTS
                        + " gl_PointSize = 1.0;"
                        // cette commande doit toujours �tre la derni�re du vertex shader.
                        + "	gl_Position =  position;"
                        + "}";

    }

    @Override
    public void initLocations() {
        // les attribs
        this.attrib_vertex_coord_location = GLES20.glGetAttribLocation(
                mGLSLProgram_location, this.VSH_ATTRIB_VERTEX_COORD);
        this.attrib_vertex_color_location = GLES20.glGetAttribLocation(
                mGLSLProgram_location, this.VSH_ATTRIB_VERTEX_COLOR);

        // les Uniforms
        this.uniform_mvp_location = GLES20.glGetUniformLocation(
                this.mGLSLProgram_location, this.VSH_UNIFORM_MVP);

    }

    // *******************************************************************
    // Attention : il ne faut pas rendre enable un attribut non valorisé
    // sinon c'est ecran noir !
    @Override
    public void enableAttribs() {
        GLES20.glEnableVertexAttribArray(this.attrib_vertex_coord_location);
    //   GLES20.glEnableVertexAttribArray(this.attrib_vertex_color_location);

    }

    // **************************************************************************
    @Override
    public void disableAttribs() {
        GLES20.glDisableVertexAttribArray(this.attrib_vertex_coord_location);
     //   GLES20.glDisableVertexAttribArray(this.attrib_vertex_color_location);



    }


    @Override
    public void draw(Drawable drawable, float[] projectionMatrix) {

        //allocation d'un floatBuffer pour les vertex
        FloatBuffer fb = ByteBuffer.allocateDirect(drawable.getNbvertex() * 3 * CONST.FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        fb = getFbVertices(drawable);

        //passage des vertex au contexte GL
        GLES20.glVertexAttribPointer(this.attrib_vertex_coord_location, 3,
                GLES20.GL_FLOAT, false, Vertex.Vertex_COORD_SIZE_BYTES, getFbVertices(drawable));
        this.enableAttribs();

        //Calcul de la matrice de projection
        float[] mMvp = new float[16];
         Matrix.multiplyMM(mMvp, 0, projectionMatrix, 0, drawable.getModelView(), 0);

        GLES20.glUniformMatrix4fv(this.uniform_mvp_location, 1, false, mMvp, 0);

        //dessiner
        GLES20.glDrawElements(GLES20.GL_LINES, drawable.getNbIndex(),
                GLES20.GL_UNSIGNED_SHORT, getFbIndex(drawable));

        //on désactive les attributs
        this.disableAttribs();
    }

    // getter vertices
    public FloatBuffer getFbVertices(Drawable drawable) {

        //allocation d'un floatBuffer pour les vertex
        FloatBuffer fb = ByteBuffer.allocateDirect(drawable.getNbvertex() * 3 * CONST.FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        //remplissage du FloatBuffer
        for (int i = 0; i < drawable.getNbvertex(); i++) {
            this.putVertex(fb, i, drawable.getVertices().get(i));
        }
        fb.rewind();
        return fb;
    }

    // setter vertices
    public void putVertex(FloatBuffer fb, int index, Vertex vertex) {
        // la position physique en mémoire des bytes qui représentent le vertex
        // c'est la taille d'un vertex en bytes x l'index
        fb.rewind();
        // ici on se positionne dans le buffer à l'endroit où l'on va ecrire le
        // prochain vertex
        fb.position(Vertex.Vertex_COORD_SIZE * index);
        fb.put(vertex.x).put(vertex.y).put(vertex.z);
        // on se repositionne en 0 , prêt pour la relecture
        fb.rewind();


    }


    // getter vertices
    public ShortBuffer getFbIndex(Drawable drawable) {

        // Allocation du short buffer pour les index
        ShortBuffer fbIndices = ByteBuffer.allocateDirect(drawable.getNbIndex() * CONST.SHORT_SIZE)
                .order(ByteOrder.nativeOrder()).asShortBuffer();

        //Remplissage du FloatBuffer
        for (int i = 0; i < drawable.getNbIndex(); i++) {
            this.putIndice(fbIndices, i, drawable.getIndices().get(i));
        }
        fbIndices.rewind();
        return fbIndices;
    }


    // setter indices
    public void putIndice(ShortBuffer fbIndice, int index, int indice) {
        // on se positionne a l'index dans le buffer
        // comme on a qu'un seul short a placer on ne fait pas comme dans
        // putvertice
        fbIndice.position(index);
        // on ecrit le short
        fbIndice.put((short) indice);
        // on se repositionne en zéro
        fbIndice.position(0);
    }


}
