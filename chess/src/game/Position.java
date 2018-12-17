package game;

public class Position {
	
	private int x,y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}
	
	/**
	 * checks if the passed values can be added to x and y while the position is still on the board
	 * @param dx
	 * @param dy
	 * @return
	 */
	public boolean isValidMove(int dx, int dy) {
		if(x + dx > 7 || x + dx < 0 || y + dy > 7 || y + dy < 0)
			return false;
		else
			return true;
	}
	
	/**
	 * adds the passed values to x and y if it is still on the board
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if(isValidMove(dx,dy)) {
			x += dx;
			y += dy;
		}
	}
	
	/**
	 * determines if p has the same x and y value as this
	 * @param p
	 * @return
	 */
	public boolean isEqualTo(Position pos) {
		if(pos.getX() == x && pos.getY() == y)
			return true;
		else
			return false;
	}
	
	/**
	 * creates a new position that is a sum of the x and y values of this and the passed position
	 * returns null if new position is off of the board
	 * @param pos
	 * @return
	 */
	public Position addPosition(Position pos) {
		int newX,newY;
		newX = pos.getX() + x;
		newY = pos.getY() + y;
		if(newX > 7 || newX < 0 || newY > 7 || newY < 0)
			return null;
		else
			return new Position(newX,newY);
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

