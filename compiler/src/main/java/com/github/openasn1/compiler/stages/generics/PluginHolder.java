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

import java.util.Properties;

/**
 * @author Clayton Hoss
 * 
 */
public class PluginHolder<ASTm extends ASTModifier> {

	private ASTm astModifier;

	private Properties properties = new Properties();

	private boolean getsASTClone;

	/**
	 * @return the astModifier
	 */
	public ASTm getAstModifier() {
		return this.astModifier;
	}

	/**
	 * @param astModifier
	 *            the astModifier to set
	 */
	void setAstModifier(ASTm astModifier) {
		this.astModifier = astModifier;
	}

	/**
	 * @return the getsASTClone
	 */
	public boolean getsASTClone() {
		return this.getsASTClone;
	}

	/**
	 * @param getsASTClone
	 *            the getsASTClone to set
	 */
	void setGetsASTClone(boolean getsASTClone) {
		this.getsASTClone = getsASTClone;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	void setProperties(Properties properties) {
		this.properties = properties;
	}

}
