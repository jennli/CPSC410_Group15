import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaConstructor;
import com.thoughtworks.qdox.model.JavaMethod;

public class SingletonPatternMatcher extends AbstractPatternMatcher {

	private Collection<Collection<JavaClass>> singletons = new LinkedList<Collection<JavaClass>>();
	private JavaProjectBuilder jpb;

	public SingletonPatternMatcher(JavaProjectBuilder pb) {
		jpb = pb;
	}

	@Override
	public Collection<Collection<JavaClass>> patternMatch(
			JavaProjectBuilder builder) {

		Collection<JavaClass> classes = jpb.getClasses();
		
		for (JavaClass c : classes) {
			System.out.println("\nClass being analyzed: " + c.getName());
			Collection<JavaMethod> methods = c.getMethods();
			for (JavaMethod method : methods) {
				System.out.println("\t\tMethod being analyzed: "
						+ method.getName());
				if (method.getName().equals("getInstance")) {
					System.out.println(c.getName() + " has a possible match");

					Collection<JavaConstructor> jCon = c.getConstructors();
					for (JavaConstructor jc : jCon) {
						List<String> jm = jc.getModifiers();
						for (String s : jm) {
							if (s.equals("private")) {
								Collection<JavaClass> oneMatchInstance = new LinkedList<JavaClass>();
								oneMatchInstance.add(c);
								singletons.add(oneMatchInstance);
							}
						}
					}

				}
			}
		}

		return singletons;
	}
}
