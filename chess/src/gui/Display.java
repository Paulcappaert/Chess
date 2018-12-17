package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class Display extends JFrame {
	
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
	
	private JButton nextMove,prevMove,restart;
	private Board board;
	private JTextArea history;
	
	public Display() {
		setSize(750,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextArea details = new JTextArea();
		add(details,BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(150,100));	
		history = new JTextArea("Move History");	
		scrollPane.setViewportView(history);
		add(scrollPane, BorderLayout.EAST);
		
		JPanel bPanel = new JPanel();
		nextMove = new JButton("Next Move");
		prevMove = new JButton("Previous Move");
		restart = new JButton("Restart");
		bPanel.add(nextMove);
		bPanel.add(prevMove);
		bPanel.add(restart);
		
		add(bPanel,BorderLayout.NORTH);
	}

	public void addController(Controller controller) {
		restart.addActionListener(controller);
		prevMove.addActionListener(controller);
		nextMove.addActionListener(controller);
	}
	
	public void setHistory(ArrayList<String> historyStrings,int currMove) {
		int move = 0;
		int position = 0;
		Highlighter h = history.getHighlighter();
		h.removeAllHighlights();
		history.setText("");
		for(String s : historyStrings) {
			
			String line = (move/2 + 1) + ". " + s + "\n";
			position += line.length();
			history.append(line);
			move++;
			
			if(move == currMove - 1) {
				try {
					h.addHighlight(position - 1, position, DefaultHighlighter.DefaultPainter);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
