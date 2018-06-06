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
package com.github.openasn1.compiler.stages.omast.naming;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.omast.ASN1ImportListFromModule;
import com.github.openasn1.compiler.omast.ASN1Module;
import com.github.openasn1.compiler.omast.ASN1TypeAssignment;
import com.github.openasn1.compiler.omast.ASN1TypeAssignmentList;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;


/**
 * @author Clayton Hoss
 *
 */
public class OMNameAdjustingVisitor extends AbstractOMVisitor {

	private static Logger LOGGER = Logger.getLogger("OMNameAdjustingVisitor");

	private String getAdjustedName(String name) {
		if (name == null) {
			return null;
		}
		if (NamingUtils.isJavaKeyword(name)) {
			return name + "_";
		}
		char[] nameChars = name.toCharArray();

		if (!Character.isJavaIdentifierStart(nameChars[0])) {
			nameChars[0] = '_';
		}

		for (int i = 1; i < nameChars.length; i++) {
			if (!Character.isJavaIdentifierPart(nameChars[i])) {
				nameChars[i] = '_';
			}
		}

		String newName = new String(nameChars);

		if (!name.equals(newName)) {
			LOGGER.debug("Adjusting Name " + name + " to " + newName);
		}
		return newName;
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignment)
	 */
	@Override
	public void visit(ASN1TypeAssignment type) {
		type.setName(getAdjustedName(type.getName()));
		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ReferencedASN1Type)
	 */
	@Override
	public void visit(ReferencedASN1Type type) {
		type.setName(getAdjustedName(type.getName()));
		type.setReferencedName(getAdjustedName(type.getReferencedName()));
		super.visit(type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.AggregatedASN1Type)
	 */
	@Override
	public void visit(AggregatedASN1Type type) {
		type.setName(getAdjustedName(type.getName()));
		super.visit(type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		type.setName(getAdjustedName(type.getName()));
		super.visit(type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.SimpleASN1Type)
	 */
	@Override
	public void visit(SimpleASN1Type type) {
		type.setName(getAdjustedName(type.getName()));
		super.visit(type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1ImportListFromModule)
	 */
	@Override
	public void visit(ASN1ImportListFromModule type) {
		type.setModuleName(getAdjustedName(type.getModuleName()));
		List<String> newImportNames = new ArrayList<String>();
		for (String s : type.getImportNames()) {
			newImportNames.add(getAdjustedName(s));
		}
		type.setImportNames(newImportNames);
		super.visit(type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1Module)
	 */
	@Override
	public void visit(ASN1Module type) {
		type.setName(getAdjustedName(type.getName()));
		super.visit(type);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignmentList)
	 */
	@Override
	public void visit(ASN1TypeAssignmentList type) {
		type.setName(getAdjustedName(type.getName()));
		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.EnumASN1Type)
	 */
	@Override
	public void visit(EnumASN1Type type) {
		type.setName(getAdjustedName(type.getName()));
		Map<String, Integer> adjustedEnumKeySet = new LinkedHashMap<String,Integer>();
		for(String key : type.getEnums().keySet()) {
			String newKey = getAdjustedName(key);
			adjustedEnumKeySet.put(newKey, type.getEnums().get(key));
		}
		type.setEnums(adjustedEnumKeySet);
		super.visit(type);
	}

}
