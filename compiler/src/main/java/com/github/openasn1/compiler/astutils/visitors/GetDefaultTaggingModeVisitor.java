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

import com.github.openasn1.common.TaggingModeEnum;
import com.github.openasn1.parser.attributes.TagDefaultAttributes;
import com.github.openasn1.parser.generated.syntaxtree.TagDefault;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * This class returns the default tagging mode for a given ASN.1 File. a valid
 * tagging mode is either IMPLICIT or EXPLICIT. If you want to know which mode
 * was explicitly defined in the ASN.1 File you can use the method
 * getDefinedTaggingMode()
 *
 * @author Clayton Hoss
 *
 */
public class GetDefaultTaggingModeVisitor extends DepthFirstVisitor {

    private TaggingModeEnum defaultTaggingMode = TaggingModeEnum.EXPLICIT;

    private TaggingModeEnum definedTaggingMode = TaggingModeEnum.EXPLICIT;

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.TagDefault)
     */
    @Override
    public void visit(TagDefault n) {
        if (TagDefaultAttributes.isAutomaticTaggingMode(n)) {
            setDefaultTaggingMode(TaggingModeEnum.IMPLICIT);
            setDefinedTaggingMode(TaggingModeEnum.AUTOMATIC);
        }
        if (TagDefaultAttributes.isImplicitTaggingMode(n)) {
            setDefaultTaggingMode(TaggingModeEnum.IMPLICIT);
            setDefinedTaggingMode(TaggingModeEnum.IMPLICIT);
        }
        if (TagDefaultAttributes.isExplicitTaggingMode(n)) {
            setDefaultTaggingMode(TaggingModeEnum.EXPLICIT);
            setDefinedTaggingMode(TaggingModeEnum.EXPLICIT);
        }
        super.visit(n);
    }

    /**
     * @return the defaultTaggingMode
     */
    public TaggingModeEnum getDefaultTaggingMode() {
        return this.defaultTaggingMode;
    }

    /**
     * @param defaultTaggingMode
     *            the defaultTaggingMode to set
     */
    private void setDefaultTaggingMode(TaggingModeEnum defaultTaggingMode) {
        this.defaultTaggingMode = defaultTaggingMode;
    }

    /**
     * @return the definedTaggingMode
     */
    public TaggingModeEnum getDefinedTaggingMode() {
        return this.definedTaggingMode;
    }

    /**
     * @param definedTaggingMode
     *            the definedTaggingMode to set
     */
    private void setDefinedTaggingMode(TaggingModeEnum definedTaggingMode) {
        this.definedTaggingMode = definedTaggingMode;
    }

}
