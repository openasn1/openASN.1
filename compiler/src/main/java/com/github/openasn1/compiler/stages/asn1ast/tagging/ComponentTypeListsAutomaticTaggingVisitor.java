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

import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditionList;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 *
 */
public class ComponentTypeListsAutomaticTaggingVisitor extends DepthFirstVisitor {
    private boolean useAutomaticTaggingInRoot = true;

    private boolean useAutomatictaggingInExtension = true;

    private boolean isInExtension;

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditionList)
     */
    @Override
    public void visit(ExtensionAdditionList n) {
        setInExtension(true);
        super.visit(n);
        setInExtension(false);
    }



    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Type)
     */
    @Override
    public void visit(Type n) {
        if (TypeAttributes.isTaggedType(n)) {
            if (isInExtension()) {
                setUseAutomatictaggingInExtension(false);
            } else {
                setUseAutomaticTaggingInRoot(false);
            }
        }
    }

    /**
     * @return the isInExtension
     */
    private boolean isInExtension() {
        return this.isInExtension;
    }

    /**
     * @param isInExtension
     *            the isInExtension to set
     */
    private void setInExtension(boolean isInExtension) {
        this.isInExtension = isInExtension;
    }

    public boolean useAutomaticTagging() {
        return (getUseAutomaticTaggingInRoot() && getUseAutomatictaggingInExtension());
    }

    /**
     * @return the useAutomatictaggingInExtenstion
     */
    protected boolean getUseAutomatictaggingInExtension() {
        return this.useAutomatictaggingInExtension;
    }

    /**
     * @param useAutomatictaggingInExtenstion
     *            the useAutomatictaggingInExtenstion to set
     */
    private void setUseAutomatictaggingInExtension(boolean useAutomatictaggingInExtenstion) {
        this.useAutomatictaggingInExtension = useAutomatictaggingInExtenstion;
    }

    /**
     * @return the useAutomaticTaggingInRoot
     */
    protected boolean getUseAutomaticTaggingInRoot() {
        return this.useAutomaticTaggingInRoot;
    }

    /**
     * @param useAutomaticTaggingInRoot
     *            the useAutomaticTaggingInRoot to set
     */
    private void setUseAutomaticTaggingInRoot(boolean useAutomaticTaggingInRoot) {
        this.useAutomaticTaggingInRoot = useAutomaticTaggingInRoot;
    }

}

