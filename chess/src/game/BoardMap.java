package game;

import pieces.Piece;

public class BoardMap {
	
	private int[][] map;
	private int[][] prevMap;
	
	public BoardMap() {
		map = new int[8][8];
		prevMap = new int[8][8];
		for(int i = 0;i < 8;i++) {
			for(int j = 0;j < 8;j++) {
				map[i][j] = 0;
				prevMap[i][j] = 0;
			}
		}
	}
	
	public void setMap(Position pos,int val) {
		map[pos.getX()][pos.getY()] = val;
	}
	
	public int getMap(Position pos) {
		return map[pos.getX()][pos.getY()];
	}
	
	public int getPrevMap(Position pos) {
		return prevMap[pos.getX()][pos.getY()];
	}
	
	public void saveCurrent() {
		for(int i = 0;i < 8;i++) {
			for(int j = 0;j < 8;j++) {
				prevMap[i][j] = map[i][j];
			}
		}
	}
	
	public void restore() {
		for(int i = 0;i < 8;i++) {
			for(int j = 0;j < 8;j++) {
				map[i][j] = prevMap[i][j];
			}
		}
	}
	
	public void clear() {
		for(int i = 0;i < 8;i++) {
			for(int j = 0;j < 8;j++) {
				map[i][j] = Piece.EMPTY;
			}
		}
	}
}
