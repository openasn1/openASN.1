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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.github.openasn1.compiler.interfaces.DeepCopyable;


/**
 * 
 * Implements a Plugable Stage, which gets the Plugins Loaded Via its Builder
 * ASTs ist an ASTStorage Object, which has to support a Deep Copy Operation and
 * implement the needed Interface
 * 
 * @author Clayton Hoss
 * 
 */

public class PlugableStage<ASTs extends DeepCopyable, ASTm extends ASTModifier<ASTs>> {

	private List<PluginHolder<ASTm>> plugins = new ArrayList<PluginHolder<ASTm>>();

	private Properties compilerProperties;

	public PlugableStage(Properties compilerProp) {
		this.compilerProperties = compilerProp;
	}

	public boolean initPlugins() {
		boolean success = true;
		for (PluginHolder<ASTm> plugin : getPlugins()) {
			Properties prop = (Properties) plugin.getProperties().clone();
			prop.putAll(getCompilerProperties());
			success = plugin.getAstModifier().init(prop) & success;
		}
		return success;
	}

	public ASTs processStage(ASTs storage) {
		for (PluginHolder<ASTm> plugin : getPlugins()) {
			if (plugin.getsASTClone()) {
				@SuppressWarnings("unchecked")
				ASTs storageclone = (ASTs) storage.deepCopy();
				plugin.getAstModifier().modifyAST(storageclone);
			} else {
				storage = plugin.getAstModifier().modifyAST(storage);
			}
		}
		return storage;
	}

	List<PluginHolder<ASTm>> getPlugins() {
		return this.plugins;
	}

	/**
	 * @return the compilerProperties
	 */
	private Properties getCompilerProperties() {
		return this.compilerProperties;
	}
}
