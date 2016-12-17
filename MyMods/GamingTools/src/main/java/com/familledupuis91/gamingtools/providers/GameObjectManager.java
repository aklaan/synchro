package com.familledupuis91.gamingtools.providers;

import android.opengl.GLES20;

import com.familledupuis91.gamingtools.components.Composition;
import com.familledupuis91.gamingtools.components.GameObject;
import com.familledupuis91.gamingtools.components.Scene;
import com.familledupuis91.gamingtools.components.Vertex;
import com.familledupuis91.gamingtools.components.physics.Collidable;
import com.familledupuis91.gamingtools.components.physics.CollisionBox;
import com.familledupuis91.gamingtools.interfaces.Drawable;
import com.familledupuis91.gamingtools.utils.CONST;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by rodol on 24/11/2015.
 */
public class GameObjectManager {

    private Scene mScene;
    private ArrayList<Composition> mGameObjectList;
    public int[] vbo;  //tableau des localisation vertex buffer
    public int[] vboi; //tableau des lovasisation d'index buffer

    /********************************************
     * GETTER & SETTER
     ********************************************/
    public Scene getScene() {
        return mScene;
    }

    public void setScene(Scene mScene) {
        this.mScene = mScene;
    }

    public GameObjectManager(Scene scene) {

        //mémoriser le lien ave la scène
        this.setScene(scene);

        //initialisation d'une liste d'objets vide
        this.mGameObjectList = new ArrayList<Composition>();
    }


    public ArrayList<GameObject> getGameObjects() {
        ArrayList<GameObject> listOfGameObjects = new ArrayList<GameObject>();

        for (Composition composition : this.mGameObjectList) {
            GameObject gameObject = (GameObject) composition;
            listOfGameObjects.add(gameObject);
        }

        return listOfGameObjects;

    }

    public ArrayList<Drawable> getDrawable() {
        ArrayList<Drawable> listOfDrawable = new ArrayList<Drawable>();

        //on ajoute les éléments de la scène dans la liste des objet à rendre
        for (Composition composition : this.getComponent()) {
            if (composition instanceof Drawable) {
                listOfDrawable.add((Drawable) composition);
            }

            for (CollisionBox collisionBox : this.getScene().getColliderManager().getCollisionBoxList()) {
                if (collisionBox.getVisibility()) {
                    listOfDrawable.add(collisionBox);
                }
            }
        }

        return listOfDrawable;
    }

    public ArrayList<Collidable> getCollidable() {
        ArrayList<Collidable> listOfCollidable = new ArrayList<Collidable>();

        for (Composition composition : this.getComponent()) {
            if (composition instanceof Collidable) {
                listOfCollidable.add((Collidable) composition);
            }
        }
        return listOfCollidable;
    }


    public ArrayList<Composition> getComponent() {
        ArrayList<Composition> componentsList = new ArrayList<Composition>();

        //pour chaque gameObject de la scene
        for (Composition composition : this.mGameObjectList) {

            componentsList.addAll(composition.getComponent());
        }

        return componentsList;
    }

    /**
     * Ajouter un GameObject dans la liste des éléments de la scène
     * @param gameObject
     */
    public void add(GameObject gameObject) {
        gameObject.setScene(this.getScene());
        gameObject.setIdOnScene(this.getScene());
        this.mGameObjectList.add(gameObject);
    }

    public void remove(GameObject gameObject) {
        this.mGameObjectList.remove(gameObject);
    }


    /**
     * on fabrique un buffer contenant les coordonées de vertex et les coordonées de texture
     * {x,y,z,u,v,x,y,z,u,v.......}
     * Cette technique ne doit pas être utlisé si on fait évoluer les coordonées de texture
     * d'un Gameobject. l'intérêt de passer par un strideBuffer c'est de placer les infos dans
     * la mémoire graphique et de ne plus y toucher pour faire l'économie d'écriture entre la mémoire
     * client et la mémoire graphique
     * si on est obligé de mettre à jour la mémoire graphique à chaques frame, ça ne vaut pas le coup
     *
     * @param drawable
     */
    public void loadVBO(Drawable drawable) {

        //on crée un buffer de travail pour récupérer les informations à
        // transmettre dans la mémoire graphique

        // la taille de ce buffer est nombre de vertex * taille d'un stride * taille d'un float
        // sachant qu'un stride c'est la taille des coordonnées de vertex xyz
        // + la taille des coordonées uv de texture
        // + la taille de la couleur rgba du veertex
        FloatBuffer tempBuffer = ByteBuffer.allocateDirect(drawable.getNbvertex() * Vertex.stride * CONST.FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        int index = 0;
        for (Vertex vertex : drawable.getVertices()) {

            tempBuffer.rewind();
            tempBuffer.position((Vertex.stride) * index);
            tempBuffer.put(vertex.x).put(vertex.y).put(vertex.z)
                    .put(vertex.u).put(vertex.v)
                    .put(vertex.r).put(vertex.g).put(vertex.b).put(vertex.a);
            tempBuffer.rewind();
            index++;

        }

        //on se place sur le glbuffer assigné à l'objet
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, drawable.getGlVBoId());

        //on charge les données
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, tempBuffer.capacity() * CONST.FLOAT_SIZE,
                tempBuffer, GLES20.GL_STATIC_DRAW);

