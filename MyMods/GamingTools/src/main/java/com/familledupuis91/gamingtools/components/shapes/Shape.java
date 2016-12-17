package com.familledupuis91.gamingtools.components.shapes;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;


import com.familledupuis91.gamingtools.components.Composition;
import com.familledupuis91.gamingtools.components.GameObject;
import com.familledupuis91.gamingtools.components.Scene;
import com.familledupuis91.gamingtools.components.Vertex;
import com.familledupuis91.gamingtools.components.physics.Collidable;
import com.familledupuis91.gamingtools.components.texture.Texture;
import com.familledupuis91.gamingtools.interfaces.Drawable;
import com.familledupuis91.gamingtools.shaders.ProgramShader;


import android.opengl.GLES20;
import android.opengl.Matrix;


public class Shape extends GameObject implements Cloneable, Collidable, Composition,Drawable {

    //Id du buffer Gl où se trouvent les coordonnees de vertex
    private int glVBoId;
    //nombre de vertex
    private int nbvertex;
    //Id du buffer Gl où se trouvent les index vertex
    private int glVBiId;
    //nombre d'index vertex
    private int nbIndex;
    //Tag de l'objet
    private String mTagName;

    //Actuelle texture utilisé pour rendre l'objet
    private Texture mTexture;
    //Top pour activer/désactiver le rendu des textures
    public boolean textureEnabled;

    public boolean isTextureEnabled() {
        return textureEnabled;
    }

    public void setTextureActivation(Boolean textureEnabled) {
        this.textureEnabled = textureEnabled;
    }


    //Top pour activer/désactiver la gestion des colissions
    //ce flag sert à créer les BOX
    public boolean canCollide = false;

    //ce falg sert à savoir si on doit calculer les collisions
    //avec les autres objets de la scène
    public boolean collisonCheckingStatus = false;

    //liste des objets à écouter
    public ArrayList<Shape> mShapeToListenList;

    public float[] mModelView = new float[16];

    public int drawMode = GLES20.GL_TRIANGLES;

    // public static final int FLOAT_SIZE = 4;
    // on indique qu'il faut 4 byte pour repésenter un float
    // 00000000 00000000 00000000 00000000

    //plutôt que de l'écrire en dur 4, on le calcul.
    //comme ça si jamais le système n'utilise pas 4 bytes on
    //reste bon.
    // public static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;

    // un byte n'est pas obligatoirement égal à 8 bit
    // cela dépend du matériel. en général il est très souvant egal à
    // 8 bit ce qui fait qu'un byte est trés souvent égal à un Octet
    // mais comme ce n'est pas toujours le cas, on choisi volontairement
    // de parler en byte et non en octet pour être plus précis.

    //    public static final int SHORT_SIZE = 2;
    //public static final int SHORT_SIZE = Short.SIZE / Byte.SIZE;

    // ici on indique qu'un short est codé sur 2 byte
    // soit généralement 2 octets
    // soit : 00000000 00000000

    //liste des vertex composant l'objet
    // public ArrayList<Vertex> mVertices;


    // ! Vertices
    // définition d'un buffer conteant les coordonées des vertices
    // TODO: ce buffer n'est pas censé rester en mémoire client
    //  public FloatBuffer mFbVertices;

    // ! indices
    //Définition d'un buffer contenant les indices représentant l'ordre dans lequel
    // doivent être dessinés chaque vertex
    // TODO: ce buffer n'est pas censé rester en mémoire client
//    private ShortBuffer mIndices;

    // ! coordonées de texture
    // définition d'un buffer avec les coordonées de textures.
    // TODO: ce buffer n'est pas censé rester en mémoire client
    private FloatBuffer mTextCoord;


    /******************************************************************
     * getter & setter
     ****************************************************************/

    public int getNbvertex() {
        return nbvertex;
    }

    public void setNbvertex(int nbvertex) {
        this.nbvertex = nbvertex;
    }

    public int getNbIndex() {
        return nbIndex;
    }

    public void setNbIndex(int nbIndex) {
        this.nbIndex = nbIndex;
    }

    public int getGlVBoId() {
        return glVBoId;
    }

    public void setGlVBoId(int glVBoId) {
        this.glVBoId = glVBoId;
    }

    public int getGlVBiId() {
        return glVBiId;
    }

    public void setGlVBiId(int glVBiId) {
        this.glVBiId = glVBiId;
    }

    public float[] getModelView() {
        return this.mModelView;
    }

    public void enableCollisions() {
        this.canCollide = true;
    }

