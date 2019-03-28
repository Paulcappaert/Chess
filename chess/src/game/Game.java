package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.ImageIcon;

import gui.Display;

import pieces.*;

public class Game {
	
	private ArrayList<Piece> pieces;
	private ArrayList<Move> history;
	BoardMap posMap,threatMap;
	int[][] displayMap;
	private King wKing,bKing;
	int moveNum;
	boolean whiteTurn;
	private int currMove;
	
	public Game() {
		pieces = new ArrayList<Piece>(32);
		history = new ArrayList<Move>();
		posMap = new BoardMap();
		threatMap = new BoardMap();
		displayMap = new int[8][8];
		wKing = new King(4,0,Piece.WHITE);
		bKing = new King(4,7,Piece.BLACK);
		pieces.add(wKing);
		pieces.add(bKing);

		initialize();
		update();
		
		moveNum = 0;
		currMove = 0;
		whiteTurn = true;
	}

	/**
	 * determines what kind of movement is happening from (x1, y1) to (x2, y2)
	 * throws an exception if move is illegal
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void move(int x1, int y1, int x2, int y2) throws Exception {
		Position pos1 = new Position(x1,y1);
		Position pos2 = new Position(x2,y2);
		Piece piece = getPiece(pos1);
		
		if(piece == null)
			throw new Exception("No such piece");
		else if(whiteTurn && piece.getColor() == Piece.BLACK || !whiteTurn && piece.getColor() == Piece.WHITE) 
			throw new Exception("Cannot move that piece");
		else if(piece instanceof Pawn && pos2.getX() != pos1.getX() && posMap.getMap(pos2) == Piece.EMPTY) {
			enPassant(piece,pos2);	
		} else if(piece instanceof Pawn && (pos2.getY() == 0 || pos2.getY() == 7)) {
			promote(piece,pos2);
		} else if(piece instanceof King && Math.abs(x1 - x2) == 2 && y1 == y2) {
			castle(piece,pos2);
		} else{
			normalMove(piece,pos2);
		}
		
	}
	/**
	 * gets the value of the white material - black material
	 * returns 100 if black is in checkmate
	 * returns -100 if white is in checkmate
	 * @return
	 */
	public int value() {
		int val = 0;
		for(Piece p : pieces) {
			if(p.getColor() == Piece.WHITE)
				val += p.getValue();
			else
				val -= p.getValue();
		}
		return val;
	}
	
	/**
	 * undoes the last move, if there is a last move
	 */
	public void undo() {
		if(currMove > 0) {
			currMove--;
			history.get(currMove).undo(pieces);
			update();
			updateDisplayMap(null,null);
			whiteTurn = !whiteTurn; 
		}
	}
	
	public void redo() {
		if(currMove < moveNum) {
			history.get(currMove).redo(pieces);
			update();
			updateDisplayMap(null,null);
			whiteTurn = !whiteTurn;
			currMove++;
		}
	}
	
	/**
	 * gets all of the possible moves for one color
	 * @param color
	 * @return
	 */
	public ArrayList<Move> getPossibleMoves(int color) {
		//Todo
		return null;
	}

	/** 
	 * tells the piece at (x1,y1) to attack the piece on (x2,y2) (the piece may not necessarily move to (x2,y2))
	 * and removes the piece at (x2,y2)
	 * updates all of the maps
	 * throws an exception if move is illegal
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void normalMove(Piece piece, Position pos2) throws Exception {
		
		//determines if the piece can attack pos2
		ArrayList<Position> possibleMoves = piece.getPossibleMoves(posMap);
		boolean movePossible = false;
		for(Position pos : possibleMoves) {
			if(pos.isEqualTo(pos2)) {
				movePossible = true;
				break;
			}
		}
		
		//checks if there is a piece to be captured in this move
		Piece capturePiece = getPiece(pos2);
		if(capturePiece != null) {
			pieces.remove(capturePiece);
		}
		
		//moves the piece to its empty square
		if(movePossible) {
			piece.move(pos2);
		} else {
			if(capturePiece != null)
				pieces.add(capturePiece);
			throw new Exception("move not legal");
		}
		
		//checks if this move creates an illegal position
		endMove(piece,capturePiece,null);		
		
	}
	
	/**
	 * performs an en passant with the given pawn, if the move is legal
	 * @param piece
	 * @param pos2
	 * @throws Exception
	 */
	public void enPassant(Piece piece, Position pos2) throws Exception {
		piece = (Pawn) piece;
		Position capturePos, prevPos;
		if(piece.getColor() == Piece.WHITE) {
			capturePos = new Position(pos2.getX(),pos2.getY() - 1);
		} else {
			capturePos = new Position(pos2.getX(),pos2.getY() + 1);
		}
		
		prevPos = piece.getPos();
		
		Piece capturePiece = getPiece(capturePos);
		
		if(capturePiece == null || !(capturePiece instanceof Pawn) || ((Pawn) capturePiece).getEnPassantable() == false) {
			throw new Exception("move not legal");
		} else {
			pieces.remove(capturePiece);
			piece.move(pos2);
			
			endMove(piece,capturePiece,null);
		} 
	}
	
