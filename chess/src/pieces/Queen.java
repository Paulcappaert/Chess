package pieces;

import java.io.IOException;

import game.Position;
import gui.Display;

public class Queen extends Piece {

	public Queen(int x, int y, int color) {
		super(x, y, color);
		
		Position[] moveLine1 = new Position[7];
		Position[] moveLine2 = new Position[7];
		Position[] moveLine3 = new Position[7];
		Position[] moveLine4 = new Position[7];
		Position[] moveLine5 = new Position[7];
		Position[] moveLine6 = new Position[7];
		Position[] moveLine7 = new Position[7];
		Position[] moveLine8 = new Position[7];
		for(int i = 1;i < 8;i++) {
			moveLine1[i-1] = new Position(i,i);
			moveLine2[i-1] = new Position(-i,i);
			moveLine3[i-1] = new Position(i,-i);
			moveLine4[i-1] = new Position(-i,-i);
			moveLine5[i-1] = new Position(i,0);
			moveLine6[i-1] = new Position(-i,0);
			moveLine7[i-1] = new Position(0,-i);
			moveLine8[i-1] = new Position(0,i);
		}
		moves.add(moveLine1);
		moves.add(moveLine2);
		moves.add(moveLine3);
		moves.add(moveLine4);
		moves.add(moveLine5);
		moves.add(moveLine6);
		moves.add(moveLine7);
		moves.add(moveLine8);
		
		if(color == WHITE)
			pieceType = Display.WQUEEN;
		else
			pieceType = Display.BQUEEN;
		
		value = 9;
	}

}
