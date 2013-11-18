
import java.io.File;

import PatternMatcher.SingletonPatternMatcher;

import com.thoughtworks.qdox.JavaProjectBuilder;

public class PatternMatcherApp {

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		SingletonPatternMatcher spf = new SingletonPatternMatcher();
		spf.patternMatch(builder);

	}
}
