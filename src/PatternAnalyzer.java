import java.io.File;

import com.thoughtworks.qdox.JavaProjectBuilder;

public class PatternAnalyzer {

	public static void main(String[] args) {
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		SingletonPatternFinder spf = new SingletonPatternFinder(builder);
		spf.singletonSearch();
		System.out.println("Number of singleton pattern used in JHotDraw: "
				+ spf.getSingletons().size());
	}
}
