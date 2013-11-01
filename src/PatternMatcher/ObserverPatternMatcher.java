package PatternMatcher;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;

public class ObserverPatternMatcher extends AbstractPatternMatcher {

	JavaProjectBuilder jpb = new JavaProjectBuilder();
	Collection<Collection<JavaClass>> Observers = new HashSet<Collection<JavaClass>>();

	public ObserverPatternMatcher(JavaProjectBuilder jpb) {
		this.jpb = jpb;
	}

	@Override
	public Collection<Collection<JavaClass>> patternMatch(
			JavaProjectBuilder builder) {
		builder = this.jpb;

		Collection<Collection<JavaClass>> match = new HashSet<Collection<JavaClass>>();
		Collection<JavaClass> classes = builder.getClasses();

		for (JavaClass c : classes) {
			Collection<JavaClass> observer = new HashSet<JavaClass>();
			Collection<JavaClass> subjects = new HashSet<JavaClass>();

			// Finding Observer
			if (c.isInterface() && c.getName().contains("Listener")) {
				// only consider interface with non-empty derived classes
				Collection<JavaClass> derivedClasses = c.getDerivedClasses();
				if (derivedClasses.size() != 0) {
					observer.add(c);
					// find all classes extend or implement this observer
					Collection<JavaClass> observerDependants = getObserverDependants(
							c, derivedClasses);
					observer.addAll(observerDependants);
					// Finding Subject
					subjects = getSubject(classes, c);

					System.out
							.println("\n One possible instance of observer pattern has the main Listener class"
									+ c.getName()
									+ " and consists of concrete observers: ");
					for (JavaClass x : observer)
						System.out.println("\t" + x.getName());
					System.out.println("\nAnd subjects: ");
					if (subjects.size() != 0) {
						for (JavaClass w : subjects) {
							System.out.println("\t" + w.getName());
						}
					} else
						System.out
								.println("No subjects found for this observer, this listener doesn't qualify for observer. ");
				}

			}

			if (observer.size() != 0 && subjects.size() != 0) {
				match.add(observer);
				match.add(subjects);
				System.out
						.println("--this is a valid observer instance, added to the result---");
			}

			// else {
			// System.out.println("This is not a valid instance of observer.");
			// }
		}
		// System.out.println("\n match contains: " + match);

		System.out.println("There are " + match.size() / 2
				+ " instances of observer design pattern.");
		return match;
	}

	// Finding possible observer class c
	private Collection<JavaClass> getSubject(Collection<JavaClass> classes,
			JavaClass c) {
		Collection<JavaClass> subjects = new HashSet<JavaClass>();
		for (JavaClass s : classes) {
			for (JavaMethod jm : s.getMethods()) {
				for (JavaType jt : jm.getParameterTypes()) {
					if (jt.getCanonicalName().equals(c.getCanonicalName())) {
						// System.out.println("\n\t\t" + s.getName()
						// + " is a candidate subject of " + c.getName());
						// System.out.println("\t\t method: " + jm.getName()
						// + "; type: " + jt.getCanonicalName() + "\n");
						while (!subjects.contains(s)) {
							subjects.add(s);
							// System.out.println("add " + s.getName()
							// + " to subjects");
						}

					}

				}

			}
		}
		return subjects;
	}

	// select the derived classes that participate in the observer pattern
	private Collection<JavaClass> getObserverDependants(JavaClass c,
			Collection<JavaClass> derivedClasses) {
		Collection<JavaClass> observerDependants = new HashSet<JavaClass>();
		for (JavaClass dc : derivedClasses) {
			// look for sub-Listeners, add to the dependent lit
			if (dc.isInterface() && dc.getImplements().contains(c)) {
				observerDependants.add(dc);

				// Look for realizations of interface c and their subclasses
			} else if (dc.getInterfaces().size() != 0
					|| dc.getImplements() != null) { // or get SuperClass?
				observerDependants.add(dc);
				// checkDerivedClassHierachy(c, derivedClasses, dc);
			}
		}
		return observerDependants;
	}

	// private void checkDerivedClassHierachy(JavaClass c,
	// Collection<JavaClass> derivedClasses, JavaClass dc) {
	// for (JavaClass otherdc : derivedClasses) {
	//
	// if (dc.getImplements().size() != 0) {
	// if (dc.getInterfaces().contains(otherdc)) {
	// System.out.println(dc.getName()
	// + " NOT an interface, implements "
	// + otherdc.getName()
	// + " which is another derived class of "
	// + c.getName());
	// }
	// } else if (dc.getSuperClass() != null) {
	//
	// if (dc.getSuperClass().equals(otherdc)) {
	//
	// System.out
	// .println(dc.getName()
	// + " extends "
	// + otherdc.getName()
	// + " which is another derived (non-interface)class of "
	// + c.getName());
	// }
	// }
	// }
	// if (dc.isInner()) {
	// System.out.println(dc.getName() + " is an inner class of"
	// + dc.getDeclaringClass().getName());
	// }
	// }

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		ObserverPatternMatcher opm = new ObserverPatternMatcher(builder);
		opm.patternMatch(builder);

	}
}
