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
	private String name;
	
	/**
	 * Creates an empty design pattern with a name.
	 * @param name the name of the design pattern
	 */
	public DesignPattern(String name) {
		nodes = new HashSet<JavaClass>();
		connections = new HashSet<Connection>();
		this.name = name;
	}
	
	public String getName() {
		return name;
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
			nodes.add(c);
			
			// connect superclass
			JavaClass s = c.getSuperJavaClass();
			if (s != null && !s.getCanonicalName().equals("java.lang.Object"))
				connections.add(new Connection(c, s, "extends"));
			
			// if root is an interface, we need to add an "implements" connection
			for (JavaClass i : c.getImplementedInterfaces()) {
				if (i.equals(root))
					connections.add(new Connection(c, i, "implements"));
			}
		}
	}
}
