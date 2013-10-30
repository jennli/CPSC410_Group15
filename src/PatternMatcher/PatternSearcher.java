package PatternMatcher;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaConstructor;
import com.thoughtworks.qdox.model.JavaMethod;


public class PatternSearcher {

	Set<String> classNames = new HashSet<String>();

	public PatternSearcher(){
		
	}
	
	public void SingletonSearch(){
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		Collection<JavaClass> classes = builder.getClasses();
		for (JavaClass c: classes){
			List<JavaMethod> methods = c.getMethods();
			for (JavaMethod m: methods){
				//System.out.println("Method name is: " + m.getName());
				if (m.getName().equals("getInstance")){
					Collection<JavaConstructor> jCon= c.getConstructors();
					for (JavaConstructor jc : jCon){
						List<String> jm = jc.getModifiers();
						for (String s : jm){
							if (s.equals("private")){
								classNames.add(c.getName());
							}
						}
					}
				}
			}
		}
		if (classNames.isEmpty()){
			System.out.println("No class is involved in Singleton design pattern");
		}else
			
		for(String l : classNames){
			System.out.println("The class name that is involved in Singleton design patter: " + l);
		}
	}
}