    public void disableCollisions() {
        this.canCollide = false;
    }


    public boolean isCollisionEnabled() {
        return (this.canCollide == true);
    }

    public boolean isCollisionCheckingEnabled() {
        return (this.collisonCheckingStatus == true);
    }

    public void disableCollisionChecking() {
        this.collisonCheckingStatus = false;
    }

    public void enableCollisionChecking() {
        this.collisonCheckingStatus = true;
    }

    public int getGlDrawMode(){return this.drawMode;};

    /**
     * @param
     */


    /**
     * @return
     */
    public ArrayList<Shape> getGameObjectToListenList() {
        return this.mShapeToListenList;
    }

    /********************************************************************
     * Constructeur
     *******************************************************************/
    public Shape() {

        //taille par défaut
        this.setWidth(1);
        this.setHeight(1);

        //pas de rendu des texture par défaut
        this.textureEnabled = false;

        //pas de tagname
        mTagName = "";

        //visible par défaut
        setVisibility(true);

        this.mShapeToListenList = new ArrayList<Shape>();


    }


    public ArrayList<Vertex> getVertices() {
        ArrayList<Vertex> result = new ArrayList<Vertex>();
        return result;
    }

    public Texture getTexture() {
        return mTexture;
    }

    public void setTexture(Texture mTexture) {
        //si on assigne une texture, alors on active par defaut l'affichage des textures.
        this.enableTexturing();
        this.mTexture = mTexture;
    }

    public void enableTexturing(){
        this.textureEnabled = true;
    }

    public void disableTexturing(){
        this.textureEnabled = false;
    }


    /**
     * insérer des bytes "u,v" dans le buffer des coordonnées de texture
     *
     * @param index
     * @param vertex
     */
    public void putUVIntoFbTextCood(int index, Vertex vertex) {
        //on se place au debut du buffer
        mTextCoord.rewind();
        //on avance dans le buffer à l'endroit où on souhaite écrire
        mTextCoord.position(Vertex.Vertex_TEXT_SIZE * index);
        //on écrit les coordonées de texture
        mTextCoord.put(vertex.u).put(vertex.v);
        // on se repositionne en 0 , prêt pour la lecture
        mTextCoord.rewind();
    }

    /**
     * @param x
     * @param y
     * @param anglRAD
     */
    public void rotate(float x, float y, float anglRAD) {

        this.setX(this.getX() + (float) (Math.cos(anglRAD)));
        this.setY(this.getY() + (float) (Math.sin(anglRAD)));
        // Matrix.translateM(wrkresult, 0, x, y, 0);
    //    Log.i("debug", String.valueOf(this.getX()) + " / " + String.valueOf(this.getY()));
   //     Log.i("debug", String.valueOf(Math.cos(anglRAD)));

    }


    /**
     * @return
     */

    /**
     * @return
     */
    /*
    public FloatBuffer getFbVertices() {

        for (int i = 0; i < this.mVertices.size(); i++) {
            this.putXYZIntoFbVertices(i, this.mVertices.get(i));
        }

        return mFbVertices;
    }
*/


    // getter indices
    public ShortBuffer getIndices() {
        return null;
    }


    /**
     * getter & setter
     *
     * @return
     */

    @Override
    public void update() {

    }


