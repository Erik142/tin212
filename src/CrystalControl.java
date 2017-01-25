import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
public class CrystalControl extends JPanel{

JButton buttonSpeed;
JButton buttonRun;
JButton buttonStop;
CrystalView view;
CrystalModel model;
Timer timer;

 public CrystalControl(int size){
	 model = new CrystalModel(size/2);
	 view = new CrystalView(model,size,size);
	 buttonSpeed = new JButton("ChangeSpeed"); 
	 buttonRun = new JButton("Run");
	 buttonStop = new JButton("Stopped");
	 timer = new Timer(50,new TimerListener());
	 
	 this.setLayout(new BorderLayout());
	 
	 this.add(view, BorderLayout.CENTER);
	 
	 JPanel buttonPanel = new JPanel();
	 buttonPanel.add(buttonSpeed);
	 buttonPanel.add(buttonRun);
	 buttonPanel.add(buttonStop);
	 buttonPanel.setLayout(new FlowLayout());
	 this.add(buttonPanel, BorderLayout.PAGE_END);
	 buttonSpeed.addActionListener(new ButtonListener());
	 buttonRun.addActionListener(new ButtonListener());
	 buttonStop.addActionListener(new ButtonListener());
 }
private class TimerListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean finished = model.crystallizeOneIon();
		if (!finished) {
			view.setColor(Color.GREEN);
			buttonStop.doClick();
			//model.reset();
		}
		view.repaint();
	}
	
	
	
}
private class ButtonListener implements ActionListener{


public void actionPerformed (ActionEvent e){
	if(e.getSource()==buttonRun){
		timer.start();
		buttonRun.setText("Running");
		buttonStop.setText("Stop");
	}
	else if (e.getSource() == buttonStop) {
		timer.stop();
		buttonStop.setText("Stopped");
		buttonRun.setText("Run");
	}
	
	else if(e.getSource() == buttonSpeed){
	int delay = timer.getDelay();
		do {
	String input = JOptionPane.showInputDialog("Skriv in ett värde mellan 50-500", delay);
	if (input == null || input.length() == 0) {
		break;
	}
	delay = Integer.parseInt(input);
	} while (delay < 50 || delay > 500 );
	timer.setDelay(delay);
	}

	
		}
	}
}