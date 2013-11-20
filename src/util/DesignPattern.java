package util;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Data structure representing a design pattern.
 */
public class DesignPattern {

	private Set<JavaClass> nodes;
	private Set<Connection> connections;
	private String patternName;
	private String instanceName;

	/**
	 * Creates an empty design pattern with a name.
	 * @param patternName the name of the design pattern
	 * @param instanceName the name of the particular instance of the design pattern
	 */
	public DesignPattern(String patternName, String instanceName) {
		nodes = new HashSet<JavaClass>();
		connections = new HashSet<Connection>();
		this.patternName = patternName;
		this.instanceName = instanceName;
	}
	
	@Deprecated
	public DesignPattern(String patternName) {
		this(patternName, "unnamed");
	}

	public String getPatternName() {
		return patternName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public Set<JavaClass> getNodes() {
		return Collections.unmodifiableSet(nodes);
	}

	public Set<Connection> getConnections() {
		return Collections.unmodifiableSet(connections);
	}

	/**
	 * Adds a "node" (class or interface) to the design pattern.
	 */
	public void addNode(JavaClass node) {
		nodes.add(node);
	}

	/**
	 * Adds a connection to the design pattern.
	 */
	public void addConnection(JavaClass from, JavaClass to, String name) {
		nodes.add(from);
		nodes.add(to);
		connections.add(new Connection(from, to, name));
	}

	/**
	 * Adds an entire class hierarchy to the pattern.
	 * @param root the class or interface at which the hierarchy begins
	 */
	public void addHierarchy(JavaClass root) {
		nodes.add(root);

		/*
		 * XXX: QDox doesn't allow me to get *onlu* direct sub-classes/implementors,
		 * so the hierarchy is built by considering each "derived class" independently
		 * and finding it's parent.
		 */
		List<JavaClass> derived = root.getDerivedClasses();
		for (JavaClass c : derived) {
			
			// if root is an interface, we need to add an "implements" connection
			for (JavaClass i : c.getImplementedInterfaces()) {
				if (i.equals(root))
					connections.add(new Connection(c, i, "implements"));
			}

			// discard classes that aren't part of the hierarchy
			JavaClass sc = c.getSuperJavaClass();
			if (c == null || !derived.contains(sc))
				continue;

			nodes.add(c);
			connections.add(new Connection(c, sc, "extends"));
		}
	}
}
