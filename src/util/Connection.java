package util;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * A connection in a DesignPattern, i.e. an arrow.
 */
public class Connection {
	
	public JavaClass from;
	public JavaClass to;
	public String name;
	
	public Connection(JavaClass from, JavaClass to, String name) {
		this.from = from;
		this.to = to;
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + from.getCanonicalName().hashCode()
				+ to.getCanonicalName().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass())
			return false;
		Connection other = (Connection) o;
		if (other.from.equals(from) && other.to.equals(to) && other.name.equals(name))
			return true;
		return false;
	}
}