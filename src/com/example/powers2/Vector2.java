package com.example.powers2;

public class Vector2 {
	public float x, y;
	
	public Vector2() { }
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector2 copy(Vector2 v) {
		return new Vector2(v.x, v.y);
	}
	
	public static Vector2 add(Vector2 lhp, Vector2 rhp) {
		return new Vector2(lhp.x + rhp.x, lhp.y + rhp.y);
	}
	
	public static Vector2 sub(Vector2 lhp, Vector2 rhp) {
		return new Vector2(lhp.x - rhp.x, lhp.y - rhp.y);
	}
	
	public static Vector2 mul(float s, Vector2 v) {
		return new Vector2(s * v.x, s * v.y);
	}
	
	public static float Length(Vector2 v) {
		return (float)Math.sqrt(v.x * v.x + v.y * v.y);
	}
	
	public static Vector2 Normalize(Vector2 v) {
		float d = 1.0f / Length(v);
		return new Vector2(d * v.x, d * v.y);
	}
}
