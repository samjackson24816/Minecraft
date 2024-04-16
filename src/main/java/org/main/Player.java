package org.main;

import org.engine.io.Input;
import org.engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

import static org.engine.maths.MathUtils.modulusAngleVector;

/*
    * The player class is a container for the player character and all of its properties
    * It handles player movement, input, and interactions with the world
    * The player is a simple first person character that can move around and interact with the world
 */
public class Player {
    private Vector3f position, rotation;

    private Vector3f controlMovement = new Vector3f(0, 0, 0);
    private Vector3f velocity = new Vector3f(0, 0, 0);
    private float radius = 0.3f;
    private float moveSpeed = 0.025f, sprintSpeed = 0.045f, jumpInitialVelocity = 0.09f, mouseSensitivity = 10f;
    private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;
    private int blockSelected = 1;

    private boolean rightMouseDown = false;
    private boolean leftMouseDown = false;

    public Player(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void update(double deltaTime, Chunk chunk) {
        // Control movement collects all the input from the player and stores it in a vector
        // It is added to the velocity vector to move the player
        controlMovement = new Vector3f(0, 0, 0);

        newMouseX = Input.getMouseX();
        newMouseY = Input.getMouseY();

        boolean sprint = Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL);

        float x = (float) (Math.sin(Math.toRadians(rotation.getY())) * (sprint ? sprintSpeed : moveSpeed));
        float z = (float) (Math.cos(Math.toRadians(rotation.getY())) * (sprint ? sprintSpeed : moveSpeed));


        // Walking around
        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) controlMovement = Vector3f.add(controlMovement, new Vector3f(-z, 0, x));
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) controlMovement = Vector3f.add(controlMovement, new Vector3f(z, 0, -x));
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) controlMovement = Vector3f.add(controlMovement, new Vector3f(-x, 0, -z));
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) controlMovement = Vector3f.add(controlMovement, new Vector3f(x, 0, z));
        var touchingGround = chunk.pointHit(Vector3f.add(position, new Vector3f(0, -radius -0.01f, 0)));

        // Reset velocity if touching the ground to stop it building up
        if (touchingGround.hit) {
            velocity = new Vector3f(0, 0, 0);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE) && touchingGround.hit) {
            // Jump
            velocity.setY(velocity.getY() + jumpInitialVelocity);
        }

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);

        // I don't know why deltaTime breaks this.  I think it's because large float values make the float cast of deltaTime round to zero sometimes
        rotation = Vector3f.add(rotation, Vector3f.multiply(new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0), 0.01f)); //(float)(deltaTime)));


        oldMouseX = newMouseX;
        oldMouseY = newMouseY;

        // Apply gravity
        velocity = Vector3f.add(velocity, new Vector3f(0, -0.0025f, 0));


        var move = Vector3f.add(controlMovement, velocity);

        // Cap move at a magnitude of 0.7 to prevent the player from moving too fast and clipping
        if (Vector3f.length(move) > 0.7) {
            move = Vector3f.multiply(Vector3f.normalize(move), 0.7f);
        }

        var newPos = Vector3f.add(position, Vector3f.multiply(move, (float)deltaTime));

        // Try to move in the x, y and z directions one at a time
        var hitInfo = chunk.pointHit(new Vector3f(newPos.getX() + (move.getX() > 0 ? radius : -radius), position.getY(), position.getZ()));
        if (!hitInfo.hit) position = Vector3f.add(position, new Vector3f(move.getX(), 0, 0));
        //else System.out.println("Hit X");
        hitInfo = chunk.pointHit(new Vector3f(position.getX(), position.getY(), newPos.getZ() + (move.getZ() > 0 ? radius : -radius)));
        if (!hitInfo.hit) position = Vector3f.add(position, new Vector3f(0, 0, move.getZ()));
        //else System.out.println("Hit Z");
        hitInfo = chunk.pointHit(new Vector3f(position.getX(), newPos.getY() + (move.getY() > 0 ? radius : -radius), position.getZ()));
        if (!hitInfo.hit) position = Vector3f.add(position, new Vector3f(0, move.getY(), 0));
        //else System.out.println("Hit Y");

        // Change selected block based on number buttons
        if (Input.isKeyDown(GLFW.GLFW_KEY_1)) blockSelected = 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_2)) blockSelected = 2;
        if (Input.isKeyDown(GLFW.GLFW_KEY_3)) blockSelected = 3;
        if (Input.isKeyDown(GLFW.GLFW_KEY_4)) blockSelected = 4;
        if (Input.isKeyDown(GLFW.GLFW_KEY_5)) blockSelected = 5;
        if (Input.isKeyDown(GLFW.GLFW_KEY_6)) blockSelected = 6;
        if (Input.isKeyDown(GLFW.GLFW_KEY_7)) blockSelected = 7;
        if (Input.isKeyDown(GLFW.GLFW_KEY_8)) blockSelected = 8;
        if (Input.isKeyDown(GLFW.GLFW_KEY_9)) blockSelected = 9;
        if (Input.isKeyDown(GLFW.GLFW_KEY_0)) blockSelected = 10;

        // Get the block that the player is facing, and could interact with
        var targetBlock = chunk.raycast(position, Vector3f.multiply(getForwardNormal(), 0.1f), 50);


        if (!Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            rightMouseDown = false;
        }

        if (!Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            leftMouseDown = false;
        }

        // If the player is looking at a block, and they click the left mouse button, destroy the block
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !leftMouseDown && targetBlock.hit) {
            leftMouseDown = true;
            chunk.setBlock(targetBlock.x, targetBlock.y, targetBlock.z, 0);
        }

        // If the player is looking at a block, and they click the right mouse button, place a block
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)  && !rightMouseDown && targetBlock.hit) {
            rightMouseDown = true;
            // We need to make sure that the block we are placing is not intersecting with the player, is in bounds, and is not already occupied
            var newBlockPos = BlockUtils.getBlockOnFace(targetBlock.x, targetBlock.y, targetBlock.z, targetBlock.face);
            if (newBlockPos != null && chunk.blockInBounds(newBlockPos)
                    && !playerIntersectingBlock((int)newBlockPos.getX(), (int)newBlockPos.getY(), (int)newBlockPos.getZ())
                    && chunk.blocks[Math.round(newBlockPos.getX())][Math.round(newBlockPos.getY())][Math.round(newBlockPos.getZ())] == 0) {

                // System.out.println("Placing block at " + newBlockPos.getX() + ", " + newBlockPos.getY() + ", " + newBlockPos.getZ());
                chunk.setBlock(Math.round(newBlockPos.getX()), Math.round(newBlockPos.getY()), Math.round(newBlockPos.getZ()), blockSelected);



            }
        }



        // Move the player above the world if they are falling off
        if (position.getY() < -40) {
            Random random = new Random();
            position = new Vector3f((int)(random.nextDouble() * chunk.WORLD_WIDTH), 25, (int)(random.nextDouble() * chunk.WORLD_DEPTH));
            velocity = new Vector3f(0, 0, 0);
        }
    }

    public boolean playerIntersectingBlock(int x, int y, int z) {
        return position.getX() > x - 0.5f && position.getX() < x + 0.5f && position.getY() > y - 0.5f && position.getY() < y + 0.5f && position.getZ() > z - 0.5f && position.getZ() < z + 0.5f;
    }



    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    // Get the vector that points in the direction the player is facing
    public Vector3f getForwardNormal() {
        var displayRot = modulusAngleVector(rotation);
        return Vector3f.normalize(new Vector3f((float) -Math.sin(Math.toRadians(displayRot.getY())), (float) Math.tan(Math.toRadians(displayRot.getX())), (float) -Math.cos(Math.toRadians(displayRot.getY()))));

    }

}
