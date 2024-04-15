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


    private int[][][] blocks;


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
                    blocks[x][y][z] = random.nextDouble() < 0.1 ? random.nextInt(4) : 0;
                }
            }
        }
    }


    public HitInfo raycast(Vector3f position, Vector3f step, float iterations) {
        for (int i = 0; i < iterations; i++) {
            var hit = pointHit(position);
            if (hit.hit) {
                return hit;
            }
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

        return new HitInfo(hit, blockId, x, y, z, 0);
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
