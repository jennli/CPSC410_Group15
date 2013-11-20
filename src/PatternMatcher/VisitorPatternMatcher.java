package PatternMatcher;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import util.DesignPattern;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;


public class VisitorPatternMatcher extends AbstractPatternMatcher {
	
	@Override
	public Collection<DesignPattern> patternMatch(
			JavaProjectBuilder builder) {
		Collection<DesignPattern> patterns = new LinkedList<DesignPattern>();
		Collection<JavaClass> classes = builder.getClasses();
		for (JavaClass c : classes) {
			if (isVisitor(c))
				patterns.add(getVisitorPattern(classes, c));
		}
		return patterns;
	}
	
	/**
	 * Finds all the classes involved in the visitor pattern.
	 * @param classes the collection of classes under consideration
	 * @param visitor the visitor interface at the root of the pattern
	 * @return the set of classes participating in the visitor pattern
	 */
	private DesignPattern getVisitorPattern(Collection<JavaClass> classes, JavaClass visitor) {
		DesignPattern pattern = new DesignPattern("Visitor", visitor.getName());
		pattern.addHierarchy(visitor);
		
		for (JavaClass e : classes) {
			if (isVisitable(e, visitor)) {
				pattern.addHierarchy(e);
			}
		}
		
		return pattern;
	}
	
	private static boolean isVisitor(JavaClass c) {
		// a visitor is an interface with "Visitor" in the name
		return c.isInterface() && c.getName().contains("Visitor");
	}
	
	private static boolean isVisitable(JavaClass c, JavaClass visitor) {
		// a class is "visitable" if it has a visit method
		for (JavaMethod m : c.getMethods()) {
			if (isVisitMethod(m, visitor)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isVisitMethod(JavaMethod m, JavaClass visitor) {
		// a visit method has "visit" in the name
		if (!m.getName().contains("visit"))
			return false;
		// ...and accepts a visitor object as a parameter
		for (JavaType type : m.getParameterTypes()) {
			if (type.getCanonicalName().equals(visitor.getCanonicalName()))
				return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org"));
		
		Collection<DesignPattern> patterns = (new VisitorPatternMatcher()).patternMatch(builder);
		for (DesignPattern pattern : patterns) {
			for (JavaClass c : pattern.getNodes()) {
				System.out.println(c.getName() + " participates in the visitor pattern");
			}
		}
	}
}
