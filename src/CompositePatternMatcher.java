import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;


public class CompositePatternMatcher extends AbstractPatternMatcher {

	public CompositePatternMatcher() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		CompositePatternMatcher cpm = new CompositePatternMatcher();
		cpm.patternMatch(builder);

	}

	@Override
	public Collection<Collection<JavaClass>> patternMatch(JavaProjectBuilder builder) {

		Collection<Collection<JavaClass>> result = new ArrayList<Collection<JavaClass>>();

		Collection<JavaClass> classes = builder.getClasses();
		for (JavaClass c : classes) {
			// Class Name contains "composite"
			if (c.getName().contains("Composite")) {
				// check if this class has a field of type List
				List<JavaField> fields = c.getFields();
				boolean hasList = false;
				for (JavaField f : fields) {
					if (f.getType().getFullyQualifiedName().equals("java.util.List")) {
						//System.out.println("Field of type List: " + f.toString());
						hasList = true;
						break;
					}
				}
				if (hasList) {
					// Determine the type of the list and check if it 
					// a) is a list of the right part of the class name (JHotDraw6 doesn't use parameterized lists?!?!)
					// b) or is a list of an interface which this class implements

					// get the right part of the class name (ie given CompositeFigure, return Figure)
					String compositeOf = c.getName().split("Composite", 2)[1];

					// find the interface with that name
					//JavaClass compositeIface = builder.getClassByName(compositeOf);
					
					JavaClass compositeIface = findCompositeInterface(c, compositeOf);
					for (int i = 0; i < 3 || compositeIface == null; i++) {
						compositeIface = findCompositeInterface(c.getSuperJavaClass(), compositeOf);
					}
					

					if (compositeIface == null) {
						// no idea why the compiler thinks this is dead code
						System.out.println("Error: Could not find interface \"" + compositeOf  + "\" for the class " + c.getFullyQualifiedName());
					} else {
						// we found the interface! Now add all the classes derived from that interface to the compositeClasses
						System.out.println(compositeIface);
						List<JavaClass> compositeClasses = compositeIface.getDerivedClasses();
						System.out.println(compositeClasses);
						
						// get all the classes that implement or extend this class because we already know they are a composite
						compositeClasses.addAll(c.getDerivedClasses());
						System.out.println(compositeClasses);
						result.add(compositeClasses);
					}
				}
			}
		}
		return result;
	}

	private JavaClass findCompositeInterface(JavaClass c, String compositeOf) {
		List<JavaClass> ifaces = c.getImplementedInterfaces();
		for (JavaClass i : ifaces) {
			if (i.getName().equalsIgnoreCase(compositeOf)) {
				return i;
			}
		}
		return null;
	}

}
