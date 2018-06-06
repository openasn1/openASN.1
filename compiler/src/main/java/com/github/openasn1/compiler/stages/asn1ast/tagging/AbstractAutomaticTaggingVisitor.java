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

import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.generated.ASN1ParserConstants;
import com.github.openasn1.parser.generated.syntaxtree.BuiltinType;
import com.github.openasn1.parser.generated.syntaxtree.ClassNumber;
import com.github.openasn1.parser.generated.syntaxtree.Node;
import com.github.openasn1.parser.generated.syntaxtree.NodeChoice;
import com.github.openasn1.parser.generated.syntaxtree.NodeListOptional;
import com.github.openasn1.parser.generated.syntaxtree.NodeOptional;
import com.github.openasn1.parser.generated.syntaxtree.NodeSequence;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.NormalConstrainedType;
import com.github.openasn1.parser.generated.syntaxtree.Tag;
import com.github.openasn1.parser.generated.syntaxtree.TaggedType;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 *
 */
public abstract class AbstractAutomaticTaggingVisitor extends DepthFirstVisitor {

  //  private static Logger LOGGER = Logger.getLogger("AbstractAutomaticTaggingVisitor");

    private int tagNumber = 0;

    private boolean tagInExtension;

    // TODO Auto-generated method stub
    // PseudoCode
    // set:tagRoot
    // Super.visit
    // set:tagExtensiom
    // Super.visit

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Type)
     */
    @Override
    public void visit(Type n) {
        if (TypeAttributes.isTaggedType(n)) {
            // TODO implement this after implementing the Components-Of
            // transformation
            throw new CompilationStoppingException("Cannot Tag an already Tagged Type");
        }

        /*
         * TagValue tagvalue = new TagValue(currentTaggingMode, currentTagClass,
         * tagNumber); LOGGER.debug("Adding Tag Info " + tagvalue + " at line " +
         * LogginHelper.getCurrentLine(n)); getNodeinfos().insertInfoIntoNode(n,
         * "Tag", tagvalue);
         */
        NodeToken cnt = new NodeToken(getTagNumberAsString(), ASN1ParserConstants.NUMBER,
                -1, -1, -1, -1);
        ClassNumber cn = new ClassNumber(new NodeChoice(cnt));
        Tag asnTag = new Tag(createLeftBracket(), createEmptyNodeOptional(), cn,
                createRightBracket());
        TaggedType tt = createTaggedType(asnTag, n.nodeChoice.choice, n.nodeChoice.which);

        // which has to be 0, look at the AST: it is the 1st choice for the Type
        // Node
        n.nodeChoice.choice = createNormalConstraintType(tt);
        n.nodeChoice.which = 0;
        n.nodeChoice.choice.setParent(n.nodeChoice);
        incTagNumber();

    }

    private NormalConstrainedType createNormalConstraintType(TaggedType tt) {
        // as you guessed the which depends on the AST, so don't ask
        // The which ain't used in my Code at all and if you want a good hint:
        // don't use it either
        BuiltinType bt = new BuiltinType(new NodeChoice(tt, 17));
        return new NormalConstrainedType(new NodeChoice(bt, 0), new NodeListOptional());
    }

    private TaggedType createTaggedType(Tag asnTag, Node oldType, int which) {
        Type type = new Type(new NodeChoice(oldType, which));
        NodeSequence ns = new NodeSequence(2);
        ns.addNode(asnTag);
        ns.addNode(type);
        return new TaggedType(new NodeChoice(ns, 2));
    }

    private NodeToken createLeftBracket() {
        return new NodeToken("[", ASN1ParserConstants.LEFT_BRACKET_TKN, -1, -1, -1, -1);
    }

    private NodeToken createRightBracket() {
        return new NodeToken("]", ASN1ParserConstants.RIGHT_BRACKET_TKN, -1, -1, -1, -1);
    }

    private NodeOptional createEmptyNodeOptional() {
        return new NodeOptional();
    }

    /**
     * @return the tagNumber
     */
    protected int getTagNumber() {
        return this.tagNumber;
    }

    private String getTagNumberAsString() {
        return new Integer(this.tagNumber).toString();
    }

    private void incTagNumber() {
        this.tagNumber++;
    }

    /**
     * @param tagNumber
     *            the tagNumber to set
     */
    protected void setTagNumber(int tagNumber) {
        this.tagNumber = tagNumber;
    }

    /**
     * @return the tagInExtension
     */
    protected boolean isTagInExtension() {
        return this.tagInExtension;
    }

    /**
     * @param tagInExtension
     *            the tagInExtension to set
     */
    protected void setTagInExtension(boolean tagInExtension) {
        this.tagInExtension = tagInExtension;
    }

}
