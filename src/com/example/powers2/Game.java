package com.example.powers2;

import android.graphics.Canvas;

public class Game {
	public static class Tile {
		Vector2 pos;
	}
	
	public GameBoard	board = new GameBoard();
	public GameRenderer	renderer = new GameRenderer();
	
	void Draw(Canvas canvas) {
		renderer.Draw(canvas, board.getSquares());
	}
}
