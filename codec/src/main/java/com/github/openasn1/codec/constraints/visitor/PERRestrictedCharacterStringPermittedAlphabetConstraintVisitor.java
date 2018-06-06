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
package com.github.openasn1.codec.constraints.visitor;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.openasn1.codec.constraints.Constraint;
import com.github.openasn1.codec.constraints.ConstraintExcept;
import com.github.openasn1.codec.constraints.ConstraintExtension;
import com.github.openasn1.codec.constraints.ConstraintIntersection;
import com.github.openasn1.codec.constraints.ConstraintSerialisation;
import com.github.openasn1.codec.constraints.ConstraintUnion;
import com.github.openasn1.codec.constraints.character.RestrictedCharacterStringInfo;
import com.github.openasn1.codec.constraints.subtype.PermittedAlphabetConstraint;
import com.github.openasn1.codec.constraints.subtype.RestrictedCharacterValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.SingleValueConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint;


/**
 * @author Marc Weyland
 *
 */
public class PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor extends PERVisibleConstraintVisitor {
	private ArrayList<RestrictedCharacterValueRangeConstraint> rangeAlphabetList = new ArrayList<RestrictedCharacterValueRangeConstraint>();
	private RestrictedCharacterStringInfo characterStringInfo;
	private int[] orderedAlphabetIndexArray = null;
	private HashMap<Character, Integer> orderedAlphabet = null;
	
	public PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor(ConstraintVisitorImpl parentConstraintVisitor, RestrictedCharacterStringInfo characterStringInfo) {
		super(parentConstraintVisitor);
		this.characterStringInfo = characterStringInfo;
	}

	private RestrictedCharacterStringInfo getCharacterStringInfo() {
		return this.characterStringInfo;
	}

	/**
	 * @return the orderedAlphabetIndexArray
	 */
	private int[] getOrderedAlphabetIndexArray() {
		return this.orderedAlphabetIndexArray;
	}
	
	/**
	 * @return the orderedAlphabet
	 */
	private HashMap<Character, Integer> getOrderedAlphabet() {
		return this.orderedAlphabet;
	}

	/**
	 * @param orderedAlphabet the orderedAlphabet to set
	 */
	private void setOrderedAlphabet(HashMap<Character, Integer> orderedAlphabet) {
		this.orderedAlphabet = orderedAlphabet;
	}

	/**
	 * @param orderedAlphabetIndexArray the orderedAlphabetIndexArray to set
	 */
	private void setOrderedAlphabetIndexArray(int[] orderedAlphabetIndexArray) {
		this.orderedAlphabetIndexArray = orderedAlphabetIndexArray;
	}

	/**
	 * @return the alphabetList
	 */
	private ArrayList<RestrictedCharacterValueRangeConstraint> getRangeAlphabetList() {
		return this.rangeAlphabetList;
	}
	
	public boolean hasEffectiveAlphabetConstraint() {
		return getRangeAlphabetList().size() > 0;
	}

	/**
	 * Returns the amount of permitted characters
	 * 
	 * NOTE: "[...] what matters is the actual number of characters 
	 * 		 permitted, not the range of characters" (Lar, 6.1, p. 289) 
	 * 
	 * @see "ASN.1 Complete - John Larmouth, section 6.1, page 289"
	 * 
	 * @return the amount of permitted characters
	 */
	public int getAlphabetSize() {
		if (! hasEffectiveAlphabetConstraint()) {
			return getCharacterStringInfo().getAlphabetSize();
		}
		
		int size = 0;

		/**
		 * NOTE: For simplicity we assume non-overlapping alphabet ranges. 
		 */
		for (RestrictedCharacterValueRangeConstraint rangeConstraint : getRangeAlphabetList()) {
			int lowerBoundIndex = getCharacterStringInfo().getCharacterIndex(rangeConstraint.getDefinedLowerBound());
			int upperBoundIndex = getCharacterStringInfo().getCharacterIndex(rangeConstraint.getDefinedUpperBound());
			
			size += upperBoundIndex - lowerBoundIndex + 1;
		}
		
		return size;
	}
	
