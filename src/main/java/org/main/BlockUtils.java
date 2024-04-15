package org.main;

import org.engine.graphics.Material;
import org.engine.graphics.Mesh;
import org.engine.graphics.Vertex;
import org.engine.maths.Vector2f;
import org.engine.maths.Vector2i;
import org.engine.maths.Vector3f;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

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
        blockData.put(1, new BlockData( "grass", new BlockTexture(new Vector2i(0, 0), new Vector2i(32, 0), new Vector2i(48, 0), new Vector2i(48, 16), new Vector2i(48, 32), new Vector2i(48, 48))));
        blockData.put(2, new BlockData( "dirt", new BlockTexture(new Vector2i(32, 0))));
        blockData.put(3, new BlockData( "stone", new BlockTexture(new Vector2i(16, 0))));
        blockData.put(4, new BlockData( "sand", new BlockTexture(new Vector2i(32, 16))));
    }

    public BlockData getBlockData(int id) {
        return blockData.get(id);
    }

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



    public Mesh getBlockMesh(int id, int face, Vector3f position) {
        BlockData data = getBlockData(id);
        //Vector2i[] textures = data.textures.getTextures();

        // Top, bottom, front, back, left, right
        switch (face) {
            case 0:
                return new Mesh(new Vertex[] {
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, 0.5f), position), new Vector3f(0, 0, 1), getTexTopLeft(data.textures.getTop())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, 0.5f), position), new Vector3f(0, 0, 1), getTexTopRight(data.textures.getTop())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, -0.5f), position), new Vector3f(0, 0, 1), getTexBottomRight(data.textures.getTop())),
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, -0.5f), position), new Vector3f(0, 0, 1), getTexBottomLeft(data.textures.getTop()))
                }, new int[] {
                        0, 1, 2,
                        0, 3, 2
                }, new Material("/textures/MinecraftTextures.png"));
            case 1:
                return new Mesh(new Vertex[] {
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, 0.5f), position), new Vector3f(0, 0, -1), getTexTopLeft(data.textures.getBottom())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, 0.5f), position), new Vector3f(0, 0, -1), getTexTopRight(data.textures.getBottom())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, -0.5f), position), new Vector3f(0, 0, -1), getTexBottomRight(data.textures.getBottom())),
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, -0.5f), position), new Vector3f(0, 0, -1), getTexBottomLeft(data.textures.getBottom()))
                }, new int[] {
                        0, 1, 2,
                        0, 3, 2
                }, new Material("/textures/MinecraftTextures.png"));
            case 2:
                return new Mesh(new Vertex[] {
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexBottomLeft(data.textures.getFront())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexBottomRight(data.textures.getFront())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexTopRight(data.textures.getFront())),
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, 0.5f), position), new Vector3f(0, 1, 0), getTexTopLeft(data.textures.getFront()))
                }, new int[] {
                        0, 1, 2,
                        0, 3, 2
                }, new Material("/textures/MinecraftTextures.png"));
            case 3:
                return new Mesh(new Vertex[] {
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexBottomLeft(data.textures.getBack())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexBottomRight(data.textures.getBack())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexTopRight(data.textures.getBack())),
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, -0.5f), position), new Vector3f(0, -1, 0), getTexTopLeft(data.textures.getBack()))
                }, new int[] {
                        0, 1, 2,
                        0, 3, 2
                }, new Material("/textures/MinecraftTextures.png"));
            case 4:
                return new Mesh(new Vertex[] {
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, 0.5f), position), new Vector3f(-1, 0, 0), getTexBottomLeft(data.textures.getLeft())),
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, -0.5f, -0.5f), position), new Vector3f(-1, 0, 0), getTexBottomRight(data.textures.getLeft())),
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, -0.5f), position), new Vector3f(-1, 0, 0), getTexTopRight(data.textures.getLeft())),
                        new Vertex(Vector3f.add(new Vector3f(-0.5f, 0.5f, 0.5f), position), new Vector3f(-1, 0, 0), getTexTopLeft(data.textures.getLeft()))
                }, new int[] {
                        0, 1, 2,
                        0, 3, 2
                }, new Material("/textures/MinecraftTextures.png"));
            case 5:
                return new Mesh(new Vertex[] {
                        new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, 0.5f), position), new Vector3f(1, 0, 0), getTexBottomLeft(data.textures.getRight())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, -0.5f, -0.5f), position), new Vector3f(1, 0, 0), getTexBottomRight(data.textures.getRight())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, -0.5f), position), new Vector3f(1, 0, 0), getTexTopRight(data.textures.getRight())),
                        new Vertex(Vector3f.add(new Vector3f(0.5f, 0.5f, 0.5f), position), new Vector3f(1, 0, 0), getTexTopLeft(data.textures.getRight()))
                }, new int[] {
                        0, 1, 2,
                        0, 3, 2
                }, new Material("/textures/MinecraftTextures.png"));


            default:
                System.out.println("Face not implemented");
        }


        /*var mesh = new Mesh(new Vertex[] {
                new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0, 0, 1), getTextureCoords(textures[0])),
                new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(0, 0, 1), getTextureCoords(textures[0])),
                new Vertex(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(0, 0, 1), getTextureCoords(textures[0])),
                new Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0, 0, 1), getTextureCoords(textures[0])),

                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0, 0, -1), getTextureCoords(textures[1])),
                new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(0, 0, -1), getTextureCoords(textures[1])),
                new Vertex(new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(0, 0, -1), getTextureCoords(textures[1])),
                new Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0, 0, -1), getTextureCoords(textures[1])),

                new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(0, 1, 0), getTextureCoords(textures[2])),
                new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(0, 1, 0), getTextureCoords(textures[2])),
                new Vertex(new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(0, 1, 0), getTextureCoords(textures[2])),
                new Vertex(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(0, 1, 0), getTextureCoords(textures[2])),

                new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0, -1, 0), getTextureCoords(textures[3])),
                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0, -1, 0), getTextureCoords(textures[3])),
                new Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0, -1, 0), getTextureCoords(textures[3])),
                new Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0, -1, 0), getTextureCoords(textures[3])),

                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(-1, 0, 0), getTextureCoords(textures[4])),
                new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(-1, 0, 0), getTextureCoords(textures[4])),
                new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(-1, 0, 0), getTextureCoords(textures[4])),
                new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(-1, 0, 0), getTextureCoords(textures[4])),

                new Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(1, 0, 0), getTextureCoords(textures[5])),
                new Vertex(new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(1, 0, 0), getTextureCoords(textures[5])),
                new Vertex(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1, 0, 0), getTextureCoords(textures[5])),
                new Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(1, 0, 0), getTextureCoords(textures[5]))
            },
            new int[] {
                0, 1, 2,
                0, 2, 3,

                4, 5, 6,
                4, 6, 7,

                8, 9, 10,
                8, 10, 11,

                12, 13, 14,
                12, 14, 15,

                16, 17, 18,
                16, 18, 19,

                20, 21, 22,
                20, 22, 23
            },
            new Material("/textures/MinecraftTextures.png")
        );*/
        return null;
    }



}
