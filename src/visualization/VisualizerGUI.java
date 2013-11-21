package visualization;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import util.Connection;
import util.DesignPattern;
import PatternMatcher.CompositePatternMatcher;
import PatternMatcher.ObserverPatternMatcher;
import PatternMatcher.SingletonPatternMatcher;
import PatternMatcher.VisitorPatternMatcher;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;

public class VisualizerGUI extends JFrame {

	// Common tab
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	// sub-tab for singleton
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

		builder.addSourceTree(new File("org")); // path to JHotDraw
		SingletonPatternMatcher spm = new SingletonPatternMatcher();
		CompositePatternMatcher cpm = new CompositePatternMatcher();
		ObserverPatternMatcher opm = new ObserverPatternMatcher();
		VisitorPatternMatcher vpm = new VisitorPatternMatcher();

		addPattern(spm.patternMatch(builder), "Singleton");
		addPattern(cpm.patternMatch(builder), "Composite");
		addPattern(opm.patternMatch(builder), "Observer");
		addPattern(vpm.patternMatch(builder), "Visitor");

		// =====HACK, To be changed later (assumption that the result is only
		// one class)
		// for(JavaClass cl: spm.patternMatch(builder)){
		// text = cl.getName();
		// }
		// =====
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

		final mxGraph graph = new mxGraph();
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		Object graphParent = graph.getDefaultParent();

		
		
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Connection c : pattern.getConnections()) {
			graph.getModel().beginUpdate();
			try {
				Object v1 = map.get(c.to.getName());
				if (v1 == null) {
					v1 = graph.insertVertex(graphParent, null, c.to.getName(), 20, 20, 80, 30);
					map.put(c.to.getName(), v1);
				}
				
				Object v2 = map.get(c.from.getName());
				if (v2 == null) {
					v2 = graph.insertVertex(graphParent, null, c.from.getName(), 240, 150, 80, 30);
					map.put(c.from.getName(), v2);
				}
				graph.insertEdge(graphParent, null, c.name, v1, v2, "startArrow=arrow_classic;endArrow=none");
			} finally {
				graph.getModel().endUpdate();
			}
		}
		
		if (pattern.getConnections().size() == 0) {
			graph.getModel().beginUpdate();
			for (JavaClass c : pattern.getNodes()) {
				try {
					graph.insertVertex(graphParent, null, c.getName(), 20, 20, 80, 30);
				} finally {
					graph.getModel().endUpdate();
				}
			}
		}

		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		
		graph.getModel().beginUpdate();
		
		try {
			layout.execute(graph.getDefaultParent()); // Run the layout on the facade.
		} finally {
			mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);
			morph.addListener(mxEvent.DONE, new mxIEventListener() {

				@Override
				public void invoke(Object arg0, mxEventObject arg1) {
					graph.getModel().endUpdate();
					
				}
				
			});
			
			morph.startAnimation();
		}
		
		//mxGraphComponent graphComponent = new mxGraphComponent(graph);

		parent.addTab(String.valueOf(parent.getTabCount() + 1), graphComponent);
	}

}