	public int getUpperAlphabetIndex() {
		if (! hasEffectiveAlphabetConstraint()) {
			return getCharacterStringInfo().getAlphabetSize() - 1;
		}

		int largestIndex = 0;

		/**
		 * NOTE: For simplicity we assume non-overlapping alphabet ranges. 
		 */
		for (RestrictedCharacterValueRangeConstraint rangeConstraint : getRangeAlphabetList()) {
			int index = getCharacterStringInfo().getCharacterIndex(rangeConstraint.getDefinedUpperBound());
			
			if (index > largestIndex) {
				largestIndex = index;
			}
		}
		
		return largestIndex;
	}
	
	
	/**
	 * Canonically orders the permitted alphabet
	 * 
	 * TODO: canonical ordering of alphabets if it has no effective alphabet constraint
	 */
	public void orderCanonically() {
		if (getAlphabetSize() == 0) {
			return;
		}
		
		HashMap<Character, Integer> orderedAlphabet = new HashMap<Character, Integer>();
		int[] orderedAlphabetIndexArray = new int[getAlphabetSize()];
		int index = 0;

		/**
		 * TODO: optimize sorting. we can use the fact that ranges are
		 * 		 sorted within themselfes. 
		 */
		for (RestrictedCharacterValueRangeConstraint rangeConstraint : getRangeAlphabetList()) {
			int lowerBoundIndex = getCharacterStringInfo().getCharacterIndex(rangeConstraint.getDefinedLowerBound());
			int upperBoundIndex = getCharacterStringInfo().getCharacterIndex(rangeConstraint.getDefinedUpperBound());
			
			for (int i = lowerBoundIndex; i <= upperBoundIndex; i++) {
				orderedAlphabetIndexArray[index] = i;
				index++;
			}
		}
		
		java.util.Arrays.sort(orderedAlphabetIndexArray);
		
		for (int i = 0; i < orderedAlphabetIndexArray.length; i++) {
			orderedAlphabet.put((char)orderedAlphabetIndexArray[i], i);
		}
		
		setOrderedAlphabetIndexArray(orderedAlphabetIndexArray);
		setOrderedAlphabet(orderedAlphabet);
	}
	
	/**
	 * Returns the character index from the permitted alphabet.
	 * 
	 * Either this index is the default character index from the
	 * international register or it is the canonical sorted index
	 * of the character in the alphabet.
	 * 
	 * @param character the character whose index should be returned
	 * @return the index of the given character 
	 */
	public int getCharacterIndex(char character) {
		if (getOrderedAlphabet() != null) {
			return getOrderedAlphabet().get(character);
		}
		
		return getCharacterStringInfo().getCharacterIndex(character);
	}
	
	/**
	 * Returns the character of the permitted alphabet.
	 * 
	 * @param index the index whose corresponding index should be returned
	 * @return the character of the given index 
	 */
	public char getCharacterFromIndex(int index) {
		if (getOrderedAlphabet() != null) {			
			return getCharacterStringInfo().getCharacterFromIndex(getOrderedAlphabetIndexArray()[index]);
		}
		
		return getCharacterStringInfo().getCharacterFromIndex(index);
	}	
	
	/**
	 * @see com.github.openasn1.codec.constraints.visitor.ConstraintVisitorImpl#visit(com.github.openasn1.codec.constraints.subtype.SingleValueConstraint)
	 */
	@Override
	public <T> void visit(SingleValueConstraint<T> constraint) {
		super.visit(constraint);
		
		if (! hasAncientVisitorVisited(PermittedAlphabetConstraint.class)) {
			/**
			 * We are not in an PermittedAlphabetConstraint subtree
			 * so we ignore the following constraints. 
			 */
			return;
		}
		
		/**
		 * NOTE: "<2> If Constraint does not include one-character strings, the strings made
		 * 		 of several characters are equivalent to a union of one-character strings
		 * 		 (i.e. separated by "|")" (Dub, 13.6.2, p. 269)
		 * 
		 * @see "Communication of Heterogenous Systems - Dubuisson, section 13.6.2, page 269"
		 */ 
		String singleValue = (String)constraint.getValue();
		
		for (Character character : singleValue.toCharArray()) {
			getRangeAlphabetList().add(new RestrictedCharacterValueRangeConstraint(character, character));
		}
	}

