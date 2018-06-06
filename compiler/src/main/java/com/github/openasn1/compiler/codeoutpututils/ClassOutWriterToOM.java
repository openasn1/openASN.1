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
import com.github.openasn1.compiler.utils.GlobalConfiguration;
import com.github.openasn1.compiler.utils.PrimeNumbers;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Clayton Hoss
 *
 */
public class ClassOutWriterToOM {
	private static Logger LOGGER = Logger.getLogger("ClassOutWriter");

	private Configuration templateRepository;

	private String basedir;

	public ClassOutWriterToOM() {
		this.basedir = "output";
		initialize();
	}

	public ClassOutWriterToOM(String basedir) {
		this.basedir = basedir;
		initialize();
	}

	private void initialize() {
		this.templateRepository = new Configuration();
		try {
			this.templateRepository.setDirectoryForTemplateLoading(new File(
					GlobalConfiguration.getInstance().getProperty(
							"compiler.templates.path")
							+ "/om"));
		} catch (IOException e) {
			LOGGER.error("Template directory not found ", e);
			throw new CompilationStoppingException(
					"Template Directory not found");
		}

		this.templateRepository.setObjectWrapper(new DefaultObjectWrapper());

		File temp = new File(getBasedir());
		temp.mkdirs();
	}

	public void writeClass(ClassInformationHolder theClass) {
		String packageDir = getPackageDir(theClass.getPackagename());
		createDir(packageDir);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(
					packageDir + File.separator + theClass.getName() + ".java"));
			writeFileHeader(output, theClass);

