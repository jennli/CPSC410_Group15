package PatternMatcher;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;


public class VisitorPatternMatcher extends AbstractPatternMatcher {
	
	@Override
	public Collection<Collection<JavaClass>> patternMatch(
			JavaProjectBuilder builder) {
		Collection<Collection<JavaClass>> patterns = new LinkedList<Collection<JavaClass>>();
		Collection<JavaClass> classes = builder.getClasses();
		for (JavaClass c : classes) {
			if (isVisitor(c))
				patterns.add(getVisitorParticipants(classes, c));
		}
		return patterns;
	}
	
	/**
	 * Finds all the classes involved in the visitor pattern.
	 * @param classes the collection of classes under consideration
	 * @param visitor the visitor interface at the root of the pattern
	 * @return the set of classes participating in the visitor pattern
	 */
	private Collection<JavaClass> getVisitorParticipants(Collection<JavaClass> classes, JavaClass visitor) {
		Set<JavaClass> participants = new HashSet<JavaClass>();
		participants.addAll(visitor.getDerivedClasses());
		
		for (JavaClass e : classes) {
			if (isVisitable(e, visitor)) {
				participants.add(e);
				participants.addAll(e.getDerivedClasses());
			}
		}
		
		return participants;
	}
	
	private static boolean isVisitor(JavaClass c) {
		return c.isInterface() && c.getName().contains("Visitor");
	}
	
	private static boolean isVisitable(JavaClass c, JavaClass visitor) {
		for (JavaMethod m : c.getMethods()) {
			if (isVisitMethod(m, visitor)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isVisitMethod(JavaMethod m, JavaClass visitor) {
		if (!m.getName().contains("visit"))
			return false;
		for (JavaType type : m.getParameterTypes()) {
			if (type.getCanonicalName().equals(visitor.getCanonicalName()))
				return true;
		}
		return false;
	}
	
	/*public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org"));
		
		Collection<Collection<JavaClass>> patterns = (new VisitorPatternMatcher()).patternMatch(builder);
		for (Collection<JavaClass> pattern : patterns) {
			for (JavaClass c : pattern) {
				System.out.println(c.getCanonicalName() + " participates in the visitor pattern");
			}
		}
	}*/
}
