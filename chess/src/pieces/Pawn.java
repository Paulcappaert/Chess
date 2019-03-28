package pieces;

import java.io.IOException;
import java.util.ArrayList;

import game.*;
import gui.Display;

public class Pawn extends Piece {

	private boolean canDouble;
	private boolean enPassantable;
	
	public Pawn(int x, int y, int color) {
		super(x, y, color);
		canDouble = true;
		
		Position[] moveLine1 = new Position[8];
		Position[] moveLine2 = new Position[8];
		Position[] moveLine3 = new Position[8];
		
		if(getColor() == WHITE) {
			moveLine1[0] = new Position(0,1);
			moveLine1[1] = new Position(0,2);
			moveLine2[0] = new Position(1,1);
			moveLine3[0] = new Position(-1,1);
		} else {
			moveLine1[0] = new Position(0,-1);
			moveLine1[1] = new Position(0,-2);
			moveLine2[0] = new Position(1,-1);
			moveLine3[0] = new Position(-1,-1);
		}
		moveLine1[2] = null;
		moveLine2[1] = null;
		moveLine3[1] = null;
		
		moves.add(moveLine1);
		moves.add(moveLine2);
		moves.add(moveLine3);
		
		
		if(color == WHITE) {
			pieceType = Display.WPAWN;
		} else {
			pieceType = Display.BPAWN;
		}	
		
		value = 1;
	}
	
	public ArrayList<Position> getPossibleMoves(BoardMap posMap) {
		
		//double ability may be restored if the pieces move is undone
		if(getPos().getY() == 6 || getPos().getY() == 1)
			canDouble = true;
		
		ArrayList<Position> possibleMoves = super.getPossibleMoves(posMap);
		
		ArrayList<Position> notPossibleMoves = new ArrayList<Position>();
		for(Position newPos : possibleMoves) {
			if(newPos.getX() != getPos().getX()) {
				if(posMap.getMap(newPos) + getColor() != WHITE + BLACK)
					notPossibleMoves.add(newPos);
			} else {
				if(posMap.getMap(newPos) != EMPTY)
					notPossibleMoves.add(newPos);
			}
		}
		possibleMoves.removeAll(notPossibleMoves);
		
		if(!canDouble) {
			Position doubleJump = null;
			for(Position move : possibleMoves) {
				if(Math.abs(move.getY() - getPos().getY()) == 2)
					doubleJump = move;
			}
			if(doubleJump != null)
				possibleMoves.remove(doubleJump);
		}
		
		return possibleMoves;
	}
	
	public void move(Position newPos) {
		if(Math.abs(newPos.getY() - getPos().getY()) == 2)
			enPassantable = true;
		
		super.move(newPos);
		
		canDouble = false;
	}
	
	public boolean getEnPassantable() {
		return enPassantable;
	}
	
	public void setEnPassantable(boolean c) {
		enPassantable = c;
	}

}
