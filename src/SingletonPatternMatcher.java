import java.util.Collection;
import java.util.LinkedList;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

public class SingletonPatternMatcher extends AbstractPatternMatcher {

	private Collection<JavaClass> singletons = new LinkedList<JavaClass>();
	private JavaProjectBuilder jpb;

	public SingletonPatternMatcher(JavaProjectBuilder pb) {
		jpb = pb;
	}

	@Override
	public Collection<Collection<JavaClass>> patternMatch(
			JavaProjectBuilder builder) {
		// TODO Auto-generated method stub
		Collection<JavaClass> classes = jpb.getClasses();
		for (JavaClass c : classes) {
			System.out.println("\nClass being analyzed: " + c.getName());
			Collection<JavaMethod> methods = c.getMethods();
			for (JavaMethod jm : methods) {
				System.out
						.println("\t\tMethod being analyzed: " + jm.getName());
				if (jm.getName().equals("getInstance")) {
					System.out.println(c.getName() + " has a possible match");
					singletons.add(c);
				}
			}
		}
		return null;
	}

}
