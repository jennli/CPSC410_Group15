import java.util.Collection;

import com.thoughtworks.qdox.model.JavaClass;

public abstract class AbstractPatternMatcher {
	
	public abstract void patternMatch();
	public Collection<JavaClass> returnMatchedClasses;
}
