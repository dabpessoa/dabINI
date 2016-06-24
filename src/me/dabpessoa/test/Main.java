package me.dabpessoa.test;

import java.util.logging.Logger;

import me.dabpessoa.ini.IniFileManager;

public class Main {
	public static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) {
		
		IniFileManager ini = new IniFileManager("me/dabpessoa/file/env.ini");
		LOGGER.info(ini+"");
		
	}
	
}
