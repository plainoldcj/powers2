package com.example.powers2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class GameRenderer {
	public static final int 	FIELD_SZ	= 4;
	public static final int 	NUM_TILES 	= FIELD_SZ * FIELD_SZ;
	public static final float 	TILE_SZ		= 1.0f / FIELD_SZ * 0.9f;
	public static final float	PAD_SZ		= 1.0f / FIELD_SZ - TILE_SZ;
	
	public static final Vector2 tileSzW = new Vector2(TILE_SZ, TILE_SZ);
	
	private Paint borderPaint	= new Paint();
	private Paint tilePaint 	= new Paint();
	private Paint textPaint 	= new Paint();
	
	public class Tile {
		Vector2 pos;
	}
	
	public Vector2 ToWorldSpace(int row, int col) {
		final float f = 1.0f / FIELD_SZ;
		return new Vector2(f * row, f * col);
	}
	
	public GameRenderer() {
		borderPaint.setColor(Color.BLACK);
		textPaint.setColor(Color.WHITE);
	}
	
	public void SetTypeface(Typeface typeface) {
		textPaint.setTypeface(typeface);
	}
	
	public Vector2 ToScreenSpace(Vector2 off, float scrSz, Vector2 v) {
		float scale = 1.0f / (1.0f - PAD_SZ);
		return Vector2.add(off, Vector2.mul(scrSz * scale, v));
	}
	
	public void DrawBorder(Canvas canvas, float scrSz) {
		float borderSz = scrSz * 0.85f;
		Vector2 borderOff = Vector2.mul(0.5f, new Vector2(canvas.getWidth() - borderSz, canvas.getHeight() - borderSz));

		canvas.drawRoundRect(new RectF(borderOff.x, borderOff.y, borderOff.x + borderSz, borderOff.y + borderSz), 10, 10, borderPaint);
	}
	
	public int InterpolateSolidColors(int c0, int c1, float l1) {
		float l0 = (1.0f - l1);
		int r = (int)(l0 * Color.red(c0) 	+ l1 * Color.red(c1));
		int g = (int)(l0 * Color.green(c0) 	+ l1 * Color.green(c1));
		int b = (int)(l0 * Color.blue(c0) 	+ l1 * Color.blue(c1));
		return Color.argb(255, r, g, b);
	}
	
	public int ColorFromPower(int pow) {
		float step = 1.0f / 11.0f; // assumes 1 << 11 to be max value
		float l = pow * step;
		if(0.5f >= l) return InterpolateSolidColors(Color.GRAY, Color.RED, 2.0f * l);
		else return InterpolateSolidColors(Color.RED, Color.YELLOW, 0.5f * l);
	}
	
	public void DrawTile(Canvas canvas, Vector2 fieldOff, float fieldSz, Tile tile, int pow) {
		int value = 1 << pow;
		String text = "" + value;
		
		Vector2 upperLeft = ToScreenSpace(fieldOff, fieldSz, tile.pos);
		Vector2 lowerRight = ToScreenSpace(fieldOff, fieldSz, Vector2.add(tile.pos, tileSzW));
		RectF rect = new RectF(upperLeft.x, upperLeft.y, lowerRight.x, lowerRight.y);
		
		tilePaint.setColor(ColorFromPower(pow));
		
		canvas.drawRoundRect(rect, 10, 10, tilePaint);
		
		Vector2 tileSzS = Vector2.sub(lowerRight, upperLeft);
		tileSzS = Vector2.mul(0.8f, tileSzS);
		
		textPaint.setTextSize(128.0f);
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		
		Vector2 scale = new Vector2(tileSzS.x / bounds.width(), tileSzS.y / bounds.height());
		float textScale = Math.min(scale.x, scale.y);
		textPaint.setTextSize(128.0f * textScale);
		
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		
		Vector2 center = ToScreenSpace(fieldOff, fieldSz, Vector2.add(tile.pos, Vector2.mul(0.5f, tileSzW)));
		Vector2 textCenter = Vector2.copy(center);
		
		textCenter.x -= 0.5f * bounds.width();
		textCenter.y += 0.5f * bounds.height();
		canvas.drawText(text, textCenter.x, textCenter.y, textPaint);
	}
	
	public void Draw(Canvas canvas, int[] squares) {
		float scrSz = Math.min(canvas.getWidth(), canvas.getHeight());
		
		DrawBorder(canvas, scrSz);
		
		float 	fieldSz = scrSz * 0.8f;
		Vector2 fieldOff = Vector2.mul(0.5f, new Vector2(canvas.getWidth() - fieldSz, canvas.getHeight() - fieldSz));
		
		Tile tile = new Tile();
		for(int row = 0; row < FIELD_SZ; ++row) {
			for(int col = 0; col < FIELD_SZ; ++col) {
				int sq = squares[col * FIELD_SZ + row]; // access row major to flip board. why is this necessary?
				if(0 < sq) {
					tile.pos = ToWorldSpace(row, col);
					DrawTile(canvas, fieldOff, fieldSz, tile, sq);
				}
			}
		}
	}
}
