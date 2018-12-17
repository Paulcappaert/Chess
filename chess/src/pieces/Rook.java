package pieces;

import java.io.IOException;

import game.Position;
import gui.Display;

public class Rook extends Piece {

	public Rook(int x, int y, int color) {
		super(x, y, color);
		
		Position[] moveLine1 = new Position[7];
		Position[] moveLine2 = new Position[7];
		Position[] moveLine3 = new Position[7];
		Position[] moveLine4 = new Position[7];
		for(int i = 1;i < 8;i++) {
			moveLine1[i-1] = new Position(i,0);
			moveLine2[i-1] = new Position(-i,0);
			moveLine3[i-1] = new Position(0,-i);
			moveLine4[i-1] = new Position(0,i);
		}
		moves.add(moveLine1);
		moves.add(moveLine2);
		moves.add(moveLine3);
		moves.add(moveLine4);
		
		if(color == WHITE)
			pieceType = Display.WROOK;
		else
			pieceType = Display.BROOK;
	}

}
