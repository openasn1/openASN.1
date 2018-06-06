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
package com.github.openasn1.compiler.stages.generics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.interfaces.DeepCopyable;
import com.github.openasn1.compiler.utils.GlobalConfiguration;


/**
 * @author Clayton Hoss
 * 
 */
public class PlugableStageBuilder<ASTs extends DeepCopyable, ASTm extends ASTModifier<ASTs>> {

	private static Logger LOGGER = Logger.getLogger("PlugableStage Builder");

	private Properties props;

	public PlugableStageBuilder(Properties prop) {
		this.props = prop;
	}

	public PlugableStage<ASTs, ASTm> buildStage() {
		PlugableStage<ASTs, ASTm> stage = new PlugableStage<ASTs, ASTm>(
				GlobalConfiguration.getInstance().getConfig());
		int pluginAmount = new Integer(getProps().getProperty(
				"compiler.plugin.amount", "0"));

		for (int i = 1; i < pluginAmount + 1; i++) {
			Boolean copyAst = new Boolean(getProps().getProperty(
					"compiler.plugin.plugin" + i + ".copyast"));
			PluginHolder<ASTm> ph = new PluginHolder<ASTm>();

			ph.setAstModifier(getASTModifierForPlugin(i));
			ph.setGetsASTClone(copyAst);
			ph.setProperties(getPropertyFileForPlugin(i));
			stage.getPlugins().add(ph);
		}
		return stage;
	}

	private Properties getPropertyFileForPlugin(int i) {
		String propertyfile = getProps().getProperty(
				"compiler.plugin.plugin" + i + ".propertyfile");
		Properties config = new Properties();
		try {
			config.loadFromXML(new FileInputStream(propertyfile));
		} catch (InvalidPropertiesFormatException e) {
			LOGGER.warn("Invalid Compiler Plugin configuration File",e);
		} catch (FileNotFoundException e) {
			LOGGER.warn("Compiler Plugin Configuration File not found");
		} catch (IOException e) {
			LOGGER.warn("Error Accessing Compiler Plugin Configuration File",e);
		}
		return config;
	}

	@SuppressWarnings("unchecked")
	private ASTm getASTModifierForPlugin(int i) {
		String className = getProps().getProperty(
				"compiler.plugin.plugin" + i + ".class");
		ASTm ast = null;
		try {
			ast = (ASTm) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("Compiler Plugin Cannot be instanciated", e);
			throw new CompilationStoppingException(
					"Error Loading Compiler Plugin: " + className, e);
		} catch (IllegalAccessException e) {
			LOGGER.error("Compiler Plugin Cannot be instanciated", e);
			throw new CompilationStoppingException(
					"Error Loading Compiler Plugin: " + className, e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Compiler Plugin Class cannot be found", e);
			throw new CompilationStoppingException(
					"Error Loading Compiler Plugin: " + className, e);
		}
		return ast;
	}

	/**
	 * @return the props
	 */
	private Properties getProps() {
		return this.props;
	}

}
