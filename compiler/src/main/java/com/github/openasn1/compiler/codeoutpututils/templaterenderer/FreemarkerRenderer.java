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
package com.github.openasn1.compiler.codeoutpututils.templaterenderer;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Clayton Hoss
 * 
 */
public class FreemarkerRenderer extends Renderer {
	private Configuration templateRepository;
	
	public FreemarkerRenderer(Writer writer, String templateRepositoryPath) {
		super(writer);
		initializeTemplateRepository(templateRepositoryPath);
	}

	private void initializeTemplateRepository(String templateRepositoryPath) {
		this.templateRepository = new Configuration();
		try {
			this.templateRepository.setDirectoryForTemplateLoading(new File(templateRepositoryPath));
		} catch (IOException e) {
			throw new RuntimeException("Template Directory not found");
		}

		this.templateRepository.setObjectWrapper(new BeansWrapper());
	}
	
	/**
	 * @return the templateRepository
	 */
	protected Configuration getTemplateRepository() {
		return templateRepository;
	}

	public void render(String templateName, Attribute ... pairs) throws RendererException {
		Template template;
		
		try {
			template = getTemplateRepository().getTemplate(templateName);
		
			SimpleHash attributes = new SimpleHash();
			
			// HashMap<String, Object> attributes = new HashMap<String, Object>(pairs.length);
			for (Attribute pair : pairs) {
				attributes.put(pair.getKey(), pair.getValue());
			}
		
			template.process(attributes, getWriter());
		} catch (TemplateException e) {
			throw new RendererTemplateException(e);
		} catch (IOException e) {
			throw new RendererIOException(e);
		}
	}

	public void render(String templateName, SimpleHash root) throws RendererException {
		Template template;
		
		try {
			template = getTemplateRepository().getTemplate(templateName);
		
			template.process(root, getWriter());
		} catch (TemplateException e) {
			throw new RendererTemplateException(e);
		} catch (IOException e) {
			throw new RendererIOException(e);
		}
	}

	@Override
	public void render(String templateName) throws RendererException {
		Template template;
		
		try {
			template = getTemplateRepository().getTemplate(templateName);
			template.process(new HashMap<String, String>(), getWriter());
		} catch (TemplateException e) {
			throw new RendererTemplateException(e);
		} catch (IOException e) {
			throw new RendererIOException(e);
		}
	}

	public void render(String templateName, Map<String, Object> attributes) throws RendererException {
		Template template;
		
		try {
			template = getTemplateRepository().getTemplate(templateName);
			template.process(attributes, getWriter());
		} catch (TemplateException e) {
			throw new RendererTemplateException(e);
		} catch (IOException e) {
			throw new RendererIOException(e);
		}	
	}	
}