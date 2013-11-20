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
	static Collection<DesignPattern> observers = new LinkedList<DesignPattern>();
	Collection<JavaClass> allClasses;

	public ObserverPatternMatcher(JavaProjectBuilder jpb) {
		this.jpb = jpb;
		allClasses = jpb.getClasses();
	}

	@Override
	public Collection<DesignPattern> patternMatch(
			JavaProjectBuilder builder) {
		builder = this.jpb;

		for (JavaClass c : allClasses) {
			// Finding Observer
			if (c.isInterface() && c.getName().contains("Listener")) {
				if (c.getDerivedClasses().size() != 0) {
					DesignPattern dp = getObserverPattern(c);
					if (dp != null)
						observers.add(dp);
				}
			}

		}

		return observers;
	}

	/**
	 * checks whether the given subject class s has already been added to the
	 * instance of the pattern
	 * 
	 * @param subjects
	 * @param s
	 * @return
	 */
	private boolean includedInSubjects(Collection<JavaClass> subjects,
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
	 * @return the set of classes participating in the observer pattern
	 */
	private DesignPattern getObserverPattern(JavaClass observer) {

		DesignPattern pattern = new DesignPattern("Observer",
				observer.getName());
		pattern.addHierarchy(observer);
		Collection<JavaClass> subjects = new HashSet<JavaClass>();
		// finding subjects
		for (JavaClass s : allClasses) {
			for (JavaMethod jm : s.getMethods()) {
				for (JavaType jt : jm.getParameterTypes()) {
					if (jt.getCanonicalName().equals(
							observer.getCanonicalName())) {

						while (!includedInSubjects(subjects, s)) {
							subjects.add(s);
							pattern.addHierarchy(s);
							pattern.addConnection(s, observer, "register");
						}
					}
				}
			}
		}
		if (subjects.size() == 0)
			return null;

		// debug
		Collection<JavaClass> nodes = pattern.getNodes();
		for (JavaClass jc : nodes) {
			System.out.println(jc.getName());
		}
		System.out.println();

		return pattern;
	}

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		ObserverPatternMatcher opm = new ObserverPatternMatcher(builder);
		opm.patternMatch(builder);
		System.out.println("Found " + getObserverCount()
				+ " observer patterns.");
	}

	public static int getObserverCount() {
		return observers.size();
	}
}
