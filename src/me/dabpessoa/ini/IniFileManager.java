package me.dabpessoa.ini;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.ini4j.Wini;

public class IniFileManager {
	public static Logger LOGGER = Logger.getLogger(IniFileManager.class.getName());
	
	private Wini iniFile;
	private String resourceFilePath;
	
	public IniFileManager() {}
	
	public IniFileManager(String resourceFilePath) {
		this.resourceFilePath = resourceFilePath;
		try {
			this.init();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao ler o arquivo para o caminho: "+resourceFilePath, e);
		}
	}
	
	public void load(String resourceFilePath) throws IOException {
		InputStream input = getInputStream(getResourceFilePath());
		if (iniFile == null) iniFile = new Wini(input);
		iniFile.load(input);
	}
	
	public void init() throws IOException {
		InputStream input = getInputStream(getResourceFilePath());
		iniFile = new Wini(input);
	}
	
	private InputStream getInputStream(String resourceFilePath) throws IOException {
		if (resourceFilePath == null) throw new RuntimeException("Caminho do arquivo não foi configurado.");
		return getResourceAsInputStream(resourceFilePath);
	}
	
	public String getValue(String key) {
		return iniFile.get("?", key);
	}
	
	public String getValue(String sectionName, String key) {
		return iniFile.get(sectionName, key);
	}
	
	public Set<Entry<String, String>> getSection(String sectionName) {
		Wini.Section section = iniFile.get(sectionName);
		if (section == null) return null;
		return section.entrySet();
	}
	
	public File getFile() {
		return iniFile.getFile();
	}
	
	public void setResourceFilePath(String resourceFilePath) {
		this.resourceFilePath = resourceFilePath;
	}
	
	public String getResourceFilePath() {
		return resourceFilePath;
	}
	
	public Map<String, List<Entry<String, String>>> getMap() {
		Map<String, List<Entry<String, String>>> map = new HashMap<String, List<Entry<String, String>>>();
		
		Set<Entry<String, Wini.Section>> sections = iniFile.entrySet();
		for (Entry<String, Wini.Section> e : sections) {
			
			String sectionName = e.getKey();
			map.put(sectionName, new ArrayList<Entry<String, String>>());
			
			Wini.Section section = e.getValue();
		    Set<Entry<String, String>> properties = section.entrySet();
		    for (Entry<String, String> property : properties) {
		    	map.get(sectionName).add(property);
		    }
		    
		}
		
		return map;
	}
	
	@Override
	public String toString() {
		if (iniFile == null) return super.toString();
		return iniFile.toString();
	}
	
	private InputStream getResourceAsInputStream(String resourceFilePath) throws FileNotFoundException {
		InputStream is = null;
		if (this.getClass() != null) {
			is = getClass().getResourceAsStream(resourceFilePath);
		}
		
		if (is == null) {
			is = getDefaultClassLoader().getResourceAsStream(resourceFilePath);
		}
		
		if (is == null) {
			throw new FileNotFoundException(resourceFilePath + " cannot be opened because it does not exist");
		}
		return is;
	}
	
	private static ClassLoader getDefaultClassLoader() {
		ClassLoader classLoader = null;
		try {
			classLoader = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			/* do nothing */}

		if (classLoader == null) {
			classLoader = IniFileManager.class.getClassLoader();
			if (classLoader == null) {
				try {
					classLoader = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					/* do nothing */}
			}
		}
		return classLoader;
	}
	
}
