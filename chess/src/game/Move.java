package game;

import java.util.ArrayList;

import pieces.Piece;

/**
 * stores the information pertinent to a chess move at a certain point in the game
 * @author paulc
 *
 */
public class Move {
	
	public static final int STANDARD = 0;
	public static final int CAPTURE = 1;
	public static final int TWOPIECE = 2;
	public static final int PROMOTION = 3;
	
	private Piece piece1;
	private Piece piece2;
	private int type;
	private String move;
	
	public Move(Piece piece1, Piece piece2, int type) {
		//if this is a promotion, piece1 is the pawn
		this.piece1 = piece1;
		this.piece2 = piece2;
		this.type = type;
		
		
		String color;
		if(piece1.getColor() == Piece.WHITE) 
			color = "W";
		else
			color = "B";
		
		if(type == STANDARD) {
			move = color + " " + piece1.getPrevPos().toString() + " -> " + piece1.getPos().toString();
		} else if(type == CAPTURE) {
			move = color + " " + piece1.getPrevPos().toString() + " X " + piece1.getPos().toString();
		} else if(type == TWOPIECE) {
			move = color + " " + piece1.getPrevPos().toString() + " -> " + piece1.getPos().toString() + " & " +
					piece2.getPrevPos().toString() + " -> " + piece1.getPos().toString();
		} else {
			move = color + " " + " promotion ";
		}
		
	}
	
	public void undo(ArrayList<Piece> pieces) {
		if(type == STANDARD) {
			piece1.undo();
		} else if(type == CAPTURE) {
			piece1.undo();
			pieces.add(piece2);
		} else if(type == TWOPIECE) {
			piece1.undo();
			piece2.undo();
		} else {
			pieces.add(piece1);
			pieces.remove(piece2);
		}
	}
	
	public void redo(ArrayList<Piece> pieces) {
		if(type == STANDARD) {
			piece1.redo();
		} else if(type == CAPTURE) {
			piece1.redo();
			pieces.remove(piece2);
		} else if(type == TWOPIECE) {
			piece1.redo();
			piece2.redo();
		} else {
			pieces.remove(piece1);
			pieces.add(piece2);
		}
	}
	
	public String toString() {
		return move;
	}
}
