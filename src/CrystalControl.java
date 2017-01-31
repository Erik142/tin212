import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class CrystalControl extends JPanel{

JButton buttonSpeed;
JButton buttonRun;
JButton buttonStop;
CrystalView view;
CrystalModel model;
Timer timer;
BackgroundWorker bg;
int timerDelay = 10;

 public CrystalControl(int size){
	 model = new CrystalModel(size);
	 view = new CrystalView(model);
	 buttonSpeed = new JButton("ChangeSpeed"); 
	 buttonRun = new JButton("Run");
	 buttonStop = new JButton("Stopped");
	 timer = new Timer(10,new TimerListener());
	 
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
 
 private class BackgroundWorker extends SwingWorker<Void,Boolean> {

		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			long startTime = System.currentTimeMillis(), middleStartTime = 0, middleStopTime = 0, stopTime = 0, execTime = 0;
			boolean status;
			while((status = model.runSomeSteps((int)(0.05*model.getBathSize()))) == true) {
			middleStopTime = System.currentTimeMillis();
			execTime = middleStopTime-startTime;
			if (execTime < timerDelay) {
				Thread.sleep(timerDelay-execTime);
			}
			else {
				Thread.sleep(1);
			}
			
			publish(status);
			middleStartTime = System.currentTimeMillis();
			}
			stopTime = System.currentTimeMillis();
			double finalExecTime = (double)(stopTime-startTime)/1000;
			System.out.println("execution time: " + finalExecTime + " seconds.");
			//view.setColor(Color.GREEN);
			return null;
		}
		
		@Override
		protected void process(List<Boolean> statuses) {
			//if (statuses.get(statuses.size()-1)) {
			view.repaint();
			//}
		}
		
		@Override
		protected void done() {
			view.repaint();
			buttonStop.doClick();
		}
 }
 
private class TimerListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean finished = !model.runSomeSteps(50-(int)(0.1*timer.getDelay()+1));
		//boolean finished = !model.crystallizeOneIon();
		if (finished) {
			//view.setColor(Color.GREEN);
			buttonStop.doClick();
			//model.reset();
		}
		view.repaint();
	}
	
	
	
}
private class ButtonListener implements ActionListener{


public void actionPerformed (ActionEvent e){
	if(e.getSource()==buttonRun){
		/*if (!timer.isRunning()) {
			model.reset();
			timer.start();
			buttonRun.setText("Running");
			buttonStop.setText("Stop");
		}*/
		if (bg != null && bg.isDone()) {
			model.reset();
		}
		if (bg == null || bg.isDone()) {
		bg = new BackgroundWorker();
		bg.execute();
		buttonRun.setText("Running");
		buttonStop.setText("Stop");
		}
	}
	else if (e.getSource() == buttonStop) {
		//timer.stop();
		if (bg != null && !bg.isDone()) {
			bg.cancel(true);
		}
		view.repaint();
		if (bg.isCancelled() || bg.isDone()) {
			buttonStop.setText("Stopped");
			buttonRun.setText("Run");
		}
	}
	
	else if(e.getSource() == buttonSpeed){
		JSlider delaySlider = getSlider(timerDelay);

		int status = generateSliderDialog(delaySlider);
		//Object message = optionPane.getInputValue();
		if (status == JOptionPane.OK_OPTION) {
			timerDelay = delaySlider.getValue();
			
		}
		System.out.println("inputValue = " + timerDelay);


		timer.setDelay(timerDelay);
		}



}
private int generateSliderDialog(JSlider slider) {
JFrame window = new JFrame();
JPanel panel = new JPanel();

JLabel label = new JLabel("Välj tidsfördröjning(ms):");

panel.setLayout(new BorderLayout());
panel.add(label, BorderLayout.CENTER);
panel.add(slider, BorderLayout.PAGE_END);


//optionPane.setMessage(new Object[] { "Välj tidsfördröjning(ms)", delaySlider });
//optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
//optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
int status = JOptionPane.showConfirmDialog(window, panel, "Ändra fördröjning", JOptionPane.OK_CANCEL_OPTION);
return status;


}

private JSlider getSlider(int currentValue) {
JSlider slider = new JSlider();
slider.setMajorTickSpacing(70);
slider.setMinimum(10);
slider.setMaximum(500);
slider.setValue(currentValue);
slider.setPaintTicks(true);
slider.setPaintLabels(true);

ChangeListener chListen = new ChangeListener() {

public void stateChanged(ChangeEvent e) {
	
		JSlider tmpSlide = (JSlider)e.getSource();
		//optionPane.setInputValue(tmpSlide.getValue());
		timer.setDelay(tmpSlide.getValue());
		timerDelay = tmpSlide.getValue();
	
}

};

slider.addChangeListener(chListen);
return slider;
}

	}
}