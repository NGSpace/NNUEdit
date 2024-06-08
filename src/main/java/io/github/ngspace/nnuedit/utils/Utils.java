package io.github.ngspace.nnuedit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class Utils {
	
	private Utils() {}
	
	
	
	public static <T> T[] concatArrays(T[] arr1, T[] arr2) {
	    T[] result = Arrays.copyOf(arr1, arr1.length + arr2.length);
	    System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
	    return result;
	}
	
	
	public static int safeParseInt(String string) {try{return Integer.parseInt(string);} catch (Exception e) {return 0;}}
	
	/*
	 * Resource management
	 */
	
	public static InputStream getResourceAsStream(String string) {
		return Utils.class.getResourceAsStream("/io/github/ngspace/nnuedit"+string);
	}
	public static InputStream getAssetAsStream(String string) {
		return getResourceAsStream("/Assets/"+string);
	}
	
	
	
	public static Resource[] getResources(String pack) throws IOException {
	    PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
	    return pmrpr.getResources("/" + pack.replace(".", "/") + "/*");
	}
	
	public static String[] getResourcesAsString(String loc) throws IOException {return getResourcesAsString(loc,"");}
	public static String[] getResourcesAsString(String loc, String prefix) throws IOException {
		Resource[] resources = getResources(loc);
		String[] result = new String[resources.length];
		for (int i = 0;i<resources.length;i++) result[i] = prefix + resources[i].getFilename();
	    return result;
	}
	
	public static String[] getResourcesName(String loc) throws IOException {return getResourcesAsString(loc,"");}
	public static String[] getResourcesName(String loc, String prefix) throws IOException {
		Resource[] resources = getResources(loc);
		String[] result = new String[resources.length];
		for (int i = 0;i<resources.length;i++) result[i] = prefix + FileIO.getFileNameWOExt(resources[i].getFilename());
	    return result;
	}


	
}
