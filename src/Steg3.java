import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Steg3 extends JFrame{
	
	public Steg3(int size) {
		this.setSize(size, size);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Steg2");
		this.setLayout(new BorderLayout());
		this.add(new CrystalControl(size));
		this.setVisible(true);
		this.pack();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Steg3 s = new Steg3(400);
	}

}
