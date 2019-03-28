package pieces;

import java.util.ArrayList;
import java.util.Observable;

import javax.swing.ImageIcon;

import game.*;

public abstract class Piece {

	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	
	private int color;
	private Position pos;
	private Position prevPos;
	private boolean hasMoved;
	protected int pieceType;
	protected ArrayList<Position[]> moves;
	private ArrayList<Position> history;
	private int currMove;
	protected int value;
	
	public Piece(int x, int y, int color) {
		this.color = color;
		this.pos = new Position(x,y);
		moves = new ArrayList<Position[]>();
		history = new ArrayList<Position>();
		history.add(pos);
		hasMoved = false;
		currMove = 0;
	}
	
	/**
	 * sets to the position to the position before the last move or attack
	 */
	public void undo() {
		if(currMove > 0) {
			currMove--;
			pos = history.get(currMove);
		}
	}
	
	public void redo() {
		if(currMove < history.size() - 1) {
			currMove++;
			pos = history.get(currMove);
		}
	}
	
	/**
	 * moves the piece to the new position
	 * @param newPos
	 */
	public void move(Position newPos) {
		history.add(newPos);
		pos = newPos;
		currMove++;
		
		//deletes all of the history after the current move
		while(currMove != history.size() - 1) {
			history.remove(currMove);
		}
	}

	/**
	 * returns an arraylist of move that are possible given this piece's type of movement
	 * @param posMap
	 * @return
	 */
	public ArrayList<Position> getPossibleMoves(BoardMap posMap) {
		ArrayList<Position> possibleMoves = new ArrayList<Position>();
		for(Position[] moveLine : moves) {
			for(int i = 0;i < 7;i++) {
				if(moveLine[i] == null) {
					break;
				} else {
					Position newPos = pos.addPosition(moveLine[i]);
					if(newPos == null)
						break;
					if(posMap.getMap(newPos) == EMPTY) 
						possibleMoves.add(newPos);
					else if(posMap.getMap(newPos) != color) {
						possibleMoves.add(newPos);
						break;
					} else if(posMap.getMap(newPos) == color) {
						break;
					}
						
				}
			}
		}
		return possibleMoves;
	}

	public ArrayList<Position> getPossibleAttacks(BoardMap posMap) {
		return getPossibleMoves(posMap);
	}

	public boolean isAt(Position newPos) {
		return pos.isEqualTo(newPos);
	}

	public Position getPos() {
		return pos;
	}
	
	public Position getPrevPos() {
		if(currMove > 0)
			return history.get(currMove - 1);
		else
			return null;
	}

	public int getColor() {
		return color;
	}

	public int getPieceType() {
		return pieceType;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	

}
