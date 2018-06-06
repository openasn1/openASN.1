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

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.LogginHelper;
import com.github.openasn1.parser.generated.syntaxtree.ChoiceType;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.SequenceType;
import com.github.openasn1.parser.generated.syntaxtree.SetType;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 *
 * This class annotates the AST with an boolean attribute called
 * "ApplyAutomaticTags" which says if the Sequence, Set or Choice should have
 * the automatic tag transformation applied.
 *
 * @author Clayton Hoss
 *
 */
public class AutomaticTaggingDecidingVisitor extends DepthFirstVisitor {

    private static Logger LOGGER = Logger.getLogger("AutomaticTaggingDecidingVisitor");

    private ASN1ASTNodeInfos nodeInfos;

    public AutomaticTaggingDecidingVisitor(ASN1ASTNodeInfos infos) {
        this.nodeInfos = infos;
    }

    /**
     * @return the nodeInfos
     */
    private ASN1ASTNodeInfos getNodeInfos() {
        return this.nodeInfos;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition)
     */
    @Override
    public void visit(ModuleDefinition n) {
        LOGGER.debug("Visiting Module to Decide Automatic Tags "
                + n.moduleIdentifier.modulereference.nodeToken.tokenImage);
        super.visit(n);
    }
    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ChoiceType)
     */
    @Override
    public void visit(ChoiceType n) {
        ChoiceAutomaticTaggingDecidingVisitor vis = new ChoiceAutomaticTaggingDecidingVisitor();
        n.accept(vis);
        Boolean autoTag = vis.useAutomaticTagging();
        LOGGER.debug("Applying automatic tagging decision: " + autoTag + " for choice at line "
                + LogginHelper.getCurrentLine(n));
        getNodeInfos().insertInfoIntoNode(n, "ApplyAutomaticTags", autoTag);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceType)
     */
    @Override
    public void visit(SequenceType n) {
        SequenceAutomaticTaggingDecidingVisitor vis = new SequenceAutomaticTaggingDecidingVisitor();
        n.accept(vis);
        Boolean autoTag = vis.useAutomaticTagging();
        LOGGER.debug("Applying automatic tagging " + autoTag + " for sequence at line "
                + LogginHelper.getCurrentLine(n));
        getNodeInfos().insertInfoIntoNode(n, "ApplyAutomaticTags", autoTag);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SetType)
     */
    @Override
    public void visit(SetType n) {
        SetAutomaticTaggingDecidingVisitor vis = new SetAutomaticTaggingDecidingVisitor();
        n.accept(vis);
        Boolean autoTag = vis.useAutomaticTagging();
        LOGGER.debug("Applying automatic tagging " + autoTag + " for set at line "
                + LogginHelper.getCurrentLine(n));
        getNodeInfos().insertInfoIntoNode(n, "ApplyAutomaticTags", autoTag);
        super.visit(n);
    }

}
