
package quadtree;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {
	private List <Point> points = new ArrayList<Point>();
	int capacity;
	public Rectangle rectangle;
	public boolean divided = false;
	static int dividedCount = 0;
	public QuadTree northwest;
	public QuadTree northeast;
	public QuadTree southeast;
	public QuadTree southwest;
	String quadrante;
	
	//rectangles
	public QuadTree(Rectangle rectangle, int capacity) 
	{
		if (quadrante == null) {
			quadrante = "quadrado principal";
		}
		this.rectangle = rectangle;
		this.capacity = capacity;
		Board.quads.add(this);
	}

	public void Insert(Point point) 
	{
		if(!this.rectangle.Contains(point))
		{
			return;
		}
			
			
		if(points.size() < capacity && !points.contains(point)) 
		{
			points.add(point);
		}
		else 
		{			
			if(!divided) 
			{			
				Subdivide();
				
				for (int i = 0; i < points.size(); i++) 
				{
					this.northwest.Insert(points.get(i));
					this.northeast.Insert(points.get(i));
					this.southeast.Insert(points.get(i));
					this.southwest.Insert(points.get(i));		
				}
			}
						

			this.northwest.Insert(point);
			this.northeast.Insert(point);
			this.southeast.Insert(point);
			this.southwest.Insert(point);
		}

	}
	public void Subdivide() {
		Rectangle rectangleNW = new Rectangle(rectangle.x, 
				rectangle.y,
				rectangle.w/2,rectangle.h/2);
		this.northwest = new QuadTree(rectangleNW, capacity);
		this.northwest.quadrante = "northwest";
				
		Rectangle rectangleNE = new Rectangle(rectangle.x + rectangle.w / 2 , 
				rectangle.y,
				rectangle.w/2,rectangle.h/2);
		this.northeast = new QuadTree(rectangleNE, capacity);
		this.northeast.quadrante = "northeast";
		
		Rectangle rectangleSW = new Rectangle(rectangle.x, 
				rectangle.y + rectangle.h/2,
				rectangle.w/2,rectangle.h/2);
		this.southwest = new QuadTree(rectangleSW, capacity);
		this.southwest.quadrante = "southwest";
		
		Rectangle rectangleSE = new Rectangle(rectangle.x + rectangle.w / 2 , 
				rectangle.y + rectangle.h/2,
				rectangle.w/2,rectangle.h/2);
		this.southeast = new QuadTree(rectangleSE, capacity);
		this.southeast.quadrante = "southeast";
		
		divided = true;				
	}
	
	public void VerifyCollisions() 
	{
		for (int i = 0; i < points.size(); i++) 
		{
			int k = 0;
			
			for (int j = 0; j < points.size(); j++)
			{
				if (DoPitagoras(points.get(i), points.get(j)) && points.get(i) != points.get(j))
				{
					k++;
				}		
			}
			if (k > 0) {
				if (points.get(i).direction == 0) {
					points.get(i).direction = 1;
				}
				
				else if (points.get(i).direction == 1) {
					points.get(i).direction = 0;
				}
				else if (points.get(i).direction == 2) {
					points.get(i).direction = 3;
				}
				else if (points.get(i).direction == 3) {
					points.get(i).direction = 2;
				}
				
				
				
				points.get(i).highLight = true;
			}
			else {
				points.get(i).highLight = false;
				

			}
		}
	}
	
	boolean DoPitagoras(Point point1, Point point2)
	{
		return(((point2.x - point1.x) * (point2.x - point1.x) + (point2.y - point1.y) * (point2.y - point1.y)) <= 24.5f);					
	}
}
