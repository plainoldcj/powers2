package com.example.powers2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
	
	public Game theGame = new Game();

	public GameView(Context context, AttributeSet attribs) {
		super(context, attribs);
		
		OnSwipeTouchListener listener = new OnSwipeTouchListener(context) {
			public void onSwipeTop() {
				theGame.board.up();
				invalidate();
			}
			
			public void onSwipeBottom() {
				theGame.board.down();
				invalidate();
			}
			
			public void onSwipeLeft() {
				theGame.board.left();
				invalidate();
			}
			
			public void onSwipeRight() {
				theGame.board.right();
				invalidate();
			}
		};
		setOnTouchListener(listener);
		
	}
	
	protected void onDraw(Canvas canvas) {
		theGame.Draw(canvas);
		
		this.postInvalidateDelayed(16);
	}

}
