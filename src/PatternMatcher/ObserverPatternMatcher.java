package PatternMatcher;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import util.DesignPattern;

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

		Collection<DesignPattern> patterns = new LinkedList<DesignPattern>();
		Collection<JavaClass> classes = builder.getClasses();

		for (JavaClass c : classes) {
			Collection<JavaClass> observer = new HashSet<JavaClass>();
			Collection<JavaClass> subjects = new HashSet<JavaClass>();

			// Finding Observer
			if (c.isInterface() && c.getName().contains("Listener")) {
				if (c.getDerivedClasses().size() != 0) {

					// finding subjects
					for (JavaClass s : classes) {
						for (JavaMethod jm : s.getMethods()) {
							for (JavaType jt : jm.getParameterTypes()) {
								if (jt.getCanonicalName().equals(
										c.getCanonicalName())) {
									while (notIncludedInSubjects(subjects, s)) {
										subjects.add(s);
										patterns.add(getObserverPattern(c, s));
									}

								}

							}

						}
					}
				}
			}
		}
		return new LinkedList<Collection<JavaClass>>();
	}

	private boolean notIncludedInSubjects(Collection<JavaClass> subjects,
			JavaClass s) {

		if (subjects.contains(s)) {
			return true;
		}

		for (JavaClass jc : subjects) {
			if (jc.getDerivedClasses().contains(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds all the classes involved in the observer pattern.
	 * 
	 * @param observer
	 *            the observer listener interface on top of the observer
	 *            hierarchy
	 * @param subject
	 *            the subject on the top of the subject hierarchy that registers
	 *            the observer
	 * @return the set of classes participating in the visitor pattern
	 */
	private DesignPattern getObserverPattern(JavaClass observer,
			JavaClass subject) {
		DesignPattern pattern = new DesignPattern("Observer");
		pattern.addHierarchy(observer);
		pattern.addHierarchy(subject);
		pattern.addConnection(subject, observer, "register");

		return pattern;
	}

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		ObserverPatternMatcher opm = new ObserverPatternMatcher(builder);
		opm.patternMatch(builder);

	}
}
