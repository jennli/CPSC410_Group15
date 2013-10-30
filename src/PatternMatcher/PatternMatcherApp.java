package PatternMatcher;
import java.io.File;

import com.thoughtworks.qdox.JavaProjectBuilder;

public class PatternMatcherApp {

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		SingletonPatternMatcher spf = new SingletonPatternMatcher(builder);
		System.out.println("\n There are " + spf.patternMatch(builder).size()
				+ " instances of the Singleton Pattern in JHotDraw");
	}
}