	@Override
	public <T> void visit(ValueRangeConstraint<T> constraint) {
		super.visit(constraint);
		
		if (! hasAncientVisitorVisited(PermittedAlphabetConstraint.class)) {
			/**
			 * We are not in an PermittedAlphabetConstraint subtree
			 * so we ignore the following constraints. 
			 */
			return;
		}

		if (! (constraint instanceof RestrictedCharacterValueRangeConstraint)) {
			return;
		}
		
		RestrictedCharacterValueRangeConstraint valueRangeConstraint = (RestrictedCharacterValueRangeConstraint)constraint; 

		getRangeAlphabetList().add(valueRangeConstraint);
	}

	/**
	 * NOTE: The extensible permitted-alphabet constraint is not PER-visible.
	 * 
	 * @see "X.691-0207 9.3.18 NOTE 2"
	 * @see "X.691-0207 B.2.1.7"
	 * @see com.github.openasn1.codec.constraints.visitor.ConstraintVisitorImpl#visit(com.github.openasn1.codec.constraints.subtype.PermittedAlphabetConstraint)
	 */
	@Override
	public void visit(PermittedAlphabetConstraint constraint) {
		super.visit(constraint);
		
		if (hasAncientVisitorVisited(PermittedAlphabetConstraint.class)) {
			/**
			 * This constraint is a subconstraint of a PermittedAlphabetConstraint.
			 * We don't allow this
			 */
			return;
		}
		
		PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor visitor = new PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor(this, getCharacterStringInfo());
		
		constraint.getConstraint().accept(visitor);
		
		getRangeAlphabetList().addAll(visitor.getRangeAlphabetList());
	}

	@Override
	public void visit(ConstraintUnion constraint) {
		super.visit(constraint);
		
		for (Constraint subConstraint : constraint.getList()) {
			PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor constraintVisitor = new PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor(this, getCharacterStringInfo());
			
			subConstraint.accept(constraintVisitor);
			
			/**
			 * NOTE: For simplicity we assume non-overlapping alphabet ranges. 
			 */
			if (constraintVisitor.hasEffectiveAlphabetConstraint()) {
				getRangeAlphabetList().addAll(constraintVisitor.getRangeAlphabetList());
			} else {
				/**
				 * TODO: find out what happens to unions of PermittedAlphabetConstraints with other
				 * 		 non-PermittedAlphabetConstraints.
				 * 		 Annex B3 - Example 9 shows that it gives the full character alphabet.
				 * 		 Find some normative statement to this question.
				 * 		 We also have to take care of PER-visible constraints here.
				 * 
				 * @see "X.691-0207 B.3 Example 9"
				 */
				
				getRangeAlphabetList().clear();
				
				return;
			}
		}
	}

	@Override
	public void visit(ConstraintIntersection constraint) {
		super.visit(constraint);
		
		boolean comparable = false;

		ArrayList<RestrictedCharacterValueRangeConstraint> alphabetList = new ArrayList<RestrictedCharacterValueRangeConstraint>();

		for (Constraint subConstraint : constraint.getList()) {
			PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor constraintVisitor = new PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor(this, getCharacterStringInfo());
			
			subConstraint.accept(constraintVisitor);

			if (constraintVisitor.hasEffectiveAlphabetConstraint()) {
				if (comparable) {
					/** 
					 * An intersection of the alphabets is needed
					 * 
					 * NOTE 1: only if the alphabet is a superset of the
					 * 	       other alphabets then this constraint is PER visible.
					 * 
					 * NOTE 2: the intersection with non-PER-visible parts are ignored.
					 * 
					 * @see "X.691-0207 9.3.19"
					 */
					
					throw new RuntimeException("PermittedAlphabet intersection is not yet implemented");
				} else {
					/**
					 * NOTE: For simplicity we assume non-overlapping alphabet ranges. 
					 */
					alphabetList.addAll(constraintVisitor.getRangeAlphabetList());
	
					comparable = true;
				}
			}
		}
		
		getRangeAlphabetList().addAll(alphabetList);
	}
	
