/*
 *   openASN.1 - an open source ASN.1 toolkit for java
 *
 *   Copyright (C) 2007 Clayton Hoss, Marc Weyland
 *
 *   This file is part of openASN.1
 *
 *   openASN.1 is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   openASN.1 is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with openASN.1. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.github.openasn1.compiler.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;


/**
 * @author Clayton Hoss
 * 
 */
public class GlobalConfiguration {
	private static Logger LOGGER = Logger.getLogger("GlobalConfiguration");

	private static GlobalConfiguration INSTANCE;

	private Properties config;

	private GlobalConfiguration(String filename) {
		this.config = new Properties();
		try {
			this.config.loadFromXML(new FileInputStream(filename));
		} catch (InvalidPropertiesFormatException e) {
			LOGGER.error("Invalid Compiler configuration File");
			throw new CompilationStoppingException("Compiler Config File Error",e);
		} catch (FileNotFoundException e) {
			LOGGER.error("Compiler Configuration File not found");
			throw new CompilationStoppingException("Compiler Config File Error",e);
		} catch (IOException e) {
			LOGGER.error("Error Accessing Compiler Configuration File");
			throw new CompilationStoppingException("Compiler Config File Error",e);
		}
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		return this.config.getProperty(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public Object setProperty(String key, String value) {
		return this.config.setProperty(key, value);
	}

	public Properties getConfig() {
		return this.config;
	}

	public static void init() {
		init("config/GlobalConfig.xml");
	}

	public static void init(String filename) {
		INSTANCE = new GlobalConfiguration(filename);
	}

	public static GlobalConfiguration getInstance() {
		if (INSTANCE == null) {
			init();
		}
		return INSTANCE;
	}
}
