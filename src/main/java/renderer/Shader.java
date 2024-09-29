package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                vertexSrc = splitString[1];
            } else if (secondPattern.equals("fragment")) {
                fragmentSrc = splitString[1];
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

    }

    public void use() {

    }

    public void detach() {

    }
}
