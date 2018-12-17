package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;

import game.Game;

public class Board extends JComponent {

	public static final int EMPTY = 0;
	public static final int WKING = 1;
	public static final int WQUEEN = 2;
	public static final int WROOK = 3;
	public static final int WBISHOP = 4;
	public static final int WKNIGHT = 5;
	public static final int WPAWN = 6;
	public static final int BKING = 7;
	public static final int BQUEEN = 8;
	public static final int BROOK = 9;
	public static final int BBISHOP = 10;
	public static final int BKNIGHT = 11;
	public static final int BPAWN = 12;
	
	private HashMap<Integer,Image> images;
	private int[][] squares;
	private boolean selected;
	private Image selectedImage;
	
	private int selectedX;
	private int selectedY;
	
	private int drawX,drawY;
	
	public Board(int[][] squares) {
		setPreferredSize(new Dimension(554,554));
		images = new HashMap<Integer,Image>();
		this.squares = squares;
		selectedImage = null;
		
		initializeImages();
		repaint();
	}

	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		for(int i = 0;i < 8;i++) {
			for(int j = 0;j < 8;j++) {
				g2d.drawImage(images.get(EMPTY), 10 + 68*i, 486 - 68*j, null);
				if(!selected || i != selectedX || j != selectedY)
					g2d.drawImage(images.get(squares[i][j]), 10 + 68*i, 486 - 68*j, null);
			}
		}
		
		if(selected) {
			g2d.drawImage(selectedImage, drawX, drawY, null);
		}
	}
	
	private void initializeImages() {
		try {
		images.put(EMPTY, ImageIO.read(new File("square.png")));
		images.put(WKING, ImageIO.read(new File("wKing.png")));
		images.put(WQUEEN, ImageIO.read(new File("wQueen.png")));
		images.put(WROOK, ImageIO.read(new File("wRook.png")));
		images.put(WBISHOP, ImageIO.read(new File("wBishop.png")));
		images.put(WKNIGHT, ImageIO.read(new File("wKnight.png")));
		images.put(WPAWN, ImageIO.read(new File("wPawn.png")));
		images.put(BKING, ImageIO.read(new File("bKing.png")));
		images.put(BQUEEN, ImageIO.read(new File("bQueen.png")));
		images.put(BROOK, ImageIO.read(new File("bRook.png")));
		images.put(BBISHOP, ImageIO.read(new File("bBishop.png")));
		images.put(BKNIGHT, ImageIO.read(new File("bKnight.png")));
		images.put(BPAWN, ImageIO.read(new File("bPawn.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSelected(boolean b) {
		selected = b;	
	}

	public void setSelectedX(int x) {
		selectedX = x;
	}
	
	public void setSelectedY(int y) {
		selectedY = y;
	}

	public void setImage(int selectedImageKey) {
		selectedImage = images.get(selectedImageKey);
		
	}

	public void setDrawX(int drawX) {
		this.drawX = drawX;	
	}

	public void setDrawY(int drawY) {
		this.drawY = drawY;	
	}
}
