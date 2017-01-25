import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Scanner;

import javax.swing.*;

public class Steg2 extends JFrame {

	private static CrystalModel m;
	private static CrystalView view;
	private static int SIZE = 100;
	private static int WIDTH = 400;
	private static int HEIGHT = 400;
	
	private Steg2() {
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Steg2");
		this.setLayout(new BorderLayout());
		view = new CrystalView(m, WIDTH, HEIGHT);
		
		JPanel buttonPanel = new JPanel();
		
		this.add(view, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//int arg;
		//Scanner sc = new Scanner(System.in);
		
		//if (args.length == 1) {
			//SIZE = Integer.parseInt(args[0]);
			m = new CrystalModel(SIZE);
			Steg2 s = new Steg2();
				
			
			/*g.setColor(Color.RED);
			g.drawRect(150, 200, 200, 100);
			view.repaint();		
		*/

			while (m.crystallizeOneIon()) {
				view.repaint();
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException ex) {
					System.out.println("InterruptedException");
				}
			}
			
		//}
		
	}

}
