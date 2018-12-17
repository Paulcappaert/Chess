package game;

import gui.Controller;
import gui.Display;

public class Main {
	
	public static void main(String[] args) {
		Display display = new Display();
		Game game = new Game();
		new Controller(display,game);
	}
}
