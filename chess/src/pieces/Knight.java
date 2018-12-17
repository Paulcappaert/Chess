package pieces;

import java.io.IOException;

import game.Position;
import gui.Display;

public class Knight extends Piece {

	public Knight(int x, int y, int color) {
		super(x, y, color);
		
		Position[] moveLine1 = new Position[8];
		Position[] moveLine2 = new Position[8];
		Position[] moveLine3 = new Position[8];
		Position[] moveLine4 = new Position[8];
		Position[] moveLine5 = new Position[8];
		Position[] moveLine6 = new Position[8];
		Position[] moveLine7 = new Position[8];
		Position[] moveLine8 = new Position[8];
		
		moveLine1[0] = new Position(2,1);
		moveLine2[0] = new Position(2,-1);
		moveLine3[0] = new Position(1,2);
		moveLine4[0] = new Position(1,-2);
		moveLine5[0] = new Position(-2,1);
		moveLine6[0] = new Position(-2,-1);
		moveLine7[0] = new Position(-1,2);
		moveLine8[0] = new Position(-1,-2);
		
		moveLine1[1] = null;
		moveLine2[1] = null;
		moveLine3[1] = null;
		moveLine4[1] = null;
		moveLine5[1] = null;
		moveLine6[1] = null;
		moveLine7[1] = null;
		moveLine8[1] = null;
		
		moves.add(moveLine1);
		moves.add(moveLine2);
		moves.add(moveLine3);
		moves.add(moveLine4);
		moves.add(moveLine5);
		moves.add(moveLine6);
		moves.add(moveLine7);
		moves.add(moveLine8);
		
		if(color == WHITE) 
			pieceType = Display.WKNIGHT;
		else
			pieceType = Display.BKNIGHT;
	}

}
