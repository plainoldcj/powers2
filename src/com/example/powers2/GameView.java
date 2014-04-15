package com.example.powers2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
	
	private GameBoard	board = null;
	public GameRenderer renderer = null;

	public GameView(Context context, AttributeSet attribs) {
		super(context, attribs);
		
		OnSwipeTouchListener listener = new OnSwipeTouchListener(context) {
			public void onSwipeTop() {
				if(null != board) board.up();
				invalidate();
			}
			
			public void onSwipeBottom() {
				if(null != board) board.down();
				invalidate();
			}
			
			public void onSwipeLeft() {
				if(null != board) board.left();
				invalidate();
			}
			
			public void onSwipeRight() {
				if(null != board) board.right();
				invalidate();
			}
		};
		setOnTouchListener(listener);
		
	}
	
	protected void onDraw(Canvas canvas) {
		if(null != board && null != renderer) renderer.Draw(canvas, board.getSquares());
	}

	public void setBoard(GameBoard board) {
		this.board = board;
		invalidate();
	}

}
