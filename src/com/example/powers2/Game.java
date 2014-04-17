package com.example.powers2;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Canvas;

public class Game {
	public static final int 	FIELD_SZ	= 4;
	public static final int 	NUM_TILES 	= FIELD_SZ * FIELD_SZ;
	public static final float	VELOCITY	= 4.0f;
	public static final float	POP_DUR		= 0.25f;
	public static final float	POP_SCALE	= 1.25f;
	
	long start = 0;
	
	public static class Tile {
		Vector2 pos;
		float	scale = 1.0f;
		
		int		pow = 1;
		
		Tile	master = null;
		
		private Vector2 pos_v0, pos_v1;
		private float	pos_t, pos_dur;
		
		private float	scl_v0, scl_v1;
		private float	scl_t, scl_dur;
		
		private boolean	isMoving 	= false;
		private boolean isPopping	= false;
		private boolean isDead		= false;
		
		public boolean IsMaster() { return null == master; }
		
		public boolean IsDead() { return isDead; }

		public void SetMaster(Tile master) { this.master = master; }
		
		public void MoveTo(Vector2 dst, float dur) {
			pos_v0 = Vector2.copy(pos);
			pos_v1 = Vector2.copy(dst);
			pos_t = 0.0f;
			pos_dur = Vector2.Length(Vector2.sub(pos_v1, pos_v0)) / VELOCITY;
			isMoving = true;
		}
		
		public void Pop() {
			scl_v0 = 1.0f;
			scl_v1 = POP_SCALE;
			scl_t = 0.0f;
			scl_dur = POP_DUR;
			isPopping = true;
		}
		
		public void MoveTo(int row, int col) {
			MoveTo(ToWorldSpace(col, row), 0.1f);
		}
		
		public void MoveTo(int row, int col, boolean transpose) {
			if(transpose) {
				int tmp = row;
				row = col;
				col = tmp;
			}
			MoveTo(row, col);
		}
		
		public void Update(float secsPassed) {
			if(isMoving) {
				Vector2 dir = Vector2.Normalize(Vector2.sub(pos_v1, pos_v0));
				pos = Vector2.add(pos, Vector2.mul(VELOCITY * secsPassed, dir));
				pos_t += secsPassed;
				if(pos_dur < pos_t) {
					pos = pos_v1;
					isMoving = false;
					
					if(null != master) {
						master.pow += 1;
						master.Pop();
						isDead = true;
					}
				}
			}
			
			if(isPopping) {
				scl_t += secsPassed;
				float l = (float)Math.PI * scl_t / scl_dur;
				scale = scl_v0 + (scl_v1 - scl_v0) * (float)Math.sin(l);
				if(scl_dur < scl_t) {
					scale = scl_v0;
					isPopping = false;
				}
			}
		}
	}
	
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	
	public GameBoard	board = null;
	public GameRenderer	renderer = new GameRenderer();
	
	public static Vector2 ToWorldSpace(int row, int col) {
		final float f = 1.0f / FIELD_SZ;
		return new Vector2(f * row, f * col);
	}
	
	public Game() { NewGame(); }
	
	public void NewGame() {
		tiles.clear();
		
		board = new GameBoard();
		board.theGame = this;
		board.Init();
	}
	
	public Tile SpawnTile(int row, int col) {
		Tile tile = new Tile();
		tile.pos = ToWorldSpace(col, row);
		tiles.add(tile);
		return tile;
	}
	
	public Tile SpawnTile(int row, int col, boolean transpose) {
		if(transpose) {
			int tmp = row;
			row = col;
			col = tmp;
		}
		return SpawnTile(row, col);
	}
	
	public void DestroyTile(Tile tile) {
		tiles.remove(tile);
	}
	
	private void DestroyDeadTiles() {
		Iterator<Tile> it = tiles.iterator();
		while(it.hasNext()) {
			Tile t = it.next();
			if(t.IsDead()) it.remove();
		}
	}
	
	void Draw(Canvas canvas) {
		long stop = System.currentTimeMillis();
		float secsPassed = 0.001f * (stop - start);
		start = stop;
		for(Tile tile : tiles) tile.Update(secsPassed);
		
		DestroyDeadTiles();
		
		renderer.Draw(canvas, tiles);
	}
}
