import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class CrystalControl extends JPanel{

	private JButton buttonSpeed;
	private JButton buttonRun;
	private JButton buttonStop;
	private CrystalView view;
	private CrystalModel model;
	private BackgroundWorker bg;
	private int timerDelay = 10;

	public CrystalControl(int size){
		model = new CrystalModel(size);
		view = new CrystalView(model);
		buttonSpeed = new JButton("ChangeSpeed"); 
		buttonRun = new JButton("Run");
		buttonStop = new JButton("Stopped");

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
	
	/*Använder en SwingWorker istället för Timer för att gränssnittet inte ska bli långsamt vid stora bad.
	(Det blev långsamt, annars hade det absolut fungerat bra med en Timer!)*/
	private class BackgroundWorker extends SwingWorker<Void,Void> {

		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			long startTime = System.currentTimeMillis(), middleStartTime = startTime,
					middleStopTime = 0, stopTime = 0, execTime = 0;
			//Enkel algoritm för att bestämma antalet steg som ska beräknas åt gången, utan att uppritningen blir långsam!
			int steps = (int)(2*0.01*(1000-model.getBathSize())/4*(10-0.02*timerDelay+1));
			int currentDelay = timerDelay;
			
			while(model.runSomeSteps(steps) == true) {
				middleStopTime = System.currentTimeMillis();
				execTime = middleStopTime-middleStartTime;
				if (execTime < timerDelay) {
					Thread.sleep(timerDelay-execTime);
				}
				else {
					Thread.sleep(1);
				}

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						view.repaint();
					}

				});
				
				if (currentDelay != timerDelay) {
					steps = (int)(2*0.01*(1000-model.getBathSize())/4*(10-0.02*timerDelay+1));
					currentDelay = timerDelay;
				}
				
				middleStartTime = System.currentTimeMillis();
			}
			stopTime = System.currentTimeMillis();
			double finalExecTime = (double)(stopTime-startTime)/1000;
			System.out.println("execution time: " + finalExecTime + " seconds.");
			return null;
		}

		@Override
		protected void done() {
			view.repaint();
			buttonStop.doClick();
		}
	}


	private class ButtonListener implements ActionListener{

		public void actionPerformed (ActionEvent e){
			
			if(e.getSource()==buttonRun){

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
				if (bg != null && !bg.isDone()) {
					bg.cancel(true);
				}
				view.repaint();
				if (bg.isCancelled() || bg.isDone()) {
					buttonStop.setText("Stopped");
					buttonRun.setText("Run");
				}
			}
			else if(e.getSource() == buttonSpeed) {
				JSlider delaySlider = getSlider(timerDelay);

				int status = generateSliderDialog(delaySlider);

				if (status == JOptionPane.OK_OPTION) {
					timerDelay = delaySlider.getValue();
				}

			}

		}
		
		private int generateSliderDialog(JSlider slider) {
			
			JFrame window = new JFrame();
			JPanel panel = new JPanel();

			JLabel label = new JLabel("Välj tidsfördröjning(ms):");

			panel.setLayout(new BorderLayout());
			panel.add(label, BorderLayout.CENTER);
			panel.add(slider, BorderLayout.PAGE_END);


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
					timerDelay = tmpSlide.getValue();

				}

			};

			slider.addChangeListener(chListen);
			return slider;
		}

	}
}