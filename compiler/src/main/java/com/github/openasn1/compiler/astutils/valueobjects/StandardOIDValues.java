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
package com.github.openasn1.compiler.astutils.valueobjects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.utils.GlobalConfiguration;


/**
 * @author Clayton Hoss
 * 
 */
public class StandardOIDValues {
	private static StandardOIDValues INSTANCE;

	private static Logger LOGGER = Logger.getLogger("StandardOIDValues");

	private Properties prop = new Properties();

	private StandardOIDValues(String propertyfile) {
		try {
			this.prop.loadFromXML(new FileInputStream(new File(propertyfile)));
		} catch (InvalidPropertiesFormatException e) {
			LOGGER.error("Malformed Property File ", e);
		} catch (FileNotFoundException e) {
			LOGGER.error("File not Found ", e);
		} catch (IOException e) {
			LOGGER.error("Error while Accessing PropertyFile", e);
		}
	}

	public static void init() {
		INSTANCE = new StandardOIDValues(GlobalConfiguration.getInstance()
				.getProperty("compiler.values.oids.configfile"));
	}

	public static void init(String file) {
		INSTANCE = new StandardOIDValues(file);
	}

	public static StandardOIDValues getInstance() {
		if (INSTANCE == null) {
			init();
		}
		return INSTANCE;
	}

	public Integer getOIDValue(String key) {
		String val = this.prop.getProperty(key);
		if (val == null) {
			return null;
		}
		return new Integer(val);
	}
}
