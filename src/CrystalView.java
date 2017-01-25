import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CrystalView extends JPanel {
	private CrystalModel model;
	private int WINDOW_WIDTH;
	private int WINDOW_HEIGHT;
	private Color color;
	private final Color standardColor = Color.RED;
	
	public CrystalView(CrystalModel m, int width, int height) {
		this.model = m;
		this.WINDOW_WIDTH = width;
		this.WINDOW_HEIGHT = height;
		color = Color.RED;
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
		this.setLayout(new BorderLayout());
		System.out.println("" + m.getSize());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(standardColor);
		ArrayList<Ion> crystals = model.getCrystals();
		
		for(int i = 0; i < crystals.size(); i++) {
			if (i == crystals.size() - 1) {
				g.setColor(color);
			}
			int xPos = crystals.get(i).getX() + WINDOW_WIDTH/2;
			int yPos = crystals.get(i).getY() + WINDOW_HEIGHT/2;
			g.drawOval(xPos, yPos, 1, 1);
			//g.drawLine(xPos, yPos, xPos, yPos);
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
