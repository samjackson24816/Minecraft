package org.main;

import org.engine.graphics.Material;
import org.engine.graphics.Mesh;
import org.engine.graphics.Vertex;
import org.engine.maths.PerlinNoise;
import org.engine.maths.Vector2f;
import org.engine.maths.Vector2i;
import org.engine.maths.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BlockUtils {

    public class BlockData {
        public String name;

        public BlockTexture textures;

        public BlockData(String name, BlockTexture textures) {
            this.name = name;
            this.textures = textures;
        }


    }

    public class BlockTexture {
        // top, bottom, front, back, left, right
        public Vector2i[] textures;
        public BlockTexture(Vector2i top, Vector2i bottom, Vector2i front, Vector2i back, Vector2i left, Vector2i right) {
            textures = new Vector2i[] {top, bottom, front, back, left, right};
        }

        public BlockTexture(Vector2i texture) {
            textures = new Vector2i[] {texture, texture, texture, texture, texture, texture};
        }

        public BlockTexture(Vector2i top, Vector2i side, Vector2i bottom) {
            textures = new Vector2i[] {top, bottom, side, side, side, side};
        }

        public BlockTexture(Vector2i[] textures) {
            if (textures.length != 6) {
                throw new IllegalArgumentException("Textures array must have a length of 6");
            }
            this.textures = textures;
        }

        public Vector2i getTexture(int index) {
            return textures[index];
        }

        public Vector2i getTop() {
            return textures[0];
        }

        public Vector2i getBottom() {
            return textures[1];
        }

        public Vector2i getFront() {
            return textures[2];
        }

        public Vector2i getBack() {
            return textures[3];
        }

        public Vector2i getLeft() {
            return textures[4];
        }

        public Vector2i getRight() {
            return textures[5];
        }

        public Vector2i[] getTextures() {
            return textures;
        }


    }

    public HashMap<Integer, BlockData> blockData = new HashMap<>();




    public BlockUtils() {
        blockData.put(1, new BlockData( "grass", new BlockTexture(new Vector2i(0, 0), new Vector2i(48, 0), new Vector2i(32, 0))));
        blockData.put(2, new BlockData( "dirt", new BlockTexture(new Vector2i(32, 0))));
        blockData.put(3, new BlockData( "stone", new BlockTexture(new Vector2i(16, 0))));
        blockData.put(4, new BlockData( "sand", new BlockTexture(new Vector2i(32, 16))));
        blockData.put(5, new BlockData("bedrock", new BlockTexture(new Vector2i(16, 16))));
        blockData.put(6, new BlockData("cobblestone", new BlockTexture(new Vector2i(0, 16))));
        blockData.put(7, new BlockData("coal ore", new BlockTexture(new Vector2i(32, 32))));
        blockData.put(8, new BlockData("diamond ore", new BlockTexture(new Vector2i(32, 48))));
        blockData.put(9, new BlockData("glass", new BlockTexture(new Vector2i(16, 48))));
        blockData.put(10, new BlockData("sponge", new BlockTexture(new Vector2i(0, 48))));
    }

    public BlockData getBlockData(int id) {
        return blockData.get(id);
    }

    // Returns the texture coordinates for the top left corner of the texture, normalized between 0 and 1 so OpenGL can use it
    public Vector2f getNormalCoords(Vector2i coords) {
        return new Vector2f(coords.getX() / 64.0f, coords.getY() / 64.0f);
    }

    public Vector2f getTexTopLeft(Vector2i coords) {
        return getNormalCoords(coords);
    }

    public Vector2f getTexTopRight(Vector2i coords) {
        return getNormalCoords(new Vector2i(coords.getX() + 16, coords.getY()));
    }

    public Vector2f getTexBottomLeft(Vector2i coords) {
        return getNormalCoords(new Vector2i(coords.getX(), coords.getY() + 16));
    }

    public Vector2f getTexBottomRight(Vector2i coords) {
        return getNormalCoords(new Vector2i(coords.getX() + 16, coords.getY() + 16));
    }

    // Adds the data for the mesh to the ArrayLists that are passed into the function.  We do this so we can construct a single mesh after collecting all the block faces and putting them into these two lists
    public void addBlockDataToLists(int id, int face, Vector3f position, ArrayList<Vertex> vertices, ArrayList<Integer> indices) {

        BlockData data = getBlockData(id);



        // Top, bottom, front, back, left, right
        switch (face) {
            case 0:
                // Top
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, 0.5f), position), new Vector3f(0, 0, 1), getTexTopLeft(data.textures.getTop())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, 0.5f), position), new Vector3f(0, 0, 1), getTexTopRight(data.textures.getTop())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, -0.5f), position), new Vector3f(0, 0, 1), getTexBottomRight(data.textures.getTop())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, -0.5f), position), new Vector3f(0, 0, 1), getTexBottomLeft(data.textures.getTop())));

                break;
            case 1:
                // Bottom
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, 0.5f), position), new Vector3f(0, 0, -1), getTexTopLeft(data.textures.getBottom())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, 0.5f), position), new Vector3f(0, 0, -1), getTexTopRight(data.textures.getBottom())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, -0.5f), position), new Vector3f(0, 0, -1), getTexBottomRight(data.textures.getBottom())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, -0.5f), position), new Vector3f(0, 0, -1), getTexBottomLeft(data.textures.getBottom())));

                break;
            case 2:
                // Front
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexBottomLeft(data.textures.getFront())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexBottomRight(data.textures.getFront())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexTopRight(data.textures.getFront())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexTopLeft(data.textures.getFront())));
                break;
            case 3:
                // Back
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexBottomLeft(data.textures.getBack())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexBottomRight(data.textures.getBack())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexTopRight(data.textures.getBack())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexTopLeft(data.textures.getBack())));
                break;
            case 4:
                // Left
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, 0.5f), position), new Vector3f(-1, 0, 0), getTexBottomLeft(data.textures.getLeft())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, -0.5f), position), new Vector3f(-1, 0, 0), getTexBottomRight(data.textures.getLeft())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, -0.5f), position), new Vector3f(-1, 0, 0), getTexTopRight(data.textures.getLeft())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, 0.5f), position), new Vector3f(-1, 0, 0), getTexTopLeft(data.textures.getLeft())));
                break;
            case 5:
                // Right
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, 0.5f), position), new Vector3f(1, 0, 0), getTexBottomLeft(data.textures.getRight())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, -0.5f), position), new Vector3f(1, 0, 0), getTexBottomRight(data.textures.getRight())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, -0.5f), position), new Vector3f(1, 0, 0), getTexTopRight(data.textures.getRight())));
                vertices.add(new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, 0.5f), position), new Vector3f(1, 0, 0), getTexTopLeft(data.textures.getRight())));
                break;
            default:
                System.out.println("Face not implemented");


        }

        int indicesOffset = vertices.size() - 4;

        indices.add(indicesOffset);
        indices.add(indicesOffset + 1);
        indices.add(indicesOffset + 2);

        indices.add(indicesOffset);
        indices.add(indicesOffset + 2);
        indices.add(indicesOffset + 3);
    }


    public static Chunk generateSuperflat() {
        var chunk = new Chunk(0, 0, 0, 48, 16, 48);

        // Bottom layer is bedrock
        for (int x = 0; x < chunk.WORLD_WIDTH; x++) {
            for (int z = 0; z < chunk.WORLD_DEPTH; z++) {
                chunk.blocks[x][0][z] = 5;
            }
        }

        // Next 3 layers are stone
        for (int y = 1; y < 4; y++) {
            for (int x = 0; x < chunk.WORLD_WIDTH; x++) {
                for (int z = 0; z < chunk.WORLD_DEPTH; z++) {
                    chunk.blocks[x][y][z] = 3;
                }
            }
        }

        // Next 3 layers are dirt
        for (int y = 4; y < 7; y++) {
            for (int x = 0; x < chunk.WORLD_WIDTH; x++) {
                for (int z = 0; z < chunk.WORLD_DEPTH; z++) {
                    chunk.blocks[x][y][z] = 2;
                }
            }
        }

        // Top layer is grass
        for (int x = 0; x < chunk.WORLD_WIDTH; x++) {
            for (int z = 0; z < chunk.WORLD_DEPTH; z++) {
                chunk.blocks[x][7][z] = 1;
            }
        }

        return chunk;

    }


    public static Chunk generateHills() {
        var chunk = new Chunk(0, 0, 0, 64, 16, 64);

        Random random = new Random();

        // First layer is bedrock
        for (int x = 0; x < chunk.WORLD_WIDTH; x++) {
            for (int z = 0; z < chunk.WORLD_DEPTH; z++) {
                chunk.blocks[x][0][z] = 5;
            }
        }

        var noise = new PerlinNoise();

        for (int x = 0; x < chunk.WORLD_WIDTH; x++) {
            for (int z = 0; z < chunk.WORLD_DEPTH; z++) {
                int height = (int) (noise.noise(x / 10.0, z / 10.0) * 6) + 8;
                for (int y = 1; y < height / 2; y++) {
                    var d = random.nextDouble();
                    if (d < 0.005) {
                        chunk.blocks[x][y][z] = 8;
                    } else if (d < 0.02) {
                        chunk.blocks[x][y][z] = 7;
                    } else {
                        chunk.blocks[x][y][z] = 3;
                    }
                }
                for (int y = height / 2; y < height; y++) {
                    chunk.blocks[x][y][z] = 2;
                }
                chunk.blocks[x][height][z] = 1;
            }
        }

        return chunk;
    }

    public static Vector3f getBlockOnFace(int x, int y, int z, int face) {
        var pos = new Vector3f(x, y, z);
        switch (face) {
            case 0:
                return Vector3f.add(pos, new Vector3f(0, 1, 0));
            case 1:
                return Vector3f.add(pos, new Vector3f(0, -1, 0));
            case 2:
                return Vector3f.add(pos, new Vector3f(0, 0, 1));
            case 3:
                return Vector3f.add(pos, new Vector3f(0, 0, -1));
            case 4:
                return Vector3f.add(pos, new Vector3f(-1, 0, 0));
            case 5:
                return Vector3f.add(pos, new Vector3f(1, 0, 0));
        }
        System.err.println("Invalid face: " + face);
        return null;
    }



}