        //unbind
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //on vide le buffer temporaire pour libérer la mémoire cliente
        tempBuffer.clear();
    }


    public void loadVBOi(Drawable drawable) {
        //On se place sur le GlBuffer assigné
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, drawable.getGlVBiId());

        //On charge les données dans la mémoire graphique
        //-------------------------------------------------------------------------------------------------
        //      target = un buffer ELEMENT_ARRAY dans mémoire graphique
        //      size = la taille du buffer = le nombre d'indices à stocker * la taille d'un SHORT
        //      data = les données
        //      usage :   GL_STATIC_DRAW  : les données sont lues une fois et sont réutilisée a chaque frame
        //             ou GL_DYNAMIC_DRAW : les données sont lues a chaque frame
        //-------------------------------------------------------------------------------------------------
        ShortBuffer wrkIndiceBuffer = drawable.getIndices();
        wrkIndiceBuffer.rewind();
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, wrkIndiceBuffer.capacity() * CONST.SHORT_SIZE,
                wrkIndiceBuffer, GLES20.GL_STATIC_DRAW);

        //unbind
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    /**
     * Récupérer un Shape de la scène via son TAG
     * TODO : ici on récupère seulement le premier qui vient
     * TODO : on peut très bien avoir plusieurs objets avec le même TAG
     *
     * @param tagName
     * @return
     */
    public GameObject getGameObjectByTag(String tagName) {
        GameObject result = null;
        for (GameObject gameObject : this.getGameObjects()) {
            // Log.i("info : ", gameObject.getTagName());

            if (gameObject.getTagName() == tagName) {
                result = (GameObject) gameObject;
            }

        }
        return result;
    }


    /**
     *
     * @param idOnScene
     * @return
     */

    public GameObject getGameObjectById(Long idOnScene) {
        GameObject result = null;
        for (GameObject gameObject : this.getGameObjects()) {
            if (gameObject.getIdOnScene() == idOnScene) {
                result = (GameObject) gameObject;
            }

        }
        return result;
    }
    /**
     * initialisation du contexte OpenGL : on va charger les objets dans la mémoire graphique
     */
    public void initializeGLContext() {

        //on récupère le nombre d'objets
        int nbObjects = countDrawable();

        //on crée un tableau qui va référencer les vertex buffer
        vbo = new int[nbObjects];

        //on demande à OpenGL de créer les buffer et de les référencer dans le tableau vbo
        GLES20.glGenBuffers(nbObjects, vbo, 0);

        //on crée un tableau qui va référencer les index buffer
        vboi = new int[nbObjects];

        //on demande à OpenGL de créer des buffer et de les référencer dans le tableau vboi
        GLES20.glGenBuffers(nbObjects, vboi, 0);


        int indx = 0;

        //pour chaque gameObject, on lui assigne un Glbuffer
        //pour la partie vertex et la partie index
        //ensuite on charge les buffers
        for (Drawable drawable : this.getDrawable()) {

            indx = loadVBs(drawable, indx);
        }
    }


    private int countDrawable() {

        return this.getDrawable().size();
    }

    /**
     *
     */
    private int loadVBs(Drawable drawable, int index) {
        drawable.setGlVBoId(vbo[index]);
        this.loadVBO(drawable);

        drawable.setGlVBiId(vboi[index]);
        this.loadVBOi(drawable);
        index++;
        return index;
    }


    /**
     *
     */
    public void update() {
        for (GameObject gameObject : this.getGameObjects()) {
            gameObject.update();
            gameObject.updateModelView();
        }
    }


}