    /**
     * Dessiner l'objet

     public void draw_Old() {

     ProgramShaderManager PSM = this.getScene().getPSManager();
     // j'utilise le shader prévu
     ProgramShader sh = PSM.getShaderByName("simple");
     PSM.use(sh);


     if (this.textureEnabled) {

     //on active l'unité de traitement des textures 0
     GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

     // on demande à opengl d'utiliser la texture chargé en mémoire graphique
     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture().getGlBufferId());

     //on assigne la texture à l'unité de traitement zéro
     GLES20.glUniform1i(sh.uniform_texture_location, 0);

     //initialisation du buffer des coodonnées de texture
     getFbTextCood();

     //Chargement des coodonées de texture
     sh.setTextureCoord(getTextCoord());


     }

     //--------------------------------------------

     FloatBuffer toto = ByteBuffer.allocateDirect(getNbvertex() * 4 * FLOAT_SIZE)
     .order(ByteOrder.nativeOrder()).asFloatBuffer();
     toto.rewind();

     for (int i = 0; i < getNbvertex(); i++) {
     toto.rewind();
     //on avance dans le buffer à l'endroit où on souhaite écrire
     toto.position(4 * i);
     //on écrit les coordonées de texture
     toto.put(1f).put(1f).put(1f).put(1f);
     // on se repositionne en 0 , prêt pour la lecture
     toto.rewind();
     }


     toto.rewind();

     //4 float dont la taille est 4*taille d'un float
     GLES20.glVertexAttribPointer(sh.attrib_color_location, 4,
     GLES20.GL_FLOAT, false, 4 * (Float.SIZE / Byte.SIZE), toto);

     //--------------------------------------------
     sh.enableAttribs();


     //Calcul de la matrice model-view-projection
     //------------------------------------------------------------------------
     float[] mMvp = new float[16];

     if (this.getScene().getViewMode() == Scene.VIEW_MODE.ORTHO) {
     Matrix.multiplyMM(mMvp, 0, this.getScene().mProjectionORTH, 0, this.mModelView, 0);

     } else
     Matrix.multiplyMM(mMvp, 0, this.getScene().getProjectionView(), 0, this.mModelView, 0);

     // On alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
     // avec une matrice de 4 flotant.
     GLES20.glUniformMatrix4fv(sh.uniform_mvp_location, 1, false, mMvp, 0);

     //on alimente la donnée UNIFORM Alpha
     GLES20.glUniform1f(sh.uniform_alpha_location, this.getAlpha());

     //utilisation des coordonnées stockées
     //------------------------------------------------------------------------
     //je me place sur le vbo
     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getGlVBoId());

     //on active l'utilisation de la variable aPostion dans le shader
     GLES20.glEnableVertexAttribArray(sh.attrib_vertex_coord_location);

     //plutôt que de passer des valeur au shader, on indique une position en int.
     // OpenGl comprend alors qu'il faut passer par le pointeur sur lequel on viens de
     // se positionner pour aller chercher les valeurs contenue dans la mémoire graphique
     // et il doit lire le buffer à partir de la position zéro
     GLES20.glVertexAttribPointer(sh.attrib_vertex_coord_location, 3,
     GLES20.GL_FLOAT, false, 0, 0);

     //utilisation de l'index buffer stocké
     //----------------------------------------------------------------------------
     //je me place sur le buffer des index
     GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.getGlVBiId());

     //je dessine les vertex selon l'ordre indiqué dans l'index.
     // au lieu de fournir des valeurs on indique une position en int ce qui permet à opengl de comprendre
     // qu'il faut passer par le pointeur sur lequel on viens de se postionner
     // et il doit lire le buffer à partir de la position zéro
     GLES20.glDrawElements(this.drawMode, this.getNbIndex(),
     GLES20.GL_UNSIGNED_SHORT, 0);

     //je pointe les buffer sur "rien" pour ne pas les réutiliser par erreur
     GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

     sh.disableAttribs();

     }

     */


    /**
     *
     */

    public void draw() {
        ProgramShader shader = this.getScene().getPSManager().getCurrentActiveShader();

        //si l'utilisation d'une texture est active on l'utilise
        if (this.textureEnabled) {

            //on active l'unité de traitement des textures 0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

            // on demande à opengl d'utiliser la texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture().getGlBufferId());

            //on fait pointer  uniform_texture_location sur le buffer où est la texture
            GLES20.glUniform1i(shader.uniform_texture_location, 0);
        }

        /********************************************************************************
         * les données Varying du shader doivent être connues POUR CHAQUES VERTEX !!!!
         * si on passe la couleur en varying, on doit avoit un buffer couleur
         * qui contient les couleurs de chaques vertex !!
         * pour rappel, on a 1 vertex Shader par vertex !!!
         *
         * Si tous les vertex ont la même couleur , on peu simplement utiliser un Uniform
         * car les uniforms sont connus pour tous les vertex shader
         */

        //--------------------------------------------


        //--------------------------------------------


        //je me place sur le buffer stride qui contient x,y,z,u,v
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getGlVBoId());

        //lecture des coordonées x,y,z:
        // plutôt que de passer des valeur au shader, on indique une position en int.
        // OpenGl comprend alors qu'il faut passer par le pointeur sur lequel on vient de
        // se positionner pour aller chercher les valeurs contenue dans la mémoire graphique
        // ici on commence à lire le buffer à partir de la position zéro et on fait des saut
        // de "stride=5" pour passer aux coordonnées suivantes
        GLES20.glVertexAttribPointer(shader.attrib_vertex_coord_location, Vertex.Vertex_COORD_SIZE,
                GLES20.GL_FLOAT, false, Vertex.stride * Vertex.FLOAT_SIZE, 0);

