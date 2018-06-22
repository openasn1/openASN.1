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
package com.github.openasn1.compiler.astutils.visitors;

import java.util.ArrayList;
import java.util.List;

import com.github.openasn1.common.TagValue;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.IdentifierMapHelper;
import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 * @author Clayton Hoss
 *
 */
public class GetTagListForTypeVisitor extends DepthFirstVisitor {

	private ASN1ASTNodeInfos infos;

	private List<TagValue> taglist = new ArrayList<TagValue>();

	/**
	 * @param infos is infos
	 */
	public GetTagListForTypeVisitor(ASN1ASTNodeInfos infos) {
		super();
		this.infos = infos;
	}

	@Override
	public void visit(Type n) {
		TagValue tv = (TagValue) getInfos().getNodeInfoMap(n).get("Tag");
		if (tv != null) {
			getTaglist().add(tv);
		}
		if (TypeAttributes.isDefinedType(n)) {
			Type t = IdentifierMapHelper.getTypeFromDefinedTypeRecursive(n, getInfos());
			t.accept(this);
		}
	}

	/**
	 * @return the taglist
	 */
	public List<TagValue> getTaglist() {
		return this.taglist;
	}

	/**
	 * @return the infos
	 */
	private ASN1ASTNodeInfos getInfos() {
		return this.infos;
	}

}
