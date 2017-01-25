
public class Ion {
	private int x;
	private int y;
	
	public Ion(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object i) {
		if (i instanceof Ion) {
			return (this.x == (((Ion) i).getX()) && this.y == (((Ion) i).getY()));
		}
		return false;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
