package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;

import game.Game;

public class Controller implements ActionListener {
	
	public static final int EMPTY = 0;
	
	private Display display;
	private Game game;
	private Board board;
	private int[][] squares;
	
	private int selectedX;
	private int selectedY;
	private boolean selected;
	private int selectedImageKey;

	public Controller(Display display,Game game) {
		this.display = display;
		this.game = game;
		squares = this.game.getDisplayMap();
		this.board = new Board(squares);
		this.display.add(board,BorderLayout.CENTER);
		
		board.addMouseListener(new MouseControl());
		board.addMouseMotionListener(new MouseMotionControl());
		this.display.setVisible(true);
		this.display.addController(this);
	}
	
	public void moveComplete() {
		//check for possible promotion
		//check for checkmate
	}
	
	public class MouseControl extends MouseAdapter {
		
		public void mousePressed(MouseEvent e) {
    		selected = true;
    		selectedX = (e.getX() - 10)/68;
    		selectedY = (554 - e.getY())/68;
    		if(selectedX < 0 || selectedX > 7 || selectedY < 0 || selectedY > 7 || squares[selectedX][selectedY] == EMPTY)
    			selected = false;
    		else
    			selectedImageKey = squares[selectedX][selectedY];
    		
    		board.setSelected(selected);
    		board.setSelectedX(selectedX);
    		board.setSelectedY(selectedY);
    		board.setImage(selectedImageKey);
    	}

    	public void mouseReleased(MouseEvent e) {
    		if(selected == false)
    			return;
    		
    		selected = false;
    		int x = (e.getX() - 10)/68;
    		int y = (554 - e.getY())/68;
    		if(selectedX >= 0 && selectedX <= 7 && selectedY >= 0 && selectedY <= 7) {
    			try {
    				game.move(selectedX, selectedY, x, y);
    				moveComplete();
    				squares = game.getDisplayMap();
    				board.repaint();
    				display.setHistory(game.getHistoryStrings(),game.getCurrMove());
    			} catch (Exception e1) {
    				board.repaint();
    			}
    		}
    		board.setSelected(false);
        }
	}
	
	public class MouseMotionControl extends MouseMotionAdapter {
		private int drawX;
		private int drawY;

		public void mouseDragged(MouseEvent e) {
	        	drawX = e.getX() - 34;
	        	drawY = e.getY() - 34;
	        	if(drawX < -34 || drawX > 520 || drawY < -34 || drawY > 530)
	        		selected = false;
	        	board.repaint();
	        	board.setDrawX(drawX);
	        	board.setDrawY(drawY);
	        }
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof JButton) {
			JButton button = (JButton) event.getSource();
			if(button.getActionCommand().equals("Restart")) {
				game.restart();
				board.repaint();
				display.setHistory(game.getHistoryStrings(),game.getCurrMove());
			} else if(button.getActionCommand().equals("Previous Move")) {
				game.undo();
				board.repaint();
				display.setHistory(game.getHistoryStrings(),game.getCurrMove());
			} else if(button.getActionCommand().equals("Next Move")) {
				game.redo();
				board.repaint();
				display.setHistory(game.getHistoryStrings(),game.getCurrMove());
			}
		}
		
	}
}
