package com.example.powers2;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class GameRenderer {
	public static final float 	TILE_SZ		= 1.0f / Game.FIELD_SZ * 0.9f;
	public static final float	PAD_SZ		= 1.0f / Game.FIELD_SZ - TILE_SZ;
	
	public static final Vector2 tileSzW = new Vector2(TILE_SZ, TILE_SZ);
	
	private Paint borderPaint	= new Paint();
	private Paint tilePaint 	= new Paint();
	private Paint textPaint 	= new Paint();
	
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
	
	public int InterpolateColorsFixedAlpha(int c0, int c1, float alpha, float l1) {
		float l0 = (1.0f - l1);
		int r = (int)(l0 * Color.red(c0) 	+ l1 * Color.red(c1));
		int g = (int)(l0 * Color.green(c0) 	+ l1 * Color.green(c1));
		int b = (int)(l0 * Color.blue(c0) 	+ l1 * Color.blue(c1));
		return Color.argb((int)(255.0f * alpha), r, g, b);
	}
	
	public int ColorFromPower(int pow) {
		float step = 1.0f / 11.0f; // assumes 1 << 11 to be max value
		float l = pow * step;
		if(0.5f >= l) return InterpolateSolidColors(Color.GRAY, Color.RED, 2.0f * l);
		else return InterpolateSolidColors(Color.RED, Color.YELLOW, 0.5f * l);
	}
	
	public int ColorFromPowerFixedAlpha(int pow, float alpha) {
		float step = 1.0f / 11.0f; // assumes 1 << 11 to be max value
		float l = pow * step;
		if(0.5f >= l) return InterpolateColorsFixedAlpha(Color.GRAY, Color.RED, alpha, 2.0f * l);
		else return InterpolateColorsFixedAlpha(Color.RED, Color.YELLOW, alpha, 0.5f * l);
	}
	
	public void DrawTile(Canvas canvas, Vector2 fieldOff, float fieldSz, Game.Tile tile) {
		int value = 1 << tile.pow;
		String text = "" + value;
		
		Vector2 upperLeft 	= ToScreenSpace(fieldOff, fieldSz, tile.pos);
		Vector2 lowerRight 	= ToScreenSpace(fieldOff, fieldSz, Vector2.add(tile.pos, tileSzW));
		Vector2 tileSzS 	= Vector2.sub(lowerRight, upperLeft);
		Vector2 center 		= Vector2.add(upperLeft, Vector2.mul(0.5f, tileSzS));
		
		canvas.save();
		canvas.rotate(tile.rot, center.x, center.y);
		canvas.scale(tile.scale, tile.scale, center.x, center.y);
		
		RectF rect = new RectF(upperLeft.x, upperLeft.y, lowerRight.x, lowerRight.y);
		
		tilePaint.setColor(ColorFromPowerFixedAlpha(tile.pow, tile.alpha));
		
		canvas.drawRoundRect(rect, 10, 10, tilePaint);
		
		
		tileSzS = Vector2.mul(0.8f, tileSzS);
		
		textPaint.setTextSize(128.0f);
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		
		Vector2 scale = new Vector2(tileSzS.x / bounds.width(), tileSzS.y / bounds.height());
		float textScale = Math.min(scale.x, scale.y);
		textPaint.setTextSize(128.0f * textScale);
		
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		
		Vector2 textCenter = Vector2.copy(center);
		textCenter.x -= 0.5f * bounds.width();
		textCenter.y += 0.5f * bounds.height();
		canvas.drawText(text, textCenter.x, textCenter.y, textPaint);
		
		canvas.restore();
	}
	
	public void Draw(Canvas canvas, ArrayList<Game.Tile> tiles) {
		float scrSz = Math.min(canvas.getWidth(), canvas.getHeight());
		
		canvas.drawColor(InterpolateSolidColors(Color.RED, Color.WHITE, 0.8f));
		
		DrawBorder(canvas, scrSz);
		
		float 	fieldSz = scrSz * 0.8f;
		Vector2 fieldOff = Vector2.mul(0.5f, new Vector2(canvas.getWidth() - fieldSz, canvas.getHeight() - fieldSz));
		
		for(Game.Tile tile : tiles) {
			if(!tile.IsMaster()) DrawTile(canvas, fieldOff, fieldSz, tile);
		}
		
		for(Game.Tile tile : tiles) {
			if(tile.IsMaster()) DrawTile(canvas, fieldOff, fieldSz, tile);
		}
	}
}
