import java.io.File;

import com.thoughtworks.qdox.JavaProjectBuilder;

public class PatternMatcherApp {

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		SingletonPatternMatcher spf = new SingletonPatternMatcher(builder);
		spf.patternMatch();
		System.out.println("Number of singleton pattern used in JHotDraw: "
				+ spf.returnMatchedClasses.size());
	}
}
