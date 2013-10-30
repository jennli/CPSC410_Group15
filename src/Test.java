import java.io.File;
import java.util.Collection;

import PatternMatcher.PatternSearcher;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		PatternSearcher ps = new PatternSearcher();
		//ps.SingletonSearch();
		// create new JavaProjectBuilder
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path
												// to
												// JHotDraw
		Collection<JavaClass> classes = builder.getClasses();
		for (JavaClass c : classes) {

			System.out.println("\nClass: " + c.getName());
			Collection<JavaMethod> jmethods = c.getMethods();
			for (JavaMethod jm : jmethods) {
				System.out.println("The method " + jm.getName()
						+ " has the following source code:\n " + jm.getSourceCode());
			}

			Collection<JavaClass> derived = c.getDerivedClasses();
			for (JavaClass d : derived) {
				System.out.println("\t\tSubclass: " + d);
				Collection<JavaField> fields = d.getFields();

				for (JavaField f : fields) {
					if (f.getType().getFullyQualifiedName()
							.equals("java.util.List")) {
						System.out.println("\t\t\tField of type List: " + f);
					}
				}
			}
		}
	}
}