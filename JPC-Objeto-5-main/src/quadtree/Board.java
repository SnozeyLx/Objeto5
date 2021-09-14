package quadtree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JPanel;

public class Board extends JPanel implements ActionListener
{
	// Tamanho do tabuleiro
	private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    
    //checar se ainda está em execução
    private static boolean inGame = true;  
    
    
    
    // Frequencia do game loop
    private final int DELAY = 100;   
    private Timer timer;
    
    //quadtree parametros
    private Rectangle rectangle = new Rectangle(1,1,B_WIDTH-3, B_HEIGHT-3);
    private QuadTree quadTree = new QuadTree(rectangle, 4);
    static List<QuadTree> quads = new ArrayList();
    
    //bolas
    Point[] points;
	int quantity = 20;

	Random rand = new Random();
	
	private boolean hasQuad = false;
    private long startTime;
    private long endTime;
    
    public Board() {
    	
    	points = new Point[quantity];
    	
        for(int i = 0; i < quantity; i++) {
        	Point point = new Point(rand.nextInt(rectangle.w - 2) + 2, rand.nextInt(rectangle.h - 2) + 2);
        	points[i] = point;
        	points[i].id = i;
        	points[i].direction = rand.nextInt(4);
        	points[i].speed = rand.nextInt(10) + 1;
        	
        	if(hasQuad) {
        	quadTree.Insert(point);	
        	}
        }
        initBoard();
    }
    
   //iniciar a board
    private void initBoard() {
        
        
        setBackground(Color.white);
        
        
        setFocusable(true);

        
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        
      
        initGame();
    }


    //contador loop
    private void initGame() 
    {       
        timer = new Timer(DELAY, this);
        timer.start();
    }
  
    
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    public void doDrawing(Graphics g) 
    {
    	if(hasQuad) {
    	g.drawRect(1,1,B_WIDTH-3, B_HEIGHT-3);
    	g.setColor(Color.black);
    		for (int i = 0; i < quads.size(); i++) 
    		{
    			g.drawRect(quads.get(i).rectangle.x, quads.get(i).rectangle.y, quads.get(i).rectangle.w, quads.get(i).rectangle.h);
    		}
    	}
    	for (int i = 0; i < quantity; i++) 
    	{
    		if(points[i].highLight) {
    		g.setColor(Color.RED);
    		}
    		else {	
    			g.setColor(Color.LIGHT_GRAY);
    		}
        	g.fillOval(points[i].x, points[i].y, 7, 7);
    	}
    	
    	java.awt.Toolkit.getDefaultToolkit().sync();  
    	  
    }
    public void movePoints() {   	
    	
        for(int i = 0; i < quantity; i++) {
        	
        	if(points[i].direction == 0) {
	
	        	points[i].y += points[i].speed;
	        	
        	}
        	if(points[i].direction == 1) {
	
	        	points[i].y -= points[i].speed;
	        	
        	}
        	if(points[i].direction == 2) {

	        	points[i].x -= points[i].speed;
	        	
        	}
        	if(points[i].direction == 3) {

	        	points[i].x += points[i].speed;

        	}
        	
        	if(points[i].x <= 1) {
				points[i].direction = 3;
        	}
        	if(points[i].x >= 598) {
				points[i].direction = 2;
        	}
        	if(points[i].y <= 1) {
				points[i].direction = 0;
        	}
        	if(points[i].y >= 598) {
				points[i].direction = 1;
        	}

        	
        	if(hasQuad) {
        	quadTree.Insert(points[i]);	
        
        	}
        }
    }
    public void CheckCollisions(Point pointToCheck) {
    	for(int i = 0; i < points.length; i++) {
    		if((points[i].x - pointToCheck.x) * (points[i].x - pointToCheck.x) + (points[i].y - pointToCheck.y) * (points[i].y - pointToCheck.y) <= 24.5f && points[i].id != pointToCheck.id)
    		{
    			pointToCheck.highLight = true;
    			
    			if (pointToCheck.direction == 0) {
    				pointToCheck.direction = 1;
				}
				
				else if (pointToCheck.direction == 1) {
					pointToCheck.direction = 0;
				}
				else if (pointToCheck.direction == 2) {
					pointToCheck.direction = 3;
				}
				else if (pointToCheck.direction == 3) {
					pointToCheck.direction = 2;
				}
    			return;
    		}
    		else
    		{
    			pointToCheck.highLight = false;
    		}
    	}
    }

    public void actionPerformed(ActionEvent e) {

        if (inGame) 
        {	       	
        	startTime = System.currentTimeMillis();
        	
        	if(hasQuad) {
        	quads.clear();
        
        	quadTree = new QuadTree(rectangle, 4);
        	}	
        	
        	movePoints();
        	
        	
        	if(hasQuad) {
        			for(int i = 0; i < quads.size(); i++)
        			{
        				quads.get(i).VerifyCollisions();
        			}
        		}
        	else
        	{
        		for(int i = 0; i < points.length; i++)
        		{
        			CheckCollisions(points[i]);
        		}
        	}
        	repaint();
        	endTime = System.currentTimeMillis();
        	
        	System.out.println("Time : " + ((endTime - startTime)));
        }
    }
}