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
package com.github.openasn1.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.astutils.ASN1ASTStorage;
import com.github.openasn1.compiler.astutils.OMAndASN1ASTStorage;
import com.github.openasn1.compiler.stages.generics.ASTModifier;
import com.github.openasn1.compiler.stages.generics.PlugableStage;
import com.github.openasn1.compiler.stages.generics.PlugableStageBuilder;
import com.github.openasn1.compiler.stages.parsing.ParsingStage;
import com.github.openasn1.compiler.utils.GlobalConfiguration;

/**
 * @author Clayton Hoss
 * 
 */
public class ASN1Compiler {

	private static Logger LOGGER = Logger.getLogger("ASN1Compiler");

	private ParsingStage parsingStage;

	private PlugableStage<ASN1ASTStorage, ASTModifier<ASN1ASTStorage>> ASN1ASTStage;

	private PlugableStage<OMAndASN1ASTStorage, ASTModifier<OMAndASN1ASTStorage>> OMStage;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ASN1Compiler compiler = new ASN1Compiler();
		compiler.compile(args);
	}

	public void compile(String[] args) {
		try {
			deleteOutputDir();
			initParsingStage(args);
			initASN1ASTStage();
			initOMAndASN1ASTStage();
			ASN1ASTStorage storage = doParsingStage();
			storage = doASN1ASTStage(storage);
			OMAndASN1ASTStorage storage2 = new OMAndASN1ASTStorage(storage);
			doOMAndASN1ASTStage(storage2);
		} catch (CompilationStoppingException e) {
			LOGGER.error("Compilation Stopped", e);
		}
	}

	public void initParsingStage(String[] files) {
		setParsingStage(new ParsingStage(files));
	}

	public ASN1ASTStorage doParsingStage() {
		return getParsingStage().parseFiles();
	}

	public ASN1ASTStorage doASN1ASTStage(ASN1ASTStorage storage) {
		return getASN1ASTStage().processStage(storage);
	}

	public OMAndASN1ASTStorage doOMAndASN1ASTStage(OMAndASN1ASTStorage storage) {
		return getOMStage().processStage(storage);
	}

	public void initASN1ASTStage() {
		Properties props = new Properties();
		try {
			props.loadFromXML(new FileInputStream(new File(GlobalConfiguration
					.getInstance().getProperty("compiler.stage1.configfile"))));
		} catch (InvalidPropertiesFormatException e) {
			LOGGER.error("ASN1AST Stage Config File is not a property File", e);
			throw new CompilationStoppingException();
		} catch (FileNotFoundException e) {
			LOGGER.error("ASN1AST Stage Config File not found", e);
			throw new CompilationStoppingException();
		} catch (IOException e) {
			LOGGER.error("Error Reading ASN1AST Stage Config File", e);
			throw new CompilationStoppingException();
		}

		PlugableStageBuilder<ASN1ASTStorage, ASTModifier<ASN1ASTStorage>> builder = new PlugableStageBuilder<ASN1ASTStorage, ASTModifier<ASN1ASTStorage>>(
				props);
		PlugableStage<ASN1ASTStorage, ASTModifier<ASN1ASTStorage>> stage = builder
				.buildStage();
		stage.initPlugins();
		setASN1ASTStage(stage);
	}

	public void initOMAndASN1ASTStage() {
		// TODO This shit
		Properties props = new Properties();
		try {
			props.loadFromXML(new FileInputStream(new File(GlobalConfiguration
					.getInstance().getProperty("compiler.stage2.configfile"))));
		} catch (InvalidPropertiesFormatException e) {
			LOGGER
					.error(
							"ObjectModel and ASN1AST Stage Config File is not a property File",
							e);
			throw new CompilationStoppingException();
		} catch (FileNotFoundException e) {
			LOGGER.error("ObjectModel and ASN1AST Stage Config File not found",
					e);
			throw new CompilationStoppingException();
		} catch (IOException e) {
			LOGGER.error(
					"Error Reading ObjectModel and ASN1AST Stage Config File",
					e);
			throw new CompilationStoppingException();
		}

		PlugableStageBuilder<OMAndASN1ASTStorage, ASTModifier<OMAndASN1ASTStorage>> builder = new PlugableStageBuilder<OMAndASN1ASTStorage, ASTModifier<OMAndASN1ASTStorage>>(
				props);
		PlugableStage<OMAndASN1ASTStorage, ASTModifier<OMAndASN1ASTStorage>> stage = builder
				.buildStage();
		stage.initPlugins();
		setOMStage(stage);
	}

	/**
	 * @param parsingStage
	 *            the parsingStage to set
	 */
	private void setParsingStage(ParsingStage parsingStage) {
		this.parsingStage = parsingStage;
	}

	/**
	 * @return the aSN1ASTStage
	 */
	private PlugableStage<ASN1ASTStorage, ASTModifier<ASN1ASTStorage>> getASN1ASTStage() {
		return this.ASN1ASTStage;
	}

	/**
	 * @param stage
	 *            the aSN1ASTStage to set
	 */
	private void setASN1ASTStage(
			PlugableStage<ASN1ASTStorage, ASTModifier<ASN1ASTStorage>> stage) {
		this.ASN1ASTStage = stage;
	}

	/**
	 * @return the parsingStage
	 */
	private ParsingStage getParsingStage() {
		return this.parsingStage;
	}

	/**
	 * @return the oMStage
	 */
	private PlugableStage<OMAndASN1ASTStorage, ASTModifier<OMAndASN1ASTStorage>> getOMStage() {
		return this.OMStage;
	}

	/**
	 * @param stage
	 *            the oMStage to set
	 */
	private void setOMStage(
			PlugableStage<OMAndASN1ASTStorage, ASTModifier<OMAndASN1ASTStorage>> stage) {
		this.OMStage = stage;
	}

	private void deleteOutputDir() {
		File file = new File(GlobalConfiguration.getInstance().getProperty(
				"compiler.codegeneration.output"));
		try {
			LOGGER.info("Deleting directory " + file.getCanonicalPath());
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			LOGGER.error("Error deleting output dir", e);
		}
	}
}
