package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private String vertexSrc;
    private String fragmentSrc;
    private String filepath;

    private int vertexID, fragmentID, shaderProgramID;
    private int vaoID, vboID, eboID;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSrc = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSrc = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSrc = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSrc = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

        } catch (IOException err) {
            err.printStackTrace();
            assert false : "Error: Could not open Shader File: '" + filepath + "'";
        }

        System.out.println(vertexSrc);
        System.out.println(fragmentSrc);
    }

    public void compile() {

        /////////////////////////////////////////////////
        // Compile & link Shaders
        /////////////////////////////////////////////////


        // Vertex Shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Fragment Shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "\n\tShader program linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }
}