			writeSubclass(theClass, output);
			output.flush();
			output.close();
		} catch (IOException e) {
			LOGGER.error("Error writing class " + theClass.getPackagename()
					+ "." + theClass.getName(), e);
		} catch (TemplateException e) {
			LOGGER.error("Error parsing Template for class "
					+ theClass.getPackagename() + "." + theClass.getName(), e);
		}
	}

	/**
	 * @param theClass
	 * @param output
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void writeSubclass(ClassInformationHolder theClass,
			BufferedWriter output) throws TemplateException, IOException {
		if (theClass.isEnum()) {
			writeEnumSubclass(theClass, output);
		} else {
			writeNormalSubclass(theClass, output);
		}
	}

	private void writeEnumSubclass(ClassInformationHolder theClass,
			BufferedWriter output) throws TemplateException, IOException {

		Template template = getTemplateRepository().getTemplate("enum.ftl");
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("className", theClass.getName());
		attributes.put("enumItems", theClass.getMembers().keySet());
		template.process(attributes, output);
	}


	/**
	 * @param theClass
	 * @param output
	 * @throws TemplateException
	 * @throws IOException
	 */
	private void writeNormalSubclass(ClassInformationHolder theClass,
			BufferedWriter output) throws TemplateException, IOException {
		writeClassHeader(output, theClass);
		for (ClassInformationHolder myClass : theClass.getSubClasses()) {
			writeSubclass(myClass, output);
		}
		writeMembers(output, theClass);
		writeCustomMethods(output, theClass);
		writeGetter(output, theClass);
		writeSetter(output, theClass);
		writeEqualsMethod(output, theClass);
		writeHashCodeMethod(output, theClass);
		writeClassFooter(output, theClass);
	}

	private void writeGetter(BufferedWriter writer,
			ClassInformationHolder theClass) throws IOException,
			TemplateException {

		for (String name : theClass.getMembers().keySet()) {
			Template template = getTemplateRepository().getTemplate(
					"getter.ftl");
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("class", theClass.getMembers().get(name));
			attributes.put("member", name);
			String line = theClass.getGetterCode().get(name);
			if (line == null) {
				line = "";
			}
			attributes.put("customCode", line);
			template.process(attributes, writer);
		}
	}

	private void writeSetter(BufferedWriter writer,
			ClassInformationHolder theClass) throws IOException,
			TemplateException {

		for (String name : theClass.getMembers().keySet()) {
			Template template = getTemplateRepository().getTemplate(
					"setter.ftl");
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("class", theClass.getMembers().get(name));
			attributes.put("member", name);
			String line = theClass.getSetterCode().get(name);
			if (line == null) {
				line = "";
			}
			attributes.put("customCode", line);
			template.process(attributes, writer);
		}
	}

	private void writeFileHeader(BufferedWriter writer,
			ClassInformationHolder theClass) throws IOException,
			TemplateException {
		Template template;
		template = getTemplateRepository().getTemplate("preHeader.ftl");
		template.process(new HashMap<String, String>(), writer);

		template = getTemplateRepository().getTemplate("packageDef.ftl");
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("package", theClass.getPackagename());
		template.process(attributes, writer);

		for (String theImport : theClass.getImports()) {
			attributes = new HashMap<String, String>();
			template = getTemplateRepository().getTemplate("import.ftl");
			attributes.put("import", theImport);
			template.process(attributes, writer);
		}

	}

	private void writeEqualsMethod(BufferedWriter writer,
			ClassInformationHolder theClass) throws IOException,
			TemplateException {

		Template template = getTemplateRepository().getTemplate(
				"equalsHeader.ftl");
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("class", theClass.getName());
		template.process(attributes, writer);

		for (String member : theClass.getMembers().keySet()) {
			template = null;
			String className = theClass.getMembers().get(member);
			attributes = new HashMap<String, String>();
			attributes.put("member", member);

			if (isPrimitiveTypeName(className)) {
				template = getTemplateRepository().getTemplate(
						"equalsMemberComparePrimitive.ftl");
			} else {
				template = getTemplateRepository().getTemplate(
						"equalsMemberCompareObject.ftl");
			}
			template.process(attributes, writer);
		}

		// for (String member : theClass.getMembers().keySet()) {
		// template = getTemplateRepository().getInstanceOf(
		// "equalsMemberComparePrimitive");
		// template.setAttribute("member", member);
		// writeTemplate(template, writer);
		// }

		template = getTemplateRepository().getTemplate("equalsFooter.ftl");
		template.process(new HashMap<String, String>(), writer);
	}

	private void writeCustomMethods(BufferedWriter writer,
			ClassInformationHolder theClass) {
		for (String s : theClass.getCustomMethods()) {
			try {
				writer.write(s);
				writer.newLine();
			} catch (IOException e) {
				LOGGER.error("Error writing output File", e);
				throw new CompilationStoppingException("Error writing output");
			}
		}

	}

	private void writeHashCodeMethod(BufferedWriter writer,
			ClassInformationHolder theClass) throws IOException,
			TemplateException {
		Template template;

		Map<String, String> attributes = new HashMap<String, String>();
		template = getTemplateRepository().getTemplate("hashcodeHeader.ftl");

		attributes.put("prime", PrimeNumbers.getRandomPrimeAsString());
		template.process(attributes, writer);

		for (String member : theClass.getMembers().keySet()) {

			template = null;
			String className = theClass.getMembers().get(member);

			attributes = new HashMap<String, String>();
			attributes.put("member", member);
			attributes.put("prime", PrimeNumbers.getRandomPrimeAsString());

			if (isPrimitiveTypeName(className)) {
				template = getTemplateRepository().getTemplate(
						"hashcodePrimitiveMember.ftl");
			} else {
				template = getTemplateRepository().getTemplate(
						"hashcodeObjectMember.ftl");
			}
			template.process(attributes, writer);

		}
		template = getTemplateRepository().getTemplate("hashcodeFooter.ftl");
		template.process(new HashMap<String, String>(), writer);
	}

	private void writeClassHeader(BufferedWriter writer,
			ClassInformationHolder theClass) throws TemplateException,
			IOException {
		Template template = getTemplateRepository().getTemplate("classDef.ftl");
		Map<String, String> attributes = new HashMap<String, String>();

		attributes.put("className", theClass.getName());
		attributes.put("extend", theClass.getExtendsClass());
		template.process(attributes, writer);
	}

	private void writeClassFooter(BufferedWriter writer,
			ClassInformationHolder theClass) throws TemplateException,
			IOException {
		Template template = getTemplateRepository().getTemplate(
				"classFooter.ftl");
		template.process(new HashMap<String, String>(), writer);
	}

	private void writeMembers(BufferedWriter writer,
			ClassInformationHolder theClass) throws TemplateException,
			IOException {
		for (String name : theClass.getMembers().keySet()) {
			Template template = getTemplateRepository().getTemplate(
					"member.ftl");
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("class", theClass.getMembers().get(name));
			attributes.put("member", name);
			template.process(attributes, writer);
		}
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

	private boolean isPrimitiveTypeName(String name) {
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
