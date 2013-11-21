
import java.awt.EventQueue;

import visualization.VisualizerGUI;

public class PatternMatcherApp {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisualizerGUI frame = new VisualizerGUI();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