	/**
	 * performs a castle with the king and rook if the move is legal
	 * @param piece
	 * @param pos2
	 * @throws Exception
	 */
	public void castle(Piece piece, Position pos2) throws Exception {
		King king = (King) piece;
		if(king.hasMoved() == false) {
			
			//cannot castle if the king is in check
			if(king.underThreat(threatMap))
				throw new Exception("Illegal Move");
				
			Position checkPosition = new Position(king.getPos().getX(),king.getPos().getY());
			int directionOfCastle = pos2.getX() - king.getPos().getX() > 0 ? 1 : -1;
			boolean canCastle = true;
			checkPosition.move(directionOfCastle, 0);
			Piece rook = null;
			
			//travels between the rook and king to see if there are any obstructions/threats
			while(true) {
				
				//used to check positions between king and rook, should eventually become the rook
				rook = getPiece(checkPosition);
				if(threatMap.getMap(checkPosition) != king.getColor() && threatMap.getMap(checkPosition) != Piece.EMPTY)
					canCastle = false;
				
				if(rook == null)
					if(checkPosition.getX() == 0 || checkPosition.getX() == 7) {
						canCastle = false;
						break;
					} else {
						checkPosition.move(directionOfCastle, 0);
					}
				else if(rook instanceof Rook)
					break;
				else {
					canCastle = false;
					break;
				}
			}
				
			if(canCastle) {
				if(rook.hasMoved() == false) {
					
					//moves the king and the rook into position
					king.move(pos2);
					Position rookPos = new Position(pos2.getX() - directionOfCastle,pos2.getY());
					rook.move(rookPos);
					rook.setMoved(true);
							
					//We should be certain the move is legal if we get to this point
					endMove(king,null,rook);
				}	
				
			}
		}
	}
	
	public void promote(Piece piece, Position pos2) throws Exception {
		// TODO
	}
	
	/**
	 * finalizes a move. 
	 * updates all of the maps
	 * checks for illegal positions to undo if necessary
	 * updates the display map
	 * 
	 * @param piece
	 * @param capturePiece
	 * @param secondPiece
	 * @throws Exception
	 */
	public void endMove(Piece piece, Piece capturePiece,Piece secondPiece) throws Exception {
		
		update();
		boolean legal = isLegalPosition();
		
		if(legal) {
			
			//for the rook in a castle move
			if(secondPiece != null) {
				updateDisplayMap(secondPiece,null);
			}
				
			
			//pertinent information to kings and rooks
			piece.setMoved(true);
			
			//updates all of the pawns that can no longer be attacked with en passant
			for(Piece p : pieces) {
				if(p instanceof Pawn) {
					Pawn pawn = (Pawn) p;
					if((whiteTurn && p.getColor() == Piece.BLACK) || (!whiteTurn && p.getColor() == Piece.WHITE))
						pawn.setEnPassantable(false);
				}
			}
			
			//if there is any move history that takes place after this point
			//it will be removed so it can be overwritten by the current move
			while(currMove != history.size()){
				history.remove(currMove);
			}
			
			//adds the move to history
			if(secondPiece != null) 
				history.add(new Move(piece,secondPiece,Move.TWOPIECE));
			else if(capturePiece != null)
				history.add(new Move(piece,capturePiece,Move.CAPTURE));
			else
				history.add(new Move(piece,null,Move.STANDARD));
			
			currMove++;
			moveNum = currMove;
			if(whiteTurn) 
				whiteTurn = false;
			else
				whiteTurn = true;
			updateDisplayMap(piece,capturePiece);
		} else {
			piece.undo();
			if(capturePiece != null)
				pieces.add(capturePiece);
			posMap.restore();
			threatMap.restore();
			throw new Exception("move not legal");
		}
	}
	
	/**
	 * updates the imgMap based on the piece that just moved and the piece that may have just been captured
	 * to the correct image file strings
	 * @param piece
	 * @param capturePiece
	 */
	private void updateDisplayMap(Piece piece, Piece capturePiece) {
		
		if(piece == null) {
			for(int i = 0;i < 8;i++) {
				for(int j = 0;j < 8;j++) {
					displayMap[i][j] = Display.EMPTY;
				}
			}
			
			for(Piece p : pieces) {
				displayMap[p.getPos().getX()][p.getPos().getY()] = p.getPieceType();
			}
			
		} else {
			
			if(capturePiece != null) {
				Position pos = capturePiece.getPos();
				displayMap[pos.getX()][pos.getY()] = Display.EMPTY;
			}
			
			Position pos1 = piece.getPrevPos();
			Position pos2 = piece.getPos();
			displayMap[pos1.getX()][pos1.getY()] = Display.EMPTY;
			displayMap[pos2.getX()][pos2.getY()] = piece.getPieceType();
			
			}
	}
	
	/**
	 * determines if the current position is for the current player
	 * to finish their turn
	 * @return
	 */
	private boolean isLegalPosition() {
		if(whiteTurn) { 	
			if(wKing.underThreat(threatMap))
				return false;
			else
				return true;
		} else { 				
			if(bKing.underThreat(threatMap))
				return false;
			else
				return true;
		}
	}
	
	/**
	 * determines the current state of the game
	 * 0 - no current winner
	 * 1 - white wins
	 * 2 - black wins
	 * 3 - draw
	 * @return int
	 */
	public int winner() {
		int status = 0;
		if(whiteTurn && wKing.underThreat(threatMap)) {
			
		} else if(!whiteTurn && bKing.underThreat(threatMap)) {
			
		}
		return status;
	}
	
	/**
	 * returns the piece at the given position
	 * returns null if no such piece exists
	 * @param pos
	 * @return the piece at pos
	 */
	private Piece getPiece(Position pos) {
		Piece piece = null;
		for(Piece p : pieces) {
			if(p.isAt(pos))
				piece = p;
		}
		return piece;
	}
	
	/**
	 * saves the current map setting so that it can be restored
	 * updates the position map and threat map
	 */
	private void update() {
		posMap.saveCurrent();
		threatMap.saveCurrent();
		posMap.clear();
		threatMap.clear();
		
		for(Piece p : pieces) {
			posMap.setMap(p.getPos(), p.getColor());
		}
		
		for(Piece p : pieces) {
			ArrayList<Position> possibleAttacks = p.getPossibleAttacks(posMap);
			for(Position pos : possibleAttacks) {
				threatMap.setMap(pos, p.getColor());
			}
		}
	}
	
	public void restart() {
		pieces.clear();
		history.clear();
		posMap.clear();
		threatMap.clear();
		wKing = new King(4,0,Piece.WHITE);
		bKing = new King(4,7,Piece.BLACK);
		pieces.add(wKing);
		pieces.add(bKing);

		initialize();
		update();
		
		moveNum = 0;
		currMove = 0;
		whiteTurn = true;
	}
	
	public ArrayList<String> getHistoryStrings() {
		ArrayList<String> historyStrings = new ArrayList<String>();
		for(Move move : history) {
			historyStrings.add(move.toString());
		}
		return historyStrings;
	}
	
	private void initialize() {
		
		pieces.add(new Pawn(0,1,Piece.WHITE));
		pieces.add(new Pawn(1,1,Piece.WHITE));
		pieces.add(new Pawn(2,1,Piece.WHITE));
		pieces.add(new Pawn(3,1,Piece.WHITE));
		pieces.add(new Pawn(4,1,Piece.WHITE));
		pieces.add(new Pawn(5,1,Piece.WHITE));
		pieces.add(new Pawn(6,1,Piece.WHITE));
		pieces.add(new Pawn(7,1,Piece.WHITE));
		pieces.add(new Rook(0,0,Piece.WHITE));
		pieces.add(new Rook(7,0,Piece.WHITE));
		pieces.add(new Knight(1,0,Piece.WHITE));
		pieces.add(new Knight(6,0,Piece.WHITE));
		pieces.add(new Bishop(2,0,Piece.WHITE));
		pieces.add(new Bishop(5,0,Piece.WHITE));
		pieces.add(new Queen (3,0,Piece.WHITE));
		
		pieces.add(new Pawn(0,6,Piece.BLACK));
		pieces.add(new Pawn(1,6,Piece.BLACK));
		pieces.add(new Pawn(2,6,Piece.BLACK));
		pieces.add(new Pawn(3,6,Piece.BLACK));
		pieces.add(new Pawn(4,6,Piece.BLACK));
		pieces.add(new Pawn(5,6,Piece.BLACK));
		pieces.add(new Pawn(6,6,Piece.BLACK));
		pieces.add(new Pawn(7,6,Piece.BLACK));
		pieces.add(new Rook(0,7,Piece.BLACK));
		pieces.add(new Rook(7,7,Piece.BLACK));
		pieces.add(new Knight(1,7,Piece.BLACK));
		pieces.add(new Knight(6,7,Piece.BLACK));
		pieces.add(new Bishop(2,7,Piece.BLACK));
		pieces.add(new Bishop(5,7,Piece.BLACK));
		pieces.add(new Queen (3,7,Piece.BLACK));
		
		for(int i = 0;i < 8;i++) {
			for(int j = 0;j < 8;j++) {
				displayMap[i][j] = Display.EMPTY;
			}
		}
		
		for(Piece piece : pieces) {
			Position pos = piece.getPos();
			displayMap[pos.getX()][pos.getY()] = piece.getPieceType();
		}
	}

	public int[][] getDisplayMap() {
		return displayMap;
	}

	public int getCurrMove() {
		return currMove;
	}
}
