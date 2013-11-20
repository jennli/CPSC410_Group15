package PatternMatcher;
import java.util.Collection;

import util.DesignPattern;

import com.thoughtworks.qdox.JavaProjectBuilder;

public abstract class AbstractPatternMatcher {
	
	public abstract Collection<DesignPattern> patternMatch(JavaProjectBuilder builder);
	
}
