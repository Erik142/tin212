import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Steg3 extends JFrame{
	
	public Steg3(int size) {
		this.setSize(size, size);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Steg3");
		this.setLayout(new BorderLayout());
		this.add(new CrystalControl(size));
		this.setVisible(true);
		this.pack();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
			int size = Integer.parseInt(args[0]);
			if (size < 10 || size > 1000) {
				System.out.println("Storleken m�ste vara minst 10 och st�rst 1000");
			}
			else {
				new Steg3(800);
			}
		}
		
	}

}
