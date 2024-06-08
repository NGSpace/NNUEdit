package io.github.ngspace.nnuedit.utils.testing;

import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import io.github.ngspace.nnuedit.Main;
import io.github.ngspace.nnuedit.utils.testing.test_types.Test;

@SuppressWarnings("unchecked")
/** Fuck JUnit... This shit hella overengineered but fuck that */
public class TestExecuter {
	
    public static final String RESET = "\033[0m";
    public static final String BLACK = "\033[0;30m";
    public static final String UNDERLINE = "\033[4;30m";
    public static final String WHITE_BG = "\033[47m";
    public static final String RED_BG = "\033[41m";
    public static final String GREEN_BG = "\033[42m";
    
    public static boolean succesful = true;
    public static String result = "\n\n\n\n" + BLACK + UNDERLINE + WHITE_BG + "Printing tests results: " + RESET + '\n';
    public static final PrintStream out = System.out;
	public static final Test<Exception>[][] tests = new Test[11][];
    
    
    
	public static List<Class<Test<Exception>>> findTestClasses() throws ReflectiveOperationException {
    	List<Class<Test<Exception>>> result = new ArrayList<Class<Test<Exception>>>();
    	ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    	provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
    	Set<BeanDefinition> classes = provider.findCandidateComponents("io.github.ngspace");
    	for (BeanDefinition bean: classes) {
    	    Class<?> clazz = Class.forName(bean.getBeanClassName());
    	    if (Test.class.isAssignableFrom(clazz)) result.add((Class<Test<Exception>>)clazz);
    	}
        return result;
    }
    
    
    
	public static void main(String[] args) throws ReflectiveOperationException {
		int scope = 1;
		if (args.length>0) try {scope = Integer.parseInt(args[0]);} catch (Exception e) {scope = 1;}
		System.out.println("\n\n\n\n");
		
		List<Class<Test<Exception>>> testclasses = findTestClasses();
    	out.println("Loaded following tests:");
    	
    	Callable<Boolean> hook = () -> {
			out.println("Stopped crash! ");
			Thread.currentThread().interrupt();
			throw new SecurityException("Can not call Main.crash(I)V while in test mode");
		};
		UncaughtExceptionHandler exceptionHandler = (t,e)->e.printStackTrace();
		
		for (Class<Test<Exception>> testclass : testclasses) {
			try {
				Main.hook = hook;
				Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
				if (Modifier.isAbstract(testclass.getModifiers())) continue;
				
				Constructor<? extends Test<Exception>> constructor = testclass.getDeclaredConstructor();
				Test<Exception> test = constructor.newInstance();
				
				if (test.getTestScope().scope>scope) continue;
				TestOrder orderannotation = testclass.getAnnotation(TestOrder.class);
				
				int order = orderannotation!=null ? orderannotation.value() + 1 : 2;
				
				out.println(testclass.getName() + " order: " + order);
				if (tests[order]==null) tests[order] = new Test[0];
				tests[order] = addToArray(tests[order],test);
			} catch (Exception e) {
				out.println(testclass.getName() + " Failed initialization");
				add(getClassName(testclass),new TestResult(false));
				e.printStackTrace();
			}
		}
		
		for (Test<? extends Exception>[] tests : tests) {
			if (tests==null) continue;
			for (Test<? extends Exception> test : tests) {
				try {
					Main.hook = hook;
					Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
					TestResult result = test.runTest();
					add(getClassName(test.getClass()),result);
				} catch (Exception e) {
					add(getClassName(test.getClass()),new TestResult(false));
					e.printStackTrace();
				}
			}
		}
		
		
		if (result.length()==0) {out.println("No results");System.exit(1);}
		out.println(result.substring(0, result.length()-1));
		System.exit(succesful?0:1);
	}
	
	
	
	public static void add(String clazz, TestResult res) {
		if (!res.isSuccesful()) succesful = false;
		String str = BLACK + UNDERLINE + (res.isSuccesful() ? GREEN_BG : RED_BG) + clazz + ": " + res + RESET + '\n';
		out.print(str);
		result+= str;
	}
	
	
	
	public static String getClassName(Class<?> clazz) {
		String[] s = (clazz!=null ? clazz.getCanonicalName() : "Unkown").split("\\.");
		return s[s.length-1];
	}
	
	
	
	public static <T> T[] addToArray(T[] arr, T element) {
	    arr = Arrays.copyOf(arr, arr.length + 1);
	    arr[arr.length-1] = element;
	    return arr;
	}
}