package org.engine.graphics;

import org.engine.math.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class Renderer {

    public static void renderMesh(Shader shader, Mesh worldMesh, Vector3f cameraPosition, Vector3f cameraForward, float screenDistance, float screenWidth, float screenHeight) {


        Mesh mesh = worldMesh.copy();
        Vector3f camForwardN = cameraForward.normalized();

        Vector3f camToScreen = camForwardN.multiply(screenDistance);


        // Calculate the screen right vector
        Vector3f screenRight = new Vector3f(camToScreen.z, 0.0f, -camToScreen.x).normalized();

        // Calculate the screen up vector
        Vector3f screenUp = screenRight.cross(camToScreen).normalized();

        for (int i = 0; i < mesh.getVertices().length; i++) {
            Vector3f vertexPosition = mesh.getVertices()[i].getPosition();
            Vector3f direction = vertexPosition.subtract(cameraPosition).normalized();



            float angle = Vector3f.angleBetween(cameraForward, direction);

            // Cap the angle to 90 degrees to prevent vertices from being rendered behind the camera
            if (angle > Math.PI / 2.0) {
                angle = (float) (Math.PI / 2.0) - 0.01f;
            }

            float magnitude = (float) (screenDistance / Math.sin((Math.PI / 2.0) - angle));

            Vector3f vertexPositionOnWorldScreen = direction.multiply(magnitude);

            // Take the vertex on World screen and convert it to screen space by figuring out how much of the screen right and screen up vectors are needed to reach the vertex
            Vector3f vertexPositionOnScreen;
            vertexPositionOnScreen = screenRight.multiply(Vector3f.dot(vertexPositionOnWorldScreen, screenRight));
            vertexPositionOnScreen = vertexPositionOnScreen.add(screenUp.multiply(Vector3f.dot(vertexPositionOnWorldScreen, screenUp)));

            // Normalize the screen position
            vertexPositionOnScreen.x /= screenWidth;
            vertexPositionOnScreen.y /= screenHeight;

            mesh.getVertices()[i].setPosition(vertexPositionOnScreen);
        }

        mesh.create();

        GL30.glBindVertexArray(mesh.getVAO());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getMaterial().getTextureID());
        shader.bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
        shader.unbind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
