package visualization;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;

import PatternMatcher.*;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;

import util.Connection;
import util.DesignPattern;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.io.File;

public class VisualizerGUI extends JFrame {

	//Common tab
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	//sub-tab for singleton
	private final JPanel singletonSub = new JPanel();

	JavaProjectBuilder builder = new JavaProjectBuilder();
	String text = null;
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


	public VisualizerGUI() {
		setTitle("Design Pattern Analyzer");
		setBounds(300, 150, 600, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		ChangeListener changeListener = new ChangeListener() {

			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				System.out.println("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
				
				if (sourceTabbedPane.getTitleAt(index).equals("Singleton Pattern")){
					singletonTabClicked();
				}
			}
		};

		tabbedPane.addChangeListener(changeListener);

        builder.addSourceTree(new File("org")); // path to JHotDraw
        SingletonPatternMatcher spm = new SingletonPatternMatcher();
        CompositePatternMatcher cpm = new CompositePatternMatcher();
        ObserverPatternMatcher opm = new ObserverPatternMatcher(builder);
        VisitorPatternMatcher vpm = new VisitorPatternMatcher();

        addPattern(spm.patternMatch(builder), "Singleton");
        addPattern(cpm.patternMatch(builder), "Composite");
        addPattern(opm.patternMatch(builder), "Observer");
        addPattern(vpm.patternMatch(builder), "Visitor");

        //=====HACK, To be changed later (assumption that the result is only one class)
        //for(JavaClass cl:  spm.patternMatch(builder)){
        //	text = cl.getName();
        //}
        //=====
	}

	public void addPattern(Collection<DesignPattern> instances, String name) {
		JTabbedPane patPane = new JTabbedPane(JTabbedPane.TOP);

		// add sub-tabs for each pattern instance
		for (DesignPattern i : instances) {
			addPatternInstance(i, patPane);
		}

		tabbedPane.addTab(name, patPane);
	}
	
	public void addPatternInstance(DesignPattern pattern, JTabbedPane parent) {

		JPanel patternPanel = new JPanel();

		// TODO logic to display the design pattern onto the JPanel
		//Set<Connection> edges = pattern.getConnections();

		/* extra JTree stuff
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("JHotDraw");
		JTree tree = new JTree(top);

		for (Connection c : edges) {
			DefaultMutableTreeNode parent = new DefaultMutableTreeNode(c.from);
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(c.to);

		}
		 */

		parent.addTab(pattern.getInstanceName(), patternPanel);
	}

	//onclick Singleton tab this method gets called
	private void singletonTabClicked() {
  
		singletonSub.add(new Rec());
	}
	
	//used only for singleton, since its exceptional case where we use only one rectangle to represent Singleton,
	//in other cases we will use JUNG lib
	private class Rec extends JPanel{
		
	    protected void paintComponent(Graphics g)  
	    {  
	        super.paintComponent(g);  
	        Graphics2D g2 = (Graphics2D)g;  

	        int w = getWidth();  
	        int h = getHeight();  
	        int x = w/3;  
	        int y = h/4;  
	        int rectW = w*3/8;  
	        int rectH = h/4;  
	        //String text = null;
	        Color c = new Color(135,206,250);
	        g.setColor(c);
	        g.fillRect(x, y, rectW, rectH);
	        g.setColor(Color.BLACK);
	        g.drawRect(x, y, rectW, rectH);  
	        
	        Font font = g2.getFont().deriveFont(16f);  
	        g2.setFont(font);  
	        FontRenderContext frc = g2.getFontRenderContext();  
	        int textWidth = (int)font.getStringBounds(text, frc).getWidth();  
	        LineMetrics lm = font.getLineMetrics(text, frc);  
	        int textHeight = (int)(lm.getAscent() + lm.getDescent());  
	        int sx = x + (rectW - textWidth)/2;  
	        int sy = (int)(y + (rectH + textHeight)/2 - lm.getDescent());  
	        g.setColor(Color.BLACK);  
	        g.drawString(text, sx, sy);  
	        
	    
	    }
	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(singletonSub.getWidth(),singletonSub.getHeight());
	    }
	}


}
