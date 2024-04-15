package org.engine.maths;

public class Vector2i {
	private int x, y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector2i add(Vector2i vector1, Vector2i vector2) {
		return new Vector2i(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
	}
	
	public static Vector2i subtract(Vector2i vector1, Vector2i vector2) {
		return new Vector2i(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
	}
	
	public static Vector2i multiply(Vector2i vector1, Vector2i vector2) {
		return new Vector2i(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY());
	}
	
	public static Vector2i divide(Vector2i vector1, Vector2i vector2) {
		return new Vector2i(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY());
	}
	
	public static float length(Vector2i vector) {
		return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
	}

	
	public static float dot(Vector2i vector1, Vector2i vector2) {
		return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2i other = (Vector2i) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}