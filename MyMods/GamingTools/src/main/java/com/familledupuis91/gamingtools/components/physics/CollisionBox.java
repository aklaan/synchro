package com.familledupuis91.gamingtools.components.physics;

import com.familledupuis91.gamingtools.components.Vertex;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.enums.DrawingMode;
import com.familledupuis91.gamingtools.shaders.ProgramShaderManager;
import com.familledupuis91.gamingtools.shaders.ProgramShader;
import com.familledupuis91.gamingtools.shaders.ProgramShader_forLines;
import com.familledupuis91.gamingtools.shaders.ShaderRef;
import com.familledupuis91.gamingtools.utils.CONST;
import com.familledupuis91.gamingtools.utils.Tools;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class CollisionBox extends Rectangle2D{

    private final float defaultOffset = 0.1f;

    //je pense qu'il est préférable de mémoriser l'objet plutôt
    //qu'un ID car il est fort probable que JAVA utilise un pointeur vers l'objet
    //si on utilise un id, on va devoir faire un calcul pour retrouver l'objet dans
    // la liste
    private Collidable mCollidable;
    private ArrayList<Vertex> mInnerVertices;

    //tableau de vertices pour les coordonées world de la boite
    public ArrayList<Vertex> mWorldVertices; // d�finition d'un tableau de vertex

    /*****************************************************************************
     * getter & setter
     *****************************************************************************/
    public ArrayList<Vertex> getVertices() {
        int a =0;
        //return mWorldVertices;
        return mInnerVertices;
    }

    public void setVertices(ArrayList<Vertex> mWorldVertices) {
        this.mWorldVertices = mWorldVertices;
    }

    public ArrayList<Vertex> getInnerVertices() {
        return mInnerVertices;
    }

    public void setInnerVertices(ArrayList<Vertex> mInnerVertices) {
        this.mInnerVertices = mInnerVertices;
    }


    /**
     * gette & setter
     */


    public Collidable getCollider() {
        return mCollidable;
    }

    public void setCollider(Collidable collidable) {
        this.mCollidable = collidable;
    }

    /**
     * Constructor 1 : avec offset
     */
    public CollisionBox(Collidable collidable, float offsetX, float offsetY) {
        super(DrawingMode.EMPTY);
        this.commonInitialization(collidable);
        this.initInnerVertices(offsetX, offsetX);
    }

    /**
     * Contructor 2 : avec offset par defaut
     *
     * @param collidable
     */
    public CollisionBox(Collidable collidable) {
        super(DrawingMode.EMPTY);
        this.mInnerVertices = new ArrayList<Vertex>();
        this.commonInitialization(collidable);
        this.initInnerVertices(defaultOffset, defaultOffset);
    }


    /**
     * @param collidable
     */
    private void commonInitialization(Collidable collidable) {
        //par défaut la box n'est pas visible
        this.setVisibility(false);

        //On mémorise la référence du shape "parent"
        this.setCollider(collidable);

    }

    /**
     * @param offsetX
     * @param offsetY
     */
    private void initInnerVertices(float offsetX, float offsetY) {
        //Aller rechercher les points limite de la forme et en déduire
        //un rectangle avec un retrait "offset"
        float xread = 0f;
        float yread = 0f;
        float xmin = 0f;
        float xmax = 0f;
        float ymin = 0f;
        float ymax = 0f;

        // pour chaque vertex composant la forme, on va en déterminer les
        // limites pour fabriquer une boite de colision
        for (int i = 0; i < this.getCollider().getVertices().size(); i++) {

            // lecture du X
            xread = this.getCollider().getVertices().get(i).x;
            xmin = (xread < xmin) ? xread : xmin;
            xmax = (xread > xmax) ? xread : xmax;

            // lecture du Y
            yread = this.getCollider().getVertices().get(i).y;
            ymin = (yread < ymin) ? yread : ymin;
            ymax = (yread > ymax) ? yread : ymax;

			/*
             * Log.i("xy",String.valueOf(i) +" / " +String.valueOf(taillemax)
			 * +" : " +String.valueOf(xmin)+ "/" + String.valueOf(xmax));
			 */
        }

        //gestion des offset en taille
        xmin += offsetX;
        xmax += -offsetX;
        ymin += offsetY;
        ymax += -offsetY;


        //on ajoute les vertex du rectangle
        this.getInnerVertices().add(new Vertex(xmin, ymax, 0));
        this.getInnerVertices().add(new Vertex(xmin, ymin, 0));
        this.getInnerVertices().add(new Vertex(xmax, ymin, 0));
        this.getInnerVertices().add(new Vertex(xmax, ymax, 0));

    }


    public void updateWorldVertices() {

        // on redéfinit les coordonées des vertices
        // pour avoir les coordonnées transformées
        this.mWorldVertices = Tools.applyModelView(this.mInnerVertices, this.getCollider().getModelView());
    }

    @Override
    public  float[] getModelView(){
        return this.getCollider().getModelView();
    }

    /**
     * @param Mvp
     */
    public void draw(ProgramShaderManager PSM, float[] Mvp) {

        ProgramShader sh = PSM.getShader(ShaderRef.SHADER_FOR_LINES);
        PSM.use(sh);

        sh.enableAttribs();

        // on charge les coordonnées des vertices

        FloatBuffer fbVertices = ByteBuffer.allocateDirect(4 * 3 * CONST.FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int i = 0; i < this.mWorldVertices.size(); i++) {
            Tools.putXYZIntoFbVertices(fbVertices, i, this.mWorldVertices.get(i));
        }
        sh.setVerticesCoord(fbVertices);

        // on alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec
        // une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, Mvp, 0);

        // on récupère les indices du rectangle vide
        // qui indiquent dans quel ordre les vertex doivent être dessinés

        ShortBuffer indices = Rectangle2D.getIndicesForEmptyRec();
        GLES20.glDrawElements(GLES20.GL_LINES, indices.capacity(),
                GLES20.GL_UNSIGNED_SHORT, indices);

    }



}