	/**
	 * Serial application of PermittedAlphabetConstraints
	 * 
	 * NOTE: "If a complete constraint in serial application of constraints 
	 * 		 is not PER-visible, then for the purposes of PER encodings, 
	 *    	 that constraint is simply completely ignored."
	 * 
	 * @see "X.691-0207 B.2.2.2"
	 * @see com.github.openasn1.codec.constraints.visitor.SimpleConstraintVisitor#visit(com.github.openasn1.codec.constraints.ConstraintList)
	 */
	@Override
	public void visit(ConstraintSerialisation constraint) {
		super.visit(constraint);
		
		boolean comparable = false;

		for (Constraint subConstraint : constraint.getList()) {
			PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor constraintVisitor = new PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor(this, getCharacterStringInfo());
			
			subConstraint.accept(constraintVisitor);

			if (constraintVisitor.hasEffectiveAlphabetConstraint()) {
				if (comparable) {
					/** 
					 * An intersection of the alphabets is needed
					 * 
					 * NOTE 1: only if the alphabet is a superset of the
					 * 	       other alphabets then this constraint is PER visible.
					 * 
					 * NOTE 2: the intersection with non-PER-visible parts are ignored.
					 * 
					 * @see "X.691-0207 9.3.19"
					 */
					
					throw new RuntimeException("PermittedAlphabet intersection is not yet implemented");
				} else {
					/**
					 * NOTE 1: For simplicity we assume non-overlapping alphabet ranges.
					 * 
					 * NOTE 2: We work on the range alphabet list of this visitor directly   
					 */
					getRangeAlphabetList().addAll(constraintVisitor.getRangeAlphabetList());
	
					comparable = true;
				}
			}
		}
	}

	/**
	 * NOTE: "If a constraint has an EXCEPT clause, the EXCEPT and the following 
	 * 		 value set is completely ignored, whether the value set following 
	 * 		 the EXCEPT is PER-visible or not."
	 * 
	 * @see "X.691-0207 9.3.19"
	 * @see com.github.openasn1.codec.constraints.visitor.ConstraintVisitorImpl#visit(com.github.openasn1.codec.constraints.ConstraintExcept)
	 */
	@Override
	public void visit(ConstraintExcept constraint) {
		super.visit(constraint);
		
		PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor operandAVisitor = new PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor(this, getCharacterStringInfo()); 
		
		/**
		 * NOTE: since B is the EXCEPT constraint operand it will be 
		 * 		 completely ignored. (see note above).
		 */
		constraint.getOperandA().accept(operandAVisitor);

		getRangeAlphabetList().addAll(operandAVisitor.getRangeAlphabetList());
	}

	/**
	 * @see com.github.openasn1.codec.constraints.visitor.ConstraintVisitorImpl#visit(com.github.openasn1.codec.constraints.ConstraintExtension)
	 */
	@Override
	public void visit(ConstraintExtension constraint) {
		super.visit(constraint);
		
		if (! hasAncientVisitorVisited(ConstraintSerialisation.class)) {
			/**
			 * If we are not in a subtree of a serialisation we skip the whole
			 * processing since extensible permitted alphabet constraints
			 * are irgenored.
			 */
			return;
		}
		
		/**
		 * Otherwise we prune extension additions and take only extension roots
		 */
		constraint.getExtensionRootConstraint().accept(this);
	}
}