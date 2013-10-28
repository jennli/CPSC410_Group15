import java.util.Collection;

import com.thoughtworks.qdox.model.JavaClass;

public abstract class AbstractPatternMatcher {
	
	public abstract Collection<Collection<JavaClass>> patternMatch();
	
}
