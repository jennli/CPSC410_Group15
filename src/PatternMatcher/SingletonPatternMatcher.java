package PatternMatcher;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import util.DesignPattern;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaConstructor;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;

public class SingletonPatternMatcher extends AbstractPatternMatcher {

	Set<String> classNames = new HashSet<String>();
	Collection<DesignPattern> singletons = new LinkedList<DesignPattern>();
	
	public SingletonPatternMatcher(){

	}
	
	public static void main(String[] args){
		JavaProjectBuilder builder = new JavaProjectBuilder();
		builder.addSourceTree(new File("org")); // path to JHotDraw
		SingletonPatternMatcher cpm = new SingletonPatternMatcher();
		for (DesignPattern d: cpm.patternMatch(builder)){
			System.out.println(d.getInstanceName());
		}
	}

	
	  public Collection<DesignPattern> patternMatch(JavaProjectBuilder builder){
	
		//List<JavaClass> result = new ArrayList<JavaClass>();
		Collection<JavaClass> classes = builder.getClasses();
		for (JavaClass c: classes){
			//this set is later used to store modifiers of constructors
			Set<String> setM = new HashSet<String>();
			List<JavaMethod> methods = c.getMethods();
			for (JavaMethod m: methods){
				//Check if there exist method with the same return type of classname and check if the class is not abstract
				if (m.getReturnType().toString().equals(c.getFullyQualifiedName()) && !c.isAbstract()){
					Collection<JavaField> jf = c.getFields();
					for (JavaField jField: jf){
						//check if there are fields with classname return type
						if (jField.getType().toString().equals(c.getFullyQualifiedName())){

							Collection<JavaConstructor> jCon = c.getConstructors();
							for (JavaConstructor jc : jCon) {
								List<String> jm = jc.getModifiers();
								for (String s : jm) {
									setM.add(s);
								}
							}
							//check if there are no public modifiers in set, which means all constructors are private
							if (!setM.contains("public")){
								DesignPattern dp = new DesignPattern("Singleton", c.getName());
								singletons.add(dp);
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
				System.out.println("The class name that is involved in Singleton design pattern: " + l);
			}


		//return result;
		return singletons; // XXX: change this
	}
}
