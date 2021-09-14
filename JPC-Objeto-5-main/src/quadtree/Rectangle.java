package quadtree;

public class Rectangle {
	
	public int x;
	public int y;
	public int w;
	public int h;

	public Rectangle(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public boolean Contains(Point point) {
		
		return (point.x > this.x &&
				point.x < this.x + this.w &&
				point.y > this.y &&
				point.y < this.y + this.h);
	}
}
