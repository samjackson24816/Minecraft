package org.engine.graphics;

import org.lwjgl.opengl.*;

import org.engine.io.Window;
import org.engine.maths.Matrix4f;
import org.engine.objects.GameObject;
import org.main.Player;

public class Renderer {
	private Shader shader;
	private Window window;
	
	public Renderer(Window window, Shader shader) {
		this.shader = shader;
		this.window = window;
	}
	
	public void renderMesh(GameObject object, Player camera) {

		// The mesh must have set up its buffers using create() before this will work
		GL30.glBindVertexArray(object.getMesh().getVAO());


		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, object.getMesh().getMaterial().getTextureID());

		// Give the shader to the GPU, so it can render with it
		shader.bind();

		// Give information to the shader, so it can the vertices can apply the right transforms to themselves to render in perspective
		shader.setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
		shader.setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		shader.setUniform("projection", window.getProjectionMatrix());

		// Render the object
		GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);

		// Unbind the shader and the buffers
		shader.unbind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);




	}


}