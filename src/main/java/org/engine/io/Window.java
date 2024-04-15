package org.engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.engine.math.Vector3f;

public class Window {

    private int width, height;
    private String title;
    private long window;

    public Input input;
    private Vector3f backgroundColor;

    private int[] windowPosX = new int[1], windowPosY = new int[1];

    private GLFWWindowSizeCallback windowSizeCallback;

    private boolean isResized;

    private boolean isFullscreen;
    public static int frames;
    public static long time;


    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("Error: GLFW failed to initialize!");
            return;
        }

        window = GLFW.glfwCreateWindow(width, height, "Game", false ? GLFW.glfwGetPrimaryMonitor() : 0, 0);

        if (window == 0) {
            System.err.println("Error: Window failed to create!");
            return;
        }

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        windowPosX[0] = (videoMode.width() - width) / 2;
        windowPosY[0] = (videoMode.height() - height) / 2;

        GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        input = new Input();

        createCallbacks();


        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);



        time = System.currentTimeMillis();

    }

    public void update() {
        if (isResized) {
            GL11.glViewport(0, 0, width, height);
            isResized = false;
        }
        GL11.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();

        frames++;
        if (System.currentTimeMillis() > time + 1000) {
            // Append the fps to the title
            GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void destroy() {
        input.destroy();
        windowSizeCallback.free();
        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwDestroyWindow(window);
    }

    public void setBackgroundColor(Vector3f color) {
        backgroundColor = color;
    }

    private void createCallbacks() {

        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };
        GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
        GLFW.glfwSetWindowSizeCallback(window, windowSizeCallback);
        GLFW.glfwSetScrollCallback(window, input.getScrollCallback());
    }


    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean isFullscreen) {
        boolean oldFullscreen = this.isFullscreen;
        this.isFullscreen = isFullscreen;


        if (isFullscreen) {
            GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
            GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        } else {
            GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
        }

        if (oldFullscreen != isFullscreen) {
            isResized = true;
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public String getTitle() {
        return title;
    }


}
