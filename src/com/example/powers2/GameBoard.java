package com.example.powers2;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class GameBoard {

	// NOTE:
	// tile (row, col) is occupied iff 0 < squares(row, col)
	
	// private static final String TAG = GameBoard.class.getName();

	private int[] squares = new int[ROWS * COLS];
	public static final int ROWS = 4;
	public static final int COLS = ROWS;

	public GameBoard() {
		int occupied = 0;
		while (occupied < 2) {
			int r = (int) Math.floor(Math.random() * squares.length);
			Log.d("TAG", "" + r);
			if (squares[r] == 0) {
				squares[r] = 1;
				occupied++;
			}
		}
	}

	// NOTE: GameRenderer assumes column major layout
	public int[] getSquares() { return squares; }
	
	public int get(int row, int col) {
		return squares[row * COLS + col];
	}

	private int get(int row, int col, boolean transpose) {
		return transpose ? get(col, row) : get(row, col);
	}

	private void set(int row, int col, int n) {
		squares[row * COLS + col] = n;
	}

	private void set(int row, int col, boolean transpose, int n) {
		if (transpose) {
			set(col, row, n);
		} else {
			set(row, col, n);
		}
	}

	public void left() {
		shift(true, true);
	}

	public void right() {
		shift(true, false);
	}

	public void up() {
		shift(false, true);
	}

	public void down() {
		shift(false, false);
	}

	private void shift(boolean horizontal, boolean inc) {
		List<Position> positions = new ArrayList<Position>();
		boolean transpose = !horizontal;
		int rows = horizontal ? ROWS : COLS;
		int cols = horizontal ? COLS : ROWS;
		for (int row = 0; row < rows; row++) {
			Deque<Integer> deque = new LinkedList<Integer>();
			int col = inc ? 0 : cols - 1;
			while (inc ? col < cols : col > -1) {
				int n = get(row, col, transpose);
				if (n > 0) {
					deque.add(get(row, col, transpose));
				}
				col = next(col, inc);
			}
			col = inc ? 0 : cols - 1;
			while (inc ? col < cols : col > -1) {
				if (deque.size() > 0) {
					int x = deque.pop();
					set(row, col, transpose, x);
					if (deque.size() > 0 && deque.peek() == x) {
						set(row, col, transpose, deque.pop() + 1);
					}
				} else {
					set(row, col, transpose, 0);
					positions.add(new Position(row, col, transpose));
				}
				col = next(col, inc);
			}
		}
		if (positions.size() > 0) {
			int i = (int) Math.floor(Math.random() * positions.size());
			Position position = positions.get(i);
			set(position.row, position.col, position.transpose, 1);
		}
	}

	private static class Position {
		public final int row;
		public final int col;
		public final boolean transpose;

		public Position(int row, int col, boolean transpose) {
			super();
			this.row = row;
			this.col = col;
			this.transpose = transpose;
		}
	}

	private int next(int col, boolean inc) {
		return inc ? col + 1 : col - 1;
	}

}