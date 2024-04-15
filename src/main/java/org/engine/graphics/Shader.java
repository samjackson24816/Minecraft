package org.engine.graphics;

import org.engine.math.Vector2f;
import org.engine.math.Vector3f;
import org.engine.utils.FileUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Shader {

    private String vertexFile, fragmentFile;

    private int vertexID, fragmentID, programID;

    public Shader(String vertexPath, String fragmentPath) {
        this.vertexFile = FileUtils.loadAsString(vertexPath);
        this.fragmentFile = FileUtils.loadAsString(fragmentPath);
    }

    public void create() {


        programID = GL20.glCreateProgram();

        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(vertexID, vertexFile);

        GL20.glCompileShader(vertexID);

        if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(vertexID));
            return;
        }

        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(fragmentID, fragmentFile);
        GL20.glCompileShader(fragmentID);

        if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(fragmentID));
            return;
        }

        GL20.glAttachShader(programID, vertexID);

        GL20.glAttachShader(programID, fragmentID);

        GL20.glLinkProgram(programID);

        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(programID));
            return;
        }

        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(programID));
            return;
        }


    }

    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, boolean value) {
        GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0);
    }

    public void setUniform(String name, Vector2f value) {
        GL20.glUniform2f(getUniformLocation(name), value.x, value.y);
    }

    public void setUniform(String name, Vector3f value) {
        GL20.glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
    }


    public void setUniform(String name, Vector3f[] value) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(value.length * 3);
        float[] data = new float[value.length * 3];
        for (int i = 0; i < value.length; i++) {
            data[i * 3] = value[i].x;
            data[i * 3 + 1] = value[i].y;
            data[i * 3 + 2] = value[i].z;
        }
        buffer.put(data).flip();
        GL20.glUniform3fv(getUniformLocation(name), buffer);
    }



    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void destroy() {
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);

        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);

        GL20.glDeleteProgram(programID);
    }
}
