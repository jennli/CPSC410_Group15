package visualization;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.tree.DefaultMutableTreeNode;

import util.Connection;
import util.DesignPattern;

public class VisualizerGUI extends JFrame {
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private final JTabbedPane patternNamePane = new JTabbedPane(JTabbedPane.TOP);
	
	private final JPanel subPane1 = new JPanel();
	private final JPanel subPane2 = new JPanel();

	public VisualizerGUI() {
		setTitle("Design Pattern Analyzer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addTab("Observer Patterns", null, patternNamePane, null);
		patternNamePane.addTab("#1", null, subPane1, null);
		
		patternNamePane.addTab("#2", null, subPane2, null);
	}
	
	public void addPattern(DesignPattern pattern) {
		
		JPanel patternPanel = new JPanel();
		
		
		// TODO logic to display the design pattern onto the JPanel
		Set<Connection> edges = pattern.getConnections();
		
		/* extra JTree stuff
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("JHotDraw");
		JTree tree = new JTree(top);
		
		for (Connection c : edges) {
			DefaultMutableTreeNode parent = new DefaultMutableTreeNode(c.from);
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(c.to);
			
		}
		*/
		
		tabbedPane.addTab(pattern.getName(), patternPanel);
	}
	

	
}
