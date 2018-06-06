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
package com.github.openasn1.compiler.codeoutpututils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.utils.GlobalConfiguration;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Clayton Hoss
 *
 */
public class ClassOutWriterToOMCoder {
	private static Logger LOGGER = Logger.getLogger("ClassOutWriter");

	private Configuration templateRepository;

	private String basedir;

	public ClassOutWriterToOMCoder() {
		this.basedir = "output";
		initialize();
	}

	public ClassOutWriterToOMCoder(String basedir) {
		this.basedir = basedir;
		initialize();
	}

	private void initialize() {
		this.templateRepository = new Configuration();
		try {
			this.templateRepository.setDirectoryForTemplateLoading(new File(GlobalConfiguration.getInstance().getProperty("compiler.templates.path") + "/omcoder"));
		} catch (IOException e) {
			LOGGER.error("Template directory not found ", e);
			throw new CompilationStoppingException("Template Directory not found");
		}
		this.templateRepository.setObjectWrapper(new DefaultObjectWrapper());

		File temp = new File(getBasedir());
		temp.mkdirs();
	}

	public void writeType(AbstractASN1Type theType, String packageName) {
		String packageDir = getPackageDir(packageName);
		createDir(packageDir);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(packageDir + File.separator + theType.getName() + "Encoder.java"));
			writeFileHeader(output, packageName);
			writeEnocderClass(output, theType);
			output.flush();
			output.close();
		} catch (IOException e) {
			LOGGER.error("Error writing type " + packageName + "." + theType.getName(), e);
		} catch (TemplateException e) {
			LOGGER.error("Error parsing Template for type " + packageName + "." + theType.getName(), e);
		}
	}

	/**
     * @param theClass
     * @param output
     * @throws IOException
     * @throws TemplateException
     */
	private void writeEnocderClass(BufferedWriter output, AbstractASN1Type theType) throws TemplateException, IOException {
		writeClassHeader(output, theType);
		writeClassFooter(output, theType);
	}

	private void writeFileHeader(BufferedWriter writer, String packageName) throws IOException, TemplateException {
		Template template;
		template = getTemplateRepository().getTemplate("preHeader.ftl");
		template.process(new HashMap<String, String>(), writer);

		template = getTemplateRepository().getTemplate("encoderPackageDef.ftl");
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("package", packageName);
		template.process(attributes, writer);
	}

	private void writeClassHeader(BufferedWriter writer, AbstractASN1Type theType) throws TemplateException, IOException {
		Template template = getTemplateRepository().getTemplate("encoderClassHeader.ftl");
		Map<String, String> attributes = new HashMap<String, String>();

		attributes.put("className", theType.getName() + "Encoder");
		attributes.put("omClassName", theType.getName());
		template.process(attributes, writer);
	}

	private void writeClassFooter(BufferedWriter writer, AbstractASN1Type theType) throws TemplateException, IOException {
		Template template = getTemplateRepository().getTemplate("encoderClassFooter.ftl");
		template.process(new HashMap<String, String>(), writer);
	}

	private void createDir(String dir) {
		new File(dir).mkdirs();
	}

	/**
     * @param packageName
     */
	private String getPackageDir(String packageName) {
		packageName = packageName.replace('.', File.separatorChar);
		return (getBasedir() + File.separator + packageName);
	}

	/**
     * @return the basedir
     */
	private String getBasedir() {
		return this.basedir;
	}

	/**
     * @return the templateRepository
     */
	private Configuration getTemplateRepository() {
		return this.templateRepository;
	}

	protected boolean isPrimitiveTypeName(String name) {
		if (name.equals("boolean")) {
			return true;
		}
		if (name.equals("float")) {
			return true;
		}
		if (name.equals("double")) {
			return true;
		}
		if (name.equals("long")) {
			return true;
		}
		if (name.equals("int")) {
			return true;
		}
		return false;
	}

}