        //ici on commence la lecture des coordonées de texture juste après les premier x,y,z
        // ensuite on fait des saut pour lire les suivantes
        GLES20.glVertexAttribPointer(shader.attrib_vertex_texture_coord_location, Vertex.Vertex_TEXT_SIZE,
                GLES20.GL_FLOAT, false, Vertex.stride * Vertex.FLOAT_SIZE, Vertex.Vertex_COORD_SIZE * Vertex.FLOAT_SIZE);

        //ici on commence la lecture des coordonées de texture juste après les premier x,y,z
        // ensuite on fait des saut pour lire les suivantes
        GLES20.glVertexAttribPointer(shader.attrib_vertex_color_location, Vertex.Vertex_COLOR_SIZE,
                GLES20.GL_FLOAT, false, Vertex.stride * Vertex.FLOAT_SIZE, (Vertex.Vertex_COORD_SIZE + Vertex.Vertex_TEXT_SIZE) * Vertex.FLOAT_SIZE);

        //--------------------------------------------
        shader.enableAttribs();

        //Calcul de la matrice model-view-projection
        float[] mMvp = new float[16];

        if (this.getScene().getViewMode() == Scene.VIEW_MODE.ORTHO) {
            Matrix.multiplyMM(mMvp, 0, this.getScene().mProjectionORTH, 0,
                    this.mModelView, 0);

        } else
            Matrix.multiplyMM(mMvp, 0, this.getScene().getProjectionView(), 0,
                    this.mModelView, 0);

        // On alimente la donnée UNIFORM mAdressOf_Mvp du programme OpenGL
        // avec une matrice de 4 flotant.
        GLES20.glUniformMatrix4fv(shader.uniform_mvp_location, 1, false, mMvp, 0);

        //on alimente la donnée Alpha
        GLES20.glUniform1f(shader.uniform_ambiantColorRGBA_location, this.getAlpha());

        //je me place sur le buffer des index
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, this.getGlVBiId());

        //je dessine les vertex selon l'ordre indiqué dans l'index.
        // au lieu de fournir des valeurs on indique zéro. ce qui permet à opengl de comprendre
        // qu'il faut passer par le pointeur sur lequel on viens de se postionner
        ///this.drawMode = GLES20.GL_LINES;
        //drawElement
        // mode de dessin
        //nombre d'indices (en théorie = 6)
        //

        GLES20.glDrawElements(this.drawMode, this.getNbIndex(),
                GLES20.GL_UNSIGNED_SHORT, 0);

        //je pointe les buffer sur "rien" pour ne pas les réutiliser par erreur
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


    }


    /********************************************************
     * On écoute les objets
     * note : on dépend de l'ordre dans lequel sont traité
     * les objets
     *******************************************************/
    public void updateListerners() {

        if (!this.getGameObjectToListenList().isEmpty()) {
            for (Shape go : this.getGameObjectToListenList()) {

                onListenGameObject(go);

            }
        }

    }


    /**************************************************************************
     * Actions effectuer lorsque l'on écoute les objets
     *
     * @param go
     *************************************************************************/
    public void onListenGameObject(Shape go) {

    }

    /**************************************************************************
     * Actions effectuer lorsque l'animation joue
     *************************************************************************/
    public void onAnimationPlay() {

    }

    /**************************************************************************
     * Actions effectuer lorsque l'animation s'arrête
     *************************************************************************/
    public void onAnimationStop() {

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




    /**
     * Mise à jour de la ModelView pour prendre en compte les
     * modification apportées à l'ojet
     * taille - position - rotation
     * <p/>
     * /!\ l'ordre où on applique les transformation et hyper important
     * il faut toujours faire : translation*rotation*scale
     */

    public void updateModelView() {
        float[] wrkRotationMatrix = new float[16];
        float[] modelView = new float[16];

        //on initialise une matrice identitaire
        Matrix.setIdentityM(modelView, 0);

        //on fabrique une matrice de déplacement vers les coordonnées x,y,z
        Matrix.translateM(modelView, 0, this.getX(), this.getY(), this.getZ());

        //on fabrique une matrice de rotation
        Matrix.setRotateEulerM(wrkRotationMatrix, 0, this.getAngleRADX(),
                this.getAngleRADY(), this.getAngleRADZ());

        //Calcul de la matrice ModelView
        Matrix.multiplyMM(this.mModelView, 0, modelView, 0,
                wrkRotationMatrix, 0);

        //Scales matrix m in place by sx, sy, and sz.
        Matrix.scaleM(this.mModelView, 0, this.getWidth(), this.getHeight(), 0.f);

    }


}
