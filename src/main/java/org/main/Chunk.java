package org.main;

import org.engine.graphics.Material;
import org.engine.graphics.Mesh;
import org.engine.graphics.Vertex;
import org.engine.maths.Vector3f;
import org.engine.objects.GameObject;

import java.util.ArrayList;
import java.util.Random;

public class Chunk {

    private int x, y, z;


    public int[][][] blocks;


    public final int WORLD_HEIGHT;
    public final int WORLD_WIDTH;
    public final int WORLD_DEPTH;

    public Chunk(int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        blocks = new int[sizeX][sizeY][sizeZ];
        WORLD_HEIGHT = sizeY;
        WORLD_WIDTH = sizeX;
        WORLD_DEPTH = sizeZ;
    }

    public GameObject getGameObject(Material material) {
        var blockUtils = new BlockUtils();
        var vert = new ArrayList<Vertex>();
        var ind = new ArrayList<Integer>();

        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                for (int z = 0; z < WORLD_DEPTH; z++) {
                    if (blocks[x][y][z] > 0) {
                        var position = new Vector3f(x, y, z);

                        for (int i = 0; i < 6; i++) {
                            if (faceOnOutside(x, y, z, i)) {
                                blockUtils.addBlockDataToLists(blocks[x][y][z], i, position, vert, ind);
                            }
                        }

                    }
                }
            }
        }

        var vertArr = new Vertex [vert.size()];
        for (int i = 0; i < vert.size(); i++) {
            vertArr[i] = vert.get(i);
        }
        var indArr = new int [ind.size()];
        for (int i = 0; i < ind.size(); i++) {
            indArr[i] = ind.get(i);
        }



        var thisMesh = new Mesh(vertArr, indArr, material);

        thisMesh.create();


        return new GameObject(new Vector3f(x, y, z), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), thisMesh);

    }

    private boolean faceOnOutside(int x, int y, int z, int face) {
        // Check if the face is touching an air block
        switch (face) {
            case 0:
                if (y == WORLD_HEIGHT - 1) return true;
                if (blocks[x][y + 1][z] == 0) return true;
                break;
            case 1:
                if (y == 0) return true;
                if (blocks[x][y - 1][z] == 0) return true;
                break;
            case 3:
                if (z == 0) return true;
                if (blocks[x][y][z - 1] == 0) return true;
                break;
            case 2:
                if (z == WORLD_DEPTH - 1) return true;
                if (blocks[x][y][z + 1] == 0) return true;
                break;

            case 5:
                if (x == WORLD_WIDTH - 1) return true;
                if (blocks[x + 1][y][z] == 0) return true;
                break;
            case 4:
                if (x == 0) return true;
                if (blocks[x - 1][y][z] == 0) return true;
                break;

        }

        return false;

    }

    public void fillRandom() {
        var random = new Random();
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                for (int z = 0; z < WORLD_DEPTH; z++) {
                    blocks[x][y][z] = random.nextDouble() < 0.1 ? random.nextInt(10) : 0;
                }
            }
        }
    }


    public HitInfo raycast(Vector3f position, Vector3f step, float iterations) {
        var lastPosition = position;
        for (int i = 0; i < iterations; i++) {
            var hit = pointHit(position);
            if (hit.hit) {
                hit.face = faceThatTheLineSegmentIntersects(lastPosition, position, hit.x, hit.y, hit.z);
                return hit;
            }
            lastPosition = position;
            position = Vector3f.add(position, step);
        }
        return new HitInfo(false, 0, 0, 0, 0, 0);
    }

    public HitInfo pointHit(Vector3f point) {
        boolean hit = false;
        int blockId = 0;
        int x = Math.round(point.getX());
        int y = Math.round(point.getY());
        int z = Math.round(point.getZ());

        if (x >= 0 && x < WORLD_WIDTH && y >= 0 && y < WORLD_HEIGHT && z >= 0 && z < WORLD_DEPTH) {
            hit = blocks[x][y][z] != 0;
            blockId = blocks[x][y][z];
        }

        return new HitInfo(hit, blockId, x, y, z, -1);
    }

    public static int faceThatTheLineSegmentIntersects(Vector3f outside, Vector3f inside, int x, int y, int z) {

        // Represent the line as V + t * D
        Vector3f D = Vector3f.normalize(Vector3f.subtract(inside, outside));
        // Check top face
        if (outside.getY() > y + 0.5) {
            float deltaY = y + 0.5f - outside.getY();
            float t = deltaY / D.getY();
            var positionAtPlane = Vector3f.add(outside, Vector3f.multiply(D, t));
            if (positionAtPlane.getX() >= x - 0.5 && positionAtPlane.getX() <= x + 0.5 && positionAtPlane.getZ() >= z - 0.5 && positionAtPlane.getZ() <= z + 0.5) {
                return 0;
            }
        }
        // Check bottom face
        if (outside.getY() < y - 0.5) {
            float deltaY = y - 0.5f - outside.getY();
            float t = deltaY / D.getY();
            var positionAtPlane = Vector3f.add(outside, Vector3f.multiply(D, t));
            if (positionAtPlane.getX() >= x - 0.5 && positionAtPlane.getX() <= x + 0.5 && positionAtPlane.getZ() >= z - 0.5 && positionAtPlane.getZ() <= z + 0.5) {
                return 1;
            }
        }
        // Check front face
        if (outside.getZ() > z + 0.5) {
            float deltaZ = z + 0.5f - outside.getZ();
            float t = deltaZ / D.getZ();
            var positionAtPlane = Vector3f.add(outside, Vector3f.multiply(D, t));
            if (positionAtPlane.getX() >= x - 0.5 && positionAtPlane.getX() <= x + 0.5 && positionAtPlane.getY() >= y - 0.5 && positionAtPlane.getY() <= y + 0.5) {
                return 2;
            }
        }
        // Check back face
        if (outside.getZ() < z - 0.5) {
            float deltaZ = z - 0.5f - outside.getZ();
            float t = deltaZ / D.getZ();
            var positionAtPlane = Vector3f.add(outside, Vector3f.multiply(D, t));
            if (positionAtPlane.getX() >= x - 0.5 && positionAtPlane.getX() <= x + 0.5 && positionAtPlane.getY() >= y - 0.5 && positionAtPlane.getY() <= y + 0.5) {
                return 3;
            }
        }
        // Check left face
        if (outside.getX() < x - 0.5) {
            float deltaX = x - 0.5f - outside.getX();
            float t = deltaX / D.getX();
            var positionAtPlane = Vector3f.add(outside, Vector3f.multiply(D, t));
            if (positionAtPlane.getZ() >= z - 0.5 && positionAtPlane.getZ() <= z + 0.5 && positionAtPlane.getY() >= y - 0.5 && positionAtPlane.getY() <= y + 0.5) {
                return 4;
            }
        }
        // Check right face
        if (outside.getX() > x + 0.5) {
            float deltaX = x + 0.5f - outside.getX();
            float t = deltaX / D.getX();
            var positionAtPlane = Vector3f.add(outside, Vector3f.multiply(D, t));
            if (positionAtPlane.getZ() >= z - 0.5 && positionAtPlane.getZ() <= z + 0.5 && positionAtPlane.getY() >= y - 0.5 && positionAtPlane.getY() <= y + 0.5) {
                return 5;
            }
        }

        return -1;

    }


    public boolean blockInBounds(Vector3f position) {
        return position.getX() >= 0 && position.getX() < WORLD_WIDTH && position.getY() >= 0 && position.getY() < WORLD_HEIGHT && position.getZ() >= 0 && position.getZ() < WORLD_DEPTH;
    }





    public static class HitInfo {
        public boolean hit;
        public int blockId;
        public int x, y, z;
        public int face;

        public HitInfo(boolean hit, int blockId, int x, int y, int z, int face) {
            this.hit = hit;
            this.blockId = blockId;
            this.x = x;
            this.y = y;
            this.z = z;
            this.face = face;
        }
    }

    public void setBlock(int x, int y, int z, int blockId) {
        blocks[x][y][z] = blockId;
    }

}
