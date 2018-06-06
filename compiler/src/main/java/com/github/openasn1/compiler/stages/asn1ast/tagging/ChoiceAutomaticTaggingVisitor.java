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
package com.github.openasn1.compiler.stages.asn1ast.tagging;

import com.github.openasn1.parser.generated.syntaxtree.ChoiceType;
import com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditionAlternatives;
import com.github.openasn1.parser.generated.syntaxtree.RootAlternativeTypeList;

/**
 * @author Clayton Hoss
 *
 */
public class ChoiceAutomaticTaggingVisitor extends AbstractAutomaticTaggingVisitor {

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditionList)
     */
    @Override
    public void visit(ExtensionAdditionAlternatives n) {
        if (isTagInExtension()) {
            super.visit(n);
        }
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.RootComponentTypeList)
     */
    @Override
    public void visit(RootAlternativeTypeList n) {
        if (!isTagInExtension()) {
            super.visit(n);
        }
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceType)
     */
    @Override
    public void visit(ChoiceType n) {
        setTagInExtension(false);
        super.visit(n);
        setTagInExtension(true);
        super.visit(n);
    }

}
