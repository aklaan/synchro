package com.familledupuis91.gamingtools.shaders;

public class ProgramShader_simple extends ProgramShader {

    public ProgramShader_simple() {
        super();
    }

    @Override
    public void initCode() {
        this.setRefId(ShaderRef.SIMPLE_WITH_TEXTURE);

        this.fragmentShaderSource = "#ifdef GL_ES \n"
                // pour les fragment shader, il n'existe pas de de précision par défaut
                // il est donc necessaire de la précisier soit pour tous (comme
                // ici) ou pour chaque variable.
                + " precision highp float; \n"
                + " #endif \n"
                + " uniform sampler2D " + ProgramShader.FSH_UNIFORM_TEXTURE + ";"

                + " uniform vec4 " + ProgramShader.FSH_UNIFORM_AMBIANT_COLOR_RGBA + ";"
                + " varying vec2 vTextureCoord; "
                + " varying vec4 vVertexColor;"
                + " void main() {"

                //+ "gl_FragColor =  vec4(1.,0.,1.,1.0);"

//                 + "gl_FragColor = " + this.FSH_UNIFORM_AMBIANT_COLOR_RGBA

                + "gl_FragColor = texture2D(" + ProgramShader.FSH_UNIFORM_TEXTURE
                + ", vTextureCoord) "
                + " * "
                + this.FSH_UNIFORM_AMBIANT_COLOR_RGBA
                + " * vVertexColor"
                + "; "

                + "}";

        this.vertexShaderSource =

                // il n'y a pas de déclaration de la version GLSL a utiliser. c'est par défaut 1.0 car
                // pour openGL 2.0 c'est la même que pour opengl 1.x c'est à dire 1.00
                // ce n'est qu'avec la version 3.0 que l'on doit spécifier la version 3.0.

                // par defaut si on ne précise rien toutes les variables du vertex
                // shader sont en hight precision

                // si on réduit la précision, on gagne en rapididté mais on perd
                // potentiellement en qualité (artefacts)
                // on peu préciser la précision sur une variable en particulier
                // exemple :
                // highp vec4 position;
                // varying lowp vec4 color

                // ou bien déclarer une bonne fois pour toute en debut de programme
                // ex: precision highp float;

                "uniform mat4 " + ProgramShader.VSH_UNIFORM_MVP + ";"
                        + "uniform vec4 " + this.FSH_UNIFORM_AMBIANT_COLOR_RGBA + ";"
                        + "attribute vec3 " + ProgramShader.VSH_ATTRIB_VERTEX_COORD + ";"
                        + "attribute vec2 " + ProgramShader.VSH_ATTRIB_VERTEX_TEXTURE_COORD + ";"
                        + "attribute vec4 " + ProgramShader.VSH_ATTRIB_VERTEX_COLOR + ";"
                        + "varying vec4 vVertexColor;"
                        + "varying vec2 vTextureCoord;"

                        + "void main() {"
                        // on calcule la position du point dans l'espace "écran" via la matrice de projection

                        + " vVertexColor = " + ProgramShader.VSH_ATTRIB_VERTEX_COLOR + ";"
                        + " vTextureCoord =  " + ProgramShader.VSH_ATTRIB_VERTEX_TEXTURE_COORD + ";"

                        //le gl_pointSize n'est utile que si GL_xxxxxx est activé
                        + "gl_PointSize = 10.;"

                        // cette commande doit toujours être la dernière du vertex shader.
                        + "	gl_Position = " + ProgramShader.VSH_UNIFORM_MVP
                        + " * vec4(" + ProgramShader.VSH_ATTRIB_VERTEX_COORD + ".xyz, 1.);"
                        + "}";

    }

}
