package visualization;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import util.Connection;
import util.DesignPattern;
import PatternMatcher.CompositePatternMatcher;
import PatternMatcher.ObserverPatternMatcher;
import PatternMatcher.SingletonPatternMatcher;
import PatternMatcher.VisitorPatternMatcher;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;

@SuppressWarnings("serial")
public class VisualizerGUI extends JFrame {

	// Common tab
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	// Source code parser
	JavaProjectBuilder builder = new JavaProjectBuilder();

	public VisualizerGUI() {
		setTitle("Design Pattern Analyzer");
		setBounds(300, 150, 600, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);

		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		builder.addSourceTree(new File("org")); // path to JHotDraw
		SingletonPatternMatcher spm = new SingletonPatternMatcher();
		CompositePatternMatcher cpm = new CompositePatternMatcher();
		ObserverPatternMatcher opm = new ObserverPatternMatcher();
		VisitorPatternMatcher vpm = new VisitorPatternMatcher();

		addPattern(cpm.patternMatch(builder), "Composite");
		addPattern(opm.patternMatch(builder), "Observer");
		addPattern(vpm.patternMatch(builder), "Visitor");
		addPattern(spm.patternMatch(builder), "Singleton");
	}

	public void addPattern(Collection<DesignPattern> instances, String name) {
		JTabbedPane patPane = new JTabbedPane(JTabbedPane.TOP);

		// add sub-tabs for each pattern instance
		for (DesignPattern i : instances) {
			addPatternInstance(i, patPane);
		}

		tabbedPane.addTab(name, patPane);
	}

	public static String getNodeName(JavaClass c) {
		String ret = "";
		if (c.isInterface())
			ret = "<<interface>>\n";
		return ret + c.getName();
	}

	public void addPatternInstance(DesignPattern pattern, JTabbedPane parent) {
		// create the graph data structure
		final mxGraph graph = new mxGraph();
		
		// set the graph to be uneditable
		graph.setAllowDanglingEdges(false);
		graph.setCellsEditable(false);
		graph.setEdgeLabelsMovable(false);
		graph.setCellsLocked(true);
		graph.setCellsMovable(false);
		graph.setVertexLabelsMovable(false);
		
		Object graphParent = graph.getDefaultParent();
		
		// create a map of all the vertices in the graph so we can add edges between them and new vertices
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Connection c : pattern.getConnections()) {
			// JGraph uses a transaction-based model
			graph.getModel().beginUpdate();
			try {
				// check if the map contains a vertex for the class
				Object v1 = map.get(c.to.getName());
				if (v1 == null) {
					String name = getNodeName(c.to);
					// if not, create a new vertex in the graph and put it in the map
					if (pattern.getPatternName().equals("Observer")){
						v1 = graph.insertVertex(graphParent, null, name, 20, 20, 185, 42, "shadow=true;fontColor=BLACK;fontSize=12");
					}
					else if (pattern.getPatternName().equals("Visitor")){
						v1 = graph.insertVertex(graphParent, null, name, 20, 20, 170, 42, "fillColor=#ffb6c1;shadow=true;fontColor=BLACK;fontSize=12");
					}
					else{
						v1 = graph.insertVertex(graphParent, null, name, 20, 20, 180, 42, "fillColor=#7fffd4;shadow=true;fontColor=BLACK;fontSize=12");
					}
					map.put(c.to.getName(), v1);
				}

				// same as above for the second vertex
				Object v2 = map.get(c.from.getName());
				if (v2 == null) {
					String name = getNodeName(c.from);
					if (pattern.getPatternName().equals("Observer")){
						v2 = graph.insertVertex(graphParent, null, name, 240, 150, 185, 42, "shadow=true;fontColor=BLACK;fontSize=12");
					} else if (pattern.getPatternName().equals("Visitor")) {
						v2 = graph.insertVertex(graphParent, null, name, 20, 20, 170, 42, "fillColor=#ffb6c1;shadow=true;fontColor=BLACK;fontSize=12");
					} else {
						v2 = graph.insertVertex(graphParent, null, name, 240, 150, 180, 42, "fillColor=#7fffd4;shadow=true;fontColor=BLACK;fontSize=12");
					}
					map.put(c.from.getName(), v2);
				}
				// create an edge between the vertices
				if (c.name.equals("implements")) {
					//graph.insertEdge(graphParent, null, c.name, v1, v2, "startArrow=arrow_classic;endArrow=none;dashed=1");
					graph.insertEdge(graphParent, null, c.name, v2, v1, "startArrow=arrow_classic;dashed=1");
				} else {
					//graph.insertEdge(graphParent, null, c.name, v1, v2, "startArrow=arrow_classic;endArrow=none");
					graph.insertEdge(graphParent, null, c.name, v2, v1, "startArrow=arrow_classic");
				}
			} finally {
				graph.getModel().endUpdate();
			}
		}
		
		// this handles the case where there are no hierarchical connections between the classes (eg Singleton pattern)
		if (pattern.getConnections().size() == 0) {
			for (JavaClass c : pattern.getNodes()) {
				graph.getModel().beginUpdate();
				try {
					graph.insertVertex(graphParent, null, c.getName(), 500, 250, 180, 42, "fillColor=#ffe4b5;shadow=true;fontColor=BLACK;fontSize=12");
				} finally {
					graph.getModel().endUpdate();
				}
			}
		}

		if (pattern.getConnections().size() > 0) {
			// layout the graph as a tree
			mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
			//layout.setInterRankCellSpacing(70);
			//layout.setIntraCellSpacing(100);
			//layout.setDisableEdgeStyle(false);
			//layout.setParallelEdgeSpacing(10);
			layout.execute(graphParent);
		} else {
			new mxFastOrganicLayout(graph).execute(graphParent);
		}
		
		// create a SWING component for the graph
		mxGraphComponent graphComponent = new mxGraphComponent(graph);

		// Finally add the finished graph to the GUI (as a subtab of its respective design pattern tab)
		parent.addTab("#" + String.valueOf(parent.getTabCount() + 1), graphComponent);
	}

}
