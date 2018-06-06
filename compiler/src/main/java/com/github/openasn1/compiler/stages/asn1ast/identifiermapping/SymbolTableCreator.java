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
package com.github.openasn1.compiler.stages.asn1ast.identifiermapping;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.Exceptions.FeatureNotSupportedException;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.TypeAssignment;
import com.github.openasn1.parser.generated.syntaxtree.ValueAssignment;
import com.github.openasn1.parser.generated.syntaxtree.ValueSetTypeAssignment;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


class SymbolTableCreator extends DepthFirstVisitor {
	
	private static Logger LOGGER = Logger.getLogger("SymbolTableCreator");

	private SymbolTable symtab = new SymbolTable();

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.TypeAssignment)
	 */
	@Override
	public void visit(TypeAssignment n) {
		String typeref = n.nodeToken.tokenImage;
		LOGGER.debug("Adding Symbol " + typeref);
		if (getSymtab().contains(typeref)) {
			LOGGER.error("Duplicate Typereference " + typeref);
			throw new CompilationStoppingException(
					"Duplicate Typereference encountered");
		}
		getSymtab().insertIntoTypeSymbolTable(typeref, n);
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition)
	 */
	@Override
	public void visit(ModuleDefinition n) {
		LOGGER.debug("Visiting Module to create SymbolTable "
				+ n.moduleIdentifier.modulereference.nodeToken.tokenImage);
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ValueAssignment)
	 */
	@Override
	public void visit(ValueAssignment n) {
		String identifier = n.valuereference.nodeToken.tokenImage;
		LOGGER.debug("Adding Symbol " + identifier);
		if (getSymtab().contains(identifier)) {
			LOGGER.error("Duplicate identifier " + identifier);
			throw new CompilationStoppingException(
					"Duplicate Identifier encountered");
		}

		getSymtab().insertIntoValueSymbolTable(identifier, n);
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ValueSetTypeAssignment)
	 */
	@Override
	public void visit(ValueSetTypeAssignment n) {
		super.visit(n);
		throw new FeatureNotSupportedException(
				"ValueSetTypeAssignments are not yet supported");
	}

	/**
	 * @return the symtab
	 */
	public SymbolTable getSymtab() {
		return this.symtab;
	}

}