package hsplet.compiler;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LibraryLoader extends ClassLoader {

	Set usedLibs = new HashSet();

	Map loadedClasses = new HashMap();

	String[] optlibs;

	URLClassLoader[] optloaders;

	public LibraryLoader(String[] libs, String[] libdirs, ClassLoader parent) throws MalformedURLException {
		super(new URLClassLoader(toURLs(libs), parent));

		usedLibs.addAll(Arrays.asList(libs));

		this.optlibs = collectLibs(libdirs);

		optloaders = new URLClassLoader[optlibs.length];
		for (int i = 0; i < optlibs.length; ++i) {
			optloaders[i] = new URLClassLoader(new URL[] { new File(optlibs[i]).getAbsoluteFile().toURL() }, parent);
		}
	}

	public Set getUsedLibs() {
		return usedLibs;
	}

	protected Class findClass(String name) throws ClassNotFoundException {

		Class existing = (Class) loadedClasses.get(name);
		if (existing != null) {
			return existing;
		}

		for (int i = 0; i < optloaders.length; ++i) {
			try {

				Class loaded = optloaders[i].loadClass(name);

				usedLibs.add(optlibs[i]);
				loadedClasses.put(name, loaded);

				return loaded;

			} catch (Exception e) {
			}
		}

		for (int i = 0; i < optlibs.length; ++i) {
			System.out.println("Found jar:" + optlibs[i]);
		}

		return super.findClass(name);
	}

	private static URL[] toURLs(String[] libs) throws MalformedURLException {

		URL[] result = new URL[libs.length];

		for (int i = 0; i < libs.length; ++i) {
			result[i] = new File(libs[i]).getAbsoluteFile().toURL();
		}
		return result;
	}

	private static String[] collectLibs(String[] libdirs) {

		List result = new ArrayList();

		for (int i = 0; i < libdirs.length; ++i) {

			File[] files = new File(libdirs[i]).listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".jar");
				}
			});

			for (int j = 0; j < files.length; ++j) {
				result.add(files[j].getPath());
			}
		}

		return (String[]) result.toArray(new String[0]);
	}

}
