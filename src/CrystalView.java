import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CrystalView extends JPanel {
	private CrystalModel model;
	private int WINDOW_SIZE;
	private final int MINIMUM_WINDOW_SIZE = 400;
	private Color color;
	private final Color standardColor = Color.RED;
	
	public CrystalView(CrystalModel m) {
		this.model = m;
		if (model.getBathSize() < this.MINIMUM_WINDOW_SIZE) {
			this.WINDOW_SIZE = this.MINIMUM_WINDOW_SIZE;
		}
		else {
			this.WINDOW_SIZE = model.getBathSize();
		}
		
		color = Color.RED;
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(WINDOW_SIZE,WINDOW_SIZE));
		this.setLayout(new BorderLayout());
		System.out.println("" + m.getBathSize());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(standardColor);
		
		for(int i = -model.getEscapeCircleRadius(); i < model.getEscapeCircleRadius(); i++) {
			for (int j = -model.getEscapeCircleRadius(); j < model.getEscapeCircleRadius(); j++) {
			if (model.getModelValue(i, j)) {
				g.drawOval(bathToWindowPosition(i), bathToWindowPosition(j), 1, 1);
			}
			//g.drawLine(xPos, yPos, xPos, yPos);
			}
		}
		g.setColor(Color.GREEN);
		if (model.getModelValue(model.getX(), model.getY())) {
			g.drawOval(bathToWindowPosition(model.getX()), bathToWindowPosition(model.getY()), 1, 1);
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	private int bathToWindowPosition(int x) {
		return (int)(x + this.WINDOW_SIZE/2);
	}
	
	
}
