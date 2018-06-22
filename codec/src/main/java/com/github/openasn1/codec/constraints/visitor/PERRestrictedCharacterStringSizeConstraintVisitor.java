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

import com.github.openasn1.codec.constraints.Constraint;
import com.github.openasn1.codec.constraints.ConstraintExcept;
import com.github.openasn1.codec.constraints.ConstraintExtension;
import com.github.openasn1.codec.constraints.ConstraintIntersection;
import com.github.openasn1.codec.constraints.ConstraintSerialisation;
import com.github.openasn1.codec.constraints.ConstraintUnion;
import com.github.openasn1.codec.constraints.subtype.IntegerValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.SingleValueConstraint;
import com.github.openasn1.codec.constraints.subtype.SizeConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint.Extrema;

/**
 * @author Marc Weyland
 *
 */
public class PERRestrictedCharacterStringSizeConstraintVisitor extends PERVisibleConstraintVisitor {
	private IntegerValueRangeConstraint effectiveExtensionRootSizeConstraint;
	private IntegerValueRangeConstraint effectiveExtensionAdditionSizeConstraint;
	
	public PERRestrictedCharacterStringSizeConstraintVisitor(ConstraintVisitorImpl parentConstraintVisitor) {
		super(parentConstraintVisitor);
	}
	
	public boolean hasConstrainedMinimum() {
		if (! hasEffectiveSizeConstraint()) {
			return false;
		}
	
		if (hasEffectiveExtensionAdditionSizeConstraint()) {
			return hasConstrainedRootMinimum() && hasConstrainedAdditionMinimum();
		}
		
		return hasConstrainedRootMinimum();
	}

	public boolean hasConstrainedRootMinimum() {
		if (! hasEffectiveSizeConstraint()) {
			return false;
		}
		
		if (getEffectiveExtensionRootSizeConstraint().hasDefinedLowerBound()) {
			return true;
		}

		if (! hasParentConstraintVisitor()) {
			return false; 
		}

		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			return false;
		}
		
		return parentVisitor.hasConstrainedRootMinimum();
	}
	
	public boolean hasConstrainedAdditionMinimum() {
		if (! hasEffectiveExtensionAdditionSizeConstraint()) {
			return false;
		}
		
		if (getEffectiveExtensionAdditionSizeConstraint().hasDefinedLowerBound()) {
			return true;
		}

		if (! hasParentConstraintVisitor()) {
			return false; 
		}

		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			return false;
		}
		
		return parentVisitor.hasConstrainedAdditionMinimum();
	}	
	
	public int getConstrainedMinimum() {
		boolean hasConstrainedRootMinimum = hasConstrainedRootMinimum();
		
		if (hasEffectiveExtensionAdditionSizeConstraint()) {
			boolean hasConstrainedAdditionMinimum = hasConstrainedAdditionMinimum();

			if (hasConstrainedRootMinimum && hasConstrainedAdditionMinimum) {
				return Math.min(getConstrainedRootMinimum(), getConstrainedAdditionMinimum());
			}

			throw new RuntimeException("Constraint has no defined minimum");
		}
		
		if (hasConstrainedRootMinimum) {
			return getConstrainedRootMinimum();
		}
		
		throw new RuntimeException("Constraint has no defined minimum");
	}

	public int getConstrainedRootMinimum() {
		if (! hasEffectiveSizeConstraint()) {
			throw new RuntimeException("Constraint has no defined root minimum");
		}

		if (getEffectiveExtensionRootSizeConstraint().hasDefinedLowerBound()) {
			return getEffectiveExtensionRootSizeConstraint().getDefinedLowerBound();
		}

		if (! hasParentConstraintVisitor()) {
			throw new RuntimeException("Constraint has no defined root minimum");
		}
		
		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			throw new RuntimeException("Constraint has no defined root minimum");
		}
		
		return parentVisitor.getConstrainedRootMinimum();
	}

	public int getConstrainedAdditionMinimum() {
		if (! hasEffectiveExtensionAdditionSizeConstraint()) {
			throw new RuntimeException("Constraint has no defined addition minimum");
		}

		if (getEffectiveExtensionRootSizeConstraint().hasDefinedLowerBound()) {
			return getEffectiveExtensionAdditionSizeConstraint().getDefinedLowerBound();
		}

		if (! hasParentConstraintVisitor()) {
			throw new RuntimeException("Constraint has no defined addition minimum");
		}
		
		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			throw new RuntimeException("Constraint has no defined addition minimum");
		}
		
		return parentVisitor.getConstrainedAdditionMinimum();
	}

	public boolean hasConstrainedMaximum() {
		if (! hasEffectiveSizeConstraint()) {
			return false;
		}
	
		if (hasEffectiveExtensionAdditionSizeConstraint()) {
			return hasConstrainedRootMaximum() && hasConstrainedAdditionMaximum();
		}
		
		return hasConstrainedRootMaximum();
	}

	public boolean hasConstrainedRootMaximum() {
		if (! hasEffectiveSizeConstraint()) {
			return false;
		}
		
		if (getEffectiveExtensionRootSizeConstraint().hasDefinedUpperBound()) {
			return true;
		}

		if (! hasParentConstraintVisitor()) {
			return false; 
		}

		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			return false;
		}
		
		return parentVisitor.hasConstrainedRootMaximum();
	}
	
	public boolean hasConstrainedAdditionMaximum() {
		if (! hasEffectiveExtensionAdditionSizeConstraint()) {
			return false;
		}
		
		if (getEffectiveExtensionAdditionSizeConstraint().hasDefinedUpperBound()) {
			return true;
		}

		if (! hasParentConstraintVisitor()) {
			return false; 
		}

		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			return false;
		}
		
		return parentVisitor.hasConstrainedAdditionMaximum();
	}	
	
	public int getConstrainedMaximum() {
		boolean hasConstrainedRootMaximum = hasConstrainedRootMaximum();
		
		if (hasEffectiveExtensionAdditionSizeConstraint()) {
			boolean hasConstrainedAdditionMaximum = hasConstrainedAdditionMaximum();

			if (hasConstrainedRootMaximum && hasConstrainedAdditionMaximum) {
				return Math.max(getConstrainedRootMaximum(), getConstrainedAdditionMaximum());
			}

			throw new RuntimeException("Constraint has no defined maximum");
		}
		
		if (hasConstrainedRootMaximum) {
			return getConstrainedRootMaximum();
		}
		
		throw new RuntimeException("Constraint has no defined maximum");
	}

	public int getConstrainedRootMaximum() {
		if (! hasEffectiveSizeConstraint()) {
			throw new RuntimeException("Constraint has no defined root maximum");
		}

		if (getEffectiveExtensionRootSizeConstraint().hasDefinedUpperBound()) {
			return getEffectiveExtensionRootSizeConstraint().getDefinedUpperBound();
		}

		if (! hasParentConstraintVisitor()) {
			throw new RuntimeException("Constraint has no defined root maximum");
		}
		
		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			throw new RuntimeException("Constraint has no defined root maximum");
		}
		
		return parentVisitor.getConstrainedRootMaximum();
	}

	public int getConstrainedAdditionMaximum() {
		if (! hasEffectiveExtensionAdditionSizeConstraint()) {
			throw new RuntimeException("Constraint has no defined addition maximum");
		}

		if (getEffectiveExtensionRootSizeConstraint().hasDefinedUpperBound()) {
			return getEffectiveExtensionAdditionSizeConstraint().getDefinedUpperBound();
		}

		if (! hasParentConstraintVisitor()) {
			throw new RuntimeException("Constraint has no defined addition maximum");
		}
		
		PERRestrictedCharacterStringSizeConstraintVisitor parentVisitor = (PERRestrictedCharacterStringSizeConstraintVisitor)getAncientVisitorWhoVisited(PERRestrictedCharacterStringSizeConstraintVisitor.class, ConstraintSerialisation.class);
		
		if (parentVisitor == null) {
			throw new RuntimeException("Constraint has no defined addition maximum");
		}
		
		return parentVisitor.getConstrainedAdditionMaximum();
	}


	/**
	 * @return the effectiveSizeConstraint
	 */
	private IntegerValueRangeConstraint getEffectiveExtensionRootSizeConstraint() {
		return this.effectiveExtensionRootSizeConstraint;
	}
	
	/**
	 * @return the effectiveSizeConstraint
	 */
	private IntegerValueRangeConstraint getEffectiveExtensionAdditionSizeConstraint() {
		return this.effectiveExtensionAdditionSizeConstraint;
	}
	
	/**
	 * @return the effectiveSizeConstraint
	 */
	private boolean hasEffectiveSizeConstraint() {
		return getEffectiveExtensionRootSizeConstraint() != null;
	}

	private boolean hasEffectiveExtensionAdditionSizeConstraint() {
		return (getEffectiveExtensionAdditionSizeConstraint() != null) && (! getEffectiveExtensionAdditionSizeConstraint().isEmpty());
	}

	/**
	 * @param effectiveSizeConstraint the effectiveSizeConstraint to set
	 */
	private void setEffectiveExtensionRootSizeConstraint(IntegerValueRangeConstraint effectiveExtensionRootSizeConstraint) {
		this.effectiveExtensionRootSizeConstraint = effectiveExtensionRootSizeConstraint;
	}

	private void setEffectiveExtensionAdditionSizeConstraint(IntegerValueRangeConstraint effectiveExtensionAdditionSizeConstraint) {
		this.effectiveExtensionAdditionSizeConstraint = effectiveExtensionAdditionSizeConstraint;
	}

	/**
	 * Returns the effective minimum size of the constraints.
	 * 
	 * @return the effective minimum size
	 */
	public int getMinimumSize() {
		if (hasConstrainedMinimum()) {
			return getConstrainedMinimum();
		}
		return Integer.MIN_VALUE;
	}
	
	private int getMinimumRootSize() {
		if (hasConstrainedRootMinimum()) {
			return getConstrainedRootMinimum();
		}
		
		return Integer.MIN_VALUE;
	}
	
	private int getMinimumAdditionSize() {
		if (hasConstrainedAdditionMinimum()) {
			return getConstrainedAdditionMinimum();
		}
		
		return Integer.MIN_VALUE;
	}	
	
	/**
	 * Returns the effective maximum size of the constraints.
	 * 
	 * @return the effective maximum size
	 */
	public int getMaximumSize() {
		if (hasConstrainedMaximum()) {
			return getConstrainedMaximum();
		}
		
		return Integer.MAX_VALUE;
	}
	
	private int getMaximumRootSize() {
		if (hasConstrainedRootMaximum()) {
			return getConstrainedRootMaximum();
		}
		
		return Integer.MAX_VALUE;
	}

	private int getMaximumAdditionSize() {
		if (hasConstrainedAdditionMaximum()) {
			return getConstrainedAdditionMaximum();
		}
		
		return Integer.MAX_VALUE;
	}

	public boolean isFixedSize() {
		return getMinimumSize() == getMaximumSize();
	}
	
	@Override
	public void visit(SizeConstraint constraint) {
		super.visit(constraint);

		if (hasAncientVisitorVisited(SizeConstraint.class)) {
			/**
			 * This constraint is a subconstraint of a SizeConstraint.
			 * We don't allow this
			 */
			return;
		}

		PERRestrictedCharacterStringSizeConstraintVisitor visitor = new PERRestrictedCharacterStringSizeConstraintVisitor(this);
		
		constraint.getConstraint().accept(visitor);
		
		setEffectiveExtensionRootSizeConstraint(visitor.getEffectiveExtensionRootSizeConstraint());
		setEffectiveExtensionAdditionSizeConstraint(visitor.getEffectiveExtensionAdditionSizeConstraint());
		setExtensible(visitor.isExtensible());
	}

    @Override
	public <T> void visit(SingleValueConstraint<T> constraint) {
		super.visit(constraint);
		
		if (! hasAncientVisitorVisited(SizeConstraint.class)) {
			/**
			 * We are not in an SizeConstraint subtree
			 * so we ignore the following constraints. 
			 */
			return;
		}
		
		setEffectiveExtensionRootSizeConstraint(new IntegerValueRangeConstraint((Integer)constraint.getValue(), (Integer)constraint.getValue()));
	}

	@Override
	public <T> void visit(ValueRangeConstraint<T> constraint) {
		super.visit(constraint);
		
		if (! hasAncientVisitorVisited(SizeConstraint.class)) {
			/**
			 * We are not in an SizeConstraint subtree
			 * so we ignore the following constraints. 
			 */
			return;
		}

		if (! (constraint instanceof IntegerValueRangeConstraint)) {
			return;
		}

		IntegerValueRangeConstraint valueRangeConstraint = (IntegerValueRangeConstraint)constraint; 

		setEffectiveExtensionRootSizeConstraint(valueRangeConstraint);
	}
	
	private IntegerValueRangeConstraint performUnion(IntegerValueRangeConstraint rangeA, IntegerValueRangeConstraint rangeB) {
		Integer unionMinimum = null;
		Integer unionMaximum = null;
		
		/**
		 * If both ranges are empty we also return an empty range
		 */
		if (rangeA.isEmpty() && rangeB.isEmpty()) {
			return new IntegerValueRangeConstraint();
		}
		
		if (rangeA.hasDefinedLowerBound() && rangeB.hasDefinedLowerBound()) {
			unionMinimum = Math.min(rangeA.getDefinedLowerBound(), rangeB.getDefinedLowerBound());
		}
		if (rangeA.hasDefinedUpperBound() && rangeB.hasDefinedUpperBound()) {
			unionMaximum = Math.max(rangeA.getDefinedUpperBound(), rangeB.getDefinedUpperBound());
		}

		if ((unionMinimum == null) && (unionMaximum == null)) {
			return new IntegerValueRangeConstraint(Extrema.MINIMUM, Extrema.MAXIMUM, false, false);
		} else if ((unionMinimum == null) && (unionMaximum != null)) {
			return new IntegerValueRangeConstraint(Extrema.MINIMUM, unionMaximum, false, false);
		} else if ((unionMinimum != null) && (unionMaximum == null)) {
			return new IntegerValueRangeConstraint(unionMinimum, Extrema.MAXIMUM, false, false);
		}
		
		return new IntegerValueRangeConstraint(unionMinimum, unionMaximum, false, false);	
	}

	private IntegerValueRangeConstraint performIntersection(IntegerValueRangeConstraint rangeA, IntegerValueRangeConstraint rangeB) {
		Integer intersectionMinimum = null;
		Integer intersectionMaximum = null;
		
		/**
		 * If any range is empty we return an empty range
		 */
		if (rangeA.isEmpty() || rangeB.isEmpty()) {
			return new IntegerValueRangeConstraint();
		}
		
		if (rangeA.hasDefinedLowerBound() && rangeB.hasDefinedLowerBound()) {
			intersectionMinimum = Math.max(rangeA.getDefinedLowerBound(), rangeB.getDefinedLowerBound());
		} else if ((! rangeA.hasDefinedLowerBound()) && rangeB.hasDefinedLowerBound()) {
			intersectionMinimum = rangeB.getDefinedLowerBound();
		} else if (rangeA.hasDefinedLowerBound() && (! rangeB.hasDefinedLowerBound())) {
			intersectionMinimum = rangeA.getDefinedLowerBound();
		}
		
		if (rangeA.hasDefinedUpperBound() && rangeB.hasDefinedUpperBound()) {
			intersectionMaximum = Math.min(rangeA.getDefinedUpperBound(), rangeB.getDefinedUpperBound());
		} else if ((! rangeA.hasDefinedUpperBound()) && rangeB.hasDefinedUpperBound()) {
			intersectionMaximum = rangeB.getDefinedUpperBound();
		} else if (rangeA.hasDefinedUpperBound() && (! rangeB.hasDefinedUpperBound())) {
			intersectionMaximum = rangeA.getDefinedUpperBound();
		}

		if ((intersectionMinimum == null) && (intersectionMaximum == null)) {
			return new IntegerValueRangeConstraint(Extrema.MINIMUM, Extrema.MAXIMUM, false, false);
		} else if ((intersectionMinimum == null) && (intersectionMaximum != null)) {
			return new IntegerValueRangeConstraint(Extrema.MINIMUM, intersectionMaximum, false, false);
		} else if ((intersectionMinimum != null) && (intersectionMaximum == null)) {
			return new IntegerValueRangeConstraint(intersectionMinimum, Extrema.MAXIMUM, false, false);
		}
		
		if (intersectionMinimum > intersectionMaximum) {
			/**
			 * no congruency between A and B so we return an empty value range
			 */
			return new IntegerValueRangeConstraint();
		}
		
		return new IntegerValueRangeConstraint(intersectionMinimum, intersectionMaximum, false, false);			
	}

	private IntegerValueRangeConstraint performExcept(IntegerValueRangeConstraint rangeA, IntegerValueRangeConstraint rangeB) {
		try {
			if (rangeA.isEmpty() && rangeB.isEmpty()) {
				/**
				 * If both ranges are empty we also return an empty range
				 */
				return new IntegerValueRangeConstraint();
			} else if (rangeA.isEmpty()) {
				return rangeB.clone();
			} else if (rangeB.isEmpty()) {
				return rangeA.clone();
			}
			
			if (rangeB.hasLowerBoundExtrema() && rangeB.hasUpperBoundExtrema()) {
				/**
				 * Range B is the whole range. The resulting range is empty
				 */
				return new IntegerValueRangeConstraint();
			}
	
			if (rangeA.hasLowerBoundExtrema() && rangeA.hasUpperBoundExtrema()) {
				/**
				 * ---|----A----|---
				 */
				// TODO: handle cases where definedUpperBound == Extrema.MAXIMUM
				if (rangeB.hasLowerBoundExtrema()) {
					return new IntegerValueRangeConstraint(rangeB.getDefinedUpperBound()+1, Extrema.MAXIMUM);
				} else if (rangeB.hasUpperBoundExtrema()) {
					return new IntegerValueRangeConstraint(Extrema.MINIMUM, rangeB.getDefinedLowerBound()-1);
				} else {
					/**
					 * We use the whole range since only ranges matter for encoding
					 */
					return new IntegerValueRangeConstraint(Extrema.MINIMUM, Extrema.MAXIMUM);
				}
			} else if (rangeA.hasLowerBoundExtrema() && (! rangeA.hasUpperBoundExtrema())) {
				/**
				 * ---|----A----  |
				 */
				// TODO: handle cases where definedUpperBound == Extrema.MAXIMUM, definedLowerBound == Extrema.MINIMUM
				if (rangeB.hasLowerBoundExtrema()) {
					if (rangeB.getDefinedUpperBound() >= rangeA.getDefinedUpperBound()) {
						return new IntegerValueRangeConstraint();
					} else {
						return new IntegerValueRangeConstraint(rangeB.getDefinedUpperBound()+1, rangeA.getDefinedUpperBound());	
					}
				} else if (rangeB.hasUpperBoundExtrema()) {
					if (rangeB.getDefinedLowerBound() > rangeA.getDefinedUpperBound()) {
						return rangeA.clone();
					} else {
						return new IntegerValueRangeConstraint(Extrema.MINIMUM, rangeB.getDefinedLowerBound()-1);	
					}
				} else {
					if (rangeB.getDefinedLowerBound() > rangeA.getDefinedUpperBound()) {
						return rangeA.clone();
					} else if (rangeB.getDefinedUpperBound() > rangeA.getDefinedUpperBound()) {
						return new IntegerValueRangeConstraint(Extrema.MINIMUM, rangeB.getDefinedLowerBound()-1);	
					} else {
						return new IntegerValueRangeConstraint(Extrema.MINIMUM, rangeA.getDefinedUpperBound());
					}
				}			
			} else if ((! rangeA.hasLowerBoundExtrema()) && (rangeA.hasUpperBoundExtrema())) {
				/**
				 * |  --A----|---
				 */
				// TODO: handle cases where definedUpperBound == Extrema.MAXIMUM, definedLowerBound == Extrema.MINIMUM
				if (rangeB.hasLowerBoundExtrema()) {
					if (rangeB.getDefinedUpperBound() < rangeA.getDefinedLowerBound()) {
						return rangeA.clone();
					} else {
						return new IntegerValueRangeConstraint(rangeB.getDefinedUpperBound()+1, Extrema.MAXIMUM);	
					}
				} else if (rangeB.hasUpperBoundExtrema()) {
					if (rangeB.getDefinedLowerBound() <= rangeA.getDefinedLowerBound()) {
						return new IntegerValueRangeConstraint();
					} else {
						return new IntegerValueRangeConstraint(rangeA.getDefinedLowerBound(), rangeB.getDefinedLowerBound()-1);	
					}
				} else {
					if (rangeB.getDefinedUpperBound() < rangeA.getDefinedLowerBound()) {
						return rangeA.clone();
					} else if (rangeB.getDefinedLowerBound() < rangeA.getDefinedLowerBound()) {
						return new IntegerValueRangeConstraint(rangeB.getDefinedUpperBound()+1, Extrema.MAXIMUM);	
					} else {
						return new IntegerValueRangeConstraint(rangeA.getDefinedLowerBound(), Extrema.MAXIMUM);
					}
				}			
			} else {
				/**
				 * |  --A--  |
				 */
				if (rangeB.hasLowerBoundExtrema()) {
					if (rangeB.getDefinedUpperBound() >= rangeA.getDefinedUpperBound()) {
						return new IntegerValueRangeConstraint();
					} else if (rangeB.getDefinedUpperBound() < rangeA.getDefinedLowerBound()) {
						return rangeA.clone();
					} else {
						return new IntegerValueRangeConstraint(rangeB.getDefinedUpperBound()+1, rangeA.getDefinedUpperBound());	
					}
				} else if (rangeB.hasUpperBoundExtrema()) {
					if (rangeB.getDefinedLowerBound() <= rangeA.getDefinedLowerBound()) {
						return new IntegerValueRangeConstraint();
					} else if (rangeB.getDefinedLowerBound() > rangeA.getDefinedUpperBound()) {
						return rangeA.clone();
					} else {
						return new IntegerValueRangeConstraint(rangeA.getDefinedLowerBound(), rangeB.getDefinedLowerBound()-1);	
					}
				} else {
					if (rangeB.getDefinedUpperBound() < rangeA.getDefinedLowerBound()) {
						return rangeA.clone();
					} else if (rangeB.getDefinedLowerBound() > rangeA.getDefinedUpperBound()) {
						return rangeA.clone();
					} else if ((rangeB.getDefinedLowerBound() <= rangeA.getDefinedLowerBound()) && (rangeB.getDefinedUpperBound() >= rangeA.getDefinedUpperBound())) {
						return new IntegerValueRangeConstraint();
					} else if ((rangeB.getDefinedLowerBound() > rangeA.getDefinedLowerBound()) && (rangeB.getDefinedUpperBound() < rangeA.getDefinedUpperBound())) {
						return rangeA.clone();
					} else if ((rangeB.getDefinedLowerBound() < rangeA.getDefinedLowerBound()) && (rangeB.getDefinedUpperBound() < rangeA.getDefinedUpperBound())) {
						return new IntegerValueRangeConstraint(rangeB.getDefinedUpperBound()+1, rangeA.getDefinedUpperBound());
					} else {
						return new IntegerValueRangeConstraint(rangeA.getDefinedLowerBound()+1, rangeB.getDefinedLowerBound()-1);
					}
				}				
			}
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("clone of IntegerValueRange was not successful!");
		}
	}
	
	@Override
	public void visit(ConstraintUnion constraint) {
		super.visit(constraint);
		
		boolean comparable = false;
		
		IntegerValueRangeConstraint unionRootConstraint = null;
		IntegerValueRangeConstraint unionAdditionConstraint = null;
		
		for (Constraint subConstraint : constraint.getList()) {
			PERRestrictedCharacterStringSizeConstraintVisitor constraintVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(this);
			
			subConstraint.accept(constraintVisitor);
			
			if (constraintVisitor.hasEffectiveSizeConstraint()) {
				if (constraintVisitor.isExtensible()) {
					setExtensible(true);
				}

				if (comparable) {
					IntegerValueRangeConstraint comparingRootConstraint = null;
					IntegerValueRangeConstraint comparingAdditionConstraint = null;
					
					comparingRootConstraint = getRootIntegerValueRangeConstraint(constraintVisitor);
					if (constraintVisitor.hasEffectiveExtensionAdditionSizeConstraint()) {
						comparingAdditionConstraint = getAdditionIntegerValueRangeConstraint(constraintVisitor);
					}
					
					/**
					 * UNION Set Rules
					 * 
					 * @see "X.680-0207 G.4.3.8"
					 */
					if ((unionAdditionConstraint == null) && (comparingAdditionConstraint == null)) {
						/**
						 * N1 UNION N2 => N
						 * Root: R1 UNION R2
						 */
						unionRootConstraint = performUnion(unionRootConstraint, comparingRootConstraint);
					} else if ((unionAdditionConstraint == null) && (comparingAdditionConstraint != null)) {
						/**
						 * N1 UNION E2 => E
						 * Root: R1 UNION R2, Extensions: X2
						 */
						unionRootConstraint = performUnion(unionRootConstraint, comparingRootConstraint);
						unionAdditionConstraint = comparingAdditionConstraint;
					} else if ((unionAdditionConstraint != null) && (comparingAdditionConstraint == null)) {
						/**
						 * E1 UNION N2 => E
						 * Root: R1 UNION R2, Extensions: X1
						 */
						unionRootConstraint = performUnion(unionRootConstraint, comparingRootConstraint);
						// unionAdditionConstraint = unionAdditionConstraint;
					} else {
						/**
						 * E1 UNION E2 => E
						 * Root: R1 UNION R2, Extensions: (R1 UNION X1 UNION R2 UNION X2) EXCEPT (R1 UNION R2)
						 */
						IntegerValueRangeConstraint tempRootConstraint = null;
						IntegerValueRangeConstraint tempAdditionConstraint = null;
						
						tempRootConstraint = performUnion(unionRootConstraint, comparingRootConstraint);
						tempAdditionConstraint = performExcept(performUnion(performUnion(unionRootConstraint, unionAdditionConstraint), performUnion(comparingRootConstraint, comparingAdditionConstraint)), tempRootConstraint);
						
						unionRootConstraint = tempRootConstraint;
						unionAdditionConstraint = tempAdditionConstraint;
					}
					
				} else {
					unionRootConstraint = getRootIntegerValueRangeConstraint(constraintVisitor);
					if (constraintVisitor.hasEffectiveExtensionAdditionSizeConstraint()) {
						unionAdditionConstraint = getAdditionIntegerValueRangeConstraint(constraintVisitor);
					}

					comparable = true;
				}
			} else {
				/**
				 * TODO: We have to take care of PER-visible constraints.
				 * 		 If we have an PER-visible constraint then nothing should happen.
				 * 		 If the constraint is PER-invisible the the union of this constraint
				 * 		 (This behaviour has to be checked and stated out in the standard).
				 * 		 gives us all sizes. This is what we assume now.
				 * 
				 * @see "X.691-0207 B.3 Example 11" 
				 */
				
				setEffectiveExtensionRootSizeConstraint(null);
				setEffectiveExtensionAdditionSizeConstraint(null);
				
				return;
			}
		}
		
		setEffectiveExtensionRootSizeConstraint(unionRootConstraint);
		setEffectiveExtensionAdditionSizeConstraint(unionAdditionConstraint);
	}

	private IntegerValueRangeConstraint getRootIntegerValueRangeConstraint(PERRestrictedCharacterStringSizeConstraintVisitor constraintVisitor) {
		boolean hasConstrainedRootMinimum = constraintVisitor.hasConstrainedRootMinimum();
		boolean hasConstrainedRootMaximum = constraintVisitor.hasConstrainedRootMaximum();
		
		if (hasConstrainedRootMinimum && hasConstrainedRootMaximum) {
			return new IntegerValueRangeConstraint(constraintVisitor.getConstrainedRootMinimum(), constraintVisitor.getConstrainedRootMaximum());
		} else if (hasConstrainedRootMinimum && (! hasConstrainedRootMaximum)) {
			return new IntegerValueRangeConstraint(constraintVisitor.getConstrainedRootMinimum(), Extrema.MAXIMUM);
		} else if ((! hasConstrainedRootMinimum) && hasConstrainedRootMaximum) {
			return new IntegerValueRangeConstraint(Extrema.MINIMUM, constraintVisitor.getConstrainedRootMaximum());
		}
		
		return new IntegerValueRangeConstraint(Extrema.MINIMUM, Extrema.MAXIMUM);
	}

	private IntegerValueRangeConstraint getAdditionIntegerValueRangeConstraint(PERRestrictedCharacterStringSizeConstraintVisitor constraintVisitor) {
		boolean hasConstrainedAdditionMinimum = constraintVisitor.hasConstrainedAdditionMinimum();
		boolean hasConstrainedAdditionMaximum = constraintVisitor.hasConstrainedAdditionMaximum();
		
		if (hasConstrainedAdditionMinimum && hasConstrainedAdditionMaximum) {
			return new IntegerValueRangeConstraint(constraintVisitor.getConstrainedAdditionMinimum(), constraintVisitor.getConstrainedAdditionMaximum());
		} else if (hasConstrainedAdditionMinimum && (! hasConstrainedAdditionMaximum)) {
			return new IntegerValueRangeConstraint(constraintVisitor.getConstrainedAdditionMinimum(), Extrema.MAXIMUM);
		} else if ((! hasConstrainedAdditionMinimum) && hasConstrainedAdditionMaximum) {
			return new IntegerValueRangeConstraint(Extrema.MINIMUM, constraintVisitor.getConstrainedAdditionMaximum());
		}
		
		return new IntegerValueRangeConstraint(Extrema.MINIMUM, Extrema.MAXIMUM);
	}

	@Override
	public void visit(ConstraintIntersection constraint) {
		super.visit(constraint);
		
		boolean comparable = false;
		
		IntegerValueRangeConstraint intersectionRootConstraint = null;
		IntegerValueRangeConstraint intersectionAdditionConstraint = null;
		
		for (Constraint subConstraint : constraint.getList()) {
			PERRestrictedCharacterStringSizeConstraintVisitor constraintVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(this);
			
			subConstraint.accept(constraintVisitor);
			
			if (constraintVisitor.hasEffectiveSizeConstraint()) {
				if (constraintVisitor.isExtensible()) {
					setExtensible(true);
				}
				
				if (comparable) {
					IntegerValueRangeConstraint comparingRootConstraint = null;
					IntegerValueRangeConstraint comparingAdditionConstraint = null;
					
					comparingRootConstraint = getRootIntegerValueRangeConstraint(constraintVisitor);
					if (constraintVisitor.hasEffectiveExtensionAdditionSizeConstraint()) {
						comparingAdditionConstraint = getAdditionIntegerValueRangeConstraint(constraintVisitor);
					}

					IntegerValueRangeConstraint tempRootConstraint = null;
					IntegerValueRangeConstraint tempAdditionConstraint = null;

					/**
					 * INTERSECTION Set Rules
					 * 
					 * @see "X.680-0207 G.4.3.8"
					 */
					if ((intersectionAdditionConstraint == null) && (comparingAdditionConstraint == null)) {
						/**
						 * N1 INTERSECTION N2 => N
						 * Root: R1 INTERSECTION R2
						 */
						intersectionRootConstraint = performIntersection(intersectionRootConstraint, comparingRootConstraint);
					} else if ((intersectionAdditionConstraint == null) && (comparingAdditionConstraint != null)) {
						/**
						 * N1 INTERSECTION E2 => E
						 * Root: R1 INTERSECTION R2, Extensions: R1 INTERSECTION X2
						 */
						tempRootConstraint = performIntersection(intersectionRootConstraint, comparingRootConstraint);
						tempAdditionConstraint = performIntersection(intersectionRootConstraint, comparingAdditionConstraint);

						intersectionRootConstraint = tempRootConstraint;
						intersectionAdditionConstraint = tempAdditionConstraint;
					} else if ((intersectionAdditionConstraint != null) && (comparingAdditionConstraint == null)) {
						/**
						 * E1 INTERSECTION N2 => E
						 * Root: R1 INTERSECTION R2, Extensions: R2 INTERSECTION X1
						 */
						tempRootConstraint = performIntersection(intersectionRootConstraint, comparingRootConstraint);
						tempAdditionConstraint = performIntersection(comparingRootConstraint, intersectionAdditionConstraint);

						intersectionRootConstraint = tempRootConstraint;
						intersectionAdditionConstraint = tempAdditionConstraint;
					} else {
						/**
						 * E1 INTERSECTION E2 => E
						 * Root: R1 INTERSECTION R2, Extensions: ((R1 UNION X1) INTERSECTION (R2 UNION X2)) EXCEPT (R1 INTERSECTION R2)
						 */
						tempRootConstraint = performUnion(intersectionRootConstraint, comparingRootConstraint);
						tempAdditionConstraint = performExcept(performIntersection(performUnion(intersectionRootConstraint, intersectionAdditionConstraint), performUnion(comparingRootConstraint, comparingAdditionConstraint)), tempRootConstraint);
						
						intersectionRootConstraint = tempRootConstraint;
						intersectionAdditionConstraint = tempAdditionConstraint;
					}
					
				} else {
					intersectionRootConstraint = getRootIntegerValueRangeConstraint(constraintVisitor);
					if (constraintVisitor.hasEffectiveExtensionAdditionSizeConstraint()) {
						intersectionAdditionConstraint = getAdditionIntegerValueRangeConstraint(constraintVisitor);
					}

					comparable = true;
				}
			}
		}
		
		setEffectiveExtensionRootSizeConstraint(intersectionRootConstraint);
		setEffectiveExtensionAdditionSizeConstraint(intersectionAdditionConstraint);
	}
	
	/**
	 * NOTE: "If a complete constraint in serial application of constraints 
	 * 		 is not PER-visible, then for the purposes of PER encodings, 
	 *    	 that constraint is simply completely ignored."
	 * 
	 * @see "X.691-0207 B.2.2.2"
	 */
	@Override
	public void visit(ConstraintSerialisation constraint) {
		super.visit(constraint);
		
		boolean comparable = false;
		
		IntegerValueRangeConstraint intersectionRootConstraint = null;
		IntegerValueRangeConstraint intersectionAdditionConstraint = null;
		
		for (Constraint subConstraint : constraint.getList()) {
			PERRestrictedCharacterStringSizeConstraintVisitor constraintVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(this);
			
			subConstraint.accept(constraintVisitor);
			
			/**
			 * Extensibility depends solely on the last constraint that is applied.
			 * 
			 * @see "X.680-0207 G.4.2.3"
			 */
			setExtensible(constraintVisitor.isExtensible());
			intersectionAdditionConstraint = null;
			
			if (constraintVisitor.hasEffectiveSizeConstraint()) {
				if (comparable) {
					IntegerValueRangeConstraint comparingRootConstraint = null;
					IntegerValueRangeConstraint comparingAdditionConstraint = null;
					
					comparingRootConstraint = getRootIntegerValueRangeConstraint(constraintVisitor);
					if (constraintVisitor.hasEffectiveExtensionAdditionSizeConstraint()) {
						comparingAdditionConstraint = getAdditionIntegerValueRangeConstraint(constraintVisitor);
					}

					IntegerValueRangeConstraint tempRootConstraint = null;
					IntegerValueRangeConstraint tempAdditionConstraint = null;

					/**
					 * INTERSECTION Set Rules
					 * 
					 * @see "X.680-0207 G.4.3.8"
					 */
					if ((intersectionAdditionConstraint == null) && (comparingAdditionConstraint == null)) {
						/**
						 * N1 INTERSECTION N2 => N
						 * Root: R1 INTERSECTION R2
						 */
						intersectionRootConstraint = performIntersection(intersectionRootConstraint, comparingRootConstraint);
					} else if ((intersectionAdditionConstraint == null) && (comparingAdditionConstraint != null)) {
						/**
						 * N1 INTERSECTION E2 => E
						 * Root: R1 INTERSECTION R2, Extensions: R1 INTERSECTION X2
						 */
						tempRootConstraint = performIntersection(intersectionRootConstraint, comparingRootConstraint);
						tempAdditionConstraint = performIntersection(intersectionRootConstraint, comparingAdditionConstraint);

						intersectionRootConstraint = tempRootConstraint;
						intersectionAdditionConstraint = tempAdditionConstraint;
					} else if ((intersectionAdditionConstraint != null) && (comparingAdditionConstraint == null)) {
						/**
						 * E1 INTERSECTION N2 => E
						 * Root: R1 INTERSECTION R2, Extensions: R2 INTERSECTION X1
						 */
						tempRootConstraint = performIntersection(intersectionRootConstraint, comparingRootConstraint);
						tempAdditionConstraint = performIntersection(comparingRootConstraint, intersectionAdditionConstraint);

						intersectionRootConstraint = tempRootConstraint;
						intersectionAdditionConstraint = tempAdditionConstraint;
					} else {
						/**
						 * E1 INTERSECTION E2 => E
						 * Root: R1 INTERSECTION R2, Extensions: ((R1 UNION X1) INTERSECTION (R2 UNION X2)) EXCEPT (R1 INTERSECTION R2)
						 */
						tempRootConstraint = performUnion(intersectionRootConstraint, comparingRootConstraint);
						tempAdditionConstraint = performExcept(performIntersection(performUnion(intersectionRootConstraint, intersectionAdditionConstraint), performUnion(comparingRootConstraint, comparingAdditionConstraint)), tempRootConstraint);
						
						intersectionRootConstraint = tempRootConstraint;
						intersectionAdditionConstraint = tempAdditionConstraint;
					}
					
				} else {
					intersectionRootConstraint = getRootIntegerValueRangeConstraint(constraintVisitor);
					if (constraintVisitor.hasEffectiveExtensionAdditionSizeConstraint()) {
						intersectionAdditionConstraint = getAdditionIntegerValueRangeConstraint(constraintVisitor);
					}

					comparable = true;
				}
			}
			
			setEffectiveExtensionRootSizeConstraint(intersectionRootConstraint);
			setEffectiveExtensionAdditionSizeConstraint(intersectionAdditionConstraint);
		}
	}

	/**
	 * NOTE: "If a constraint has an EXCEPT clause, the EXCEPT and the following 
	 * 		 value set is completely ignored, whether the value set following 
	 * 		 the EXCEPT is PER-visible or not."
	 * 
	 * @see "X.691-0207 9.3.19"
	 */
	@Override
	public void visit(ConstraintExcept constraint) {
		super.visit(constraint);
		
		PERRestrictedCharacterStringSizeConstraintVisitor operandAVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(this); 
		
		/**
		 * NOTE: since B is the EXCEPT constraint operand it will be 
		 * 		 completely ignored. (see note above).
		 */
		constraint.getOperandA().accept(operandAVisitor);

		setEffectiveExtensionRootSizeConstraint(operandAVisitor.getEffectiveExtensionRootSizeConstraint());
		setEffectiveExtensionAdditionSizeConstraint(operandAVisitor.getEffectiveExtensionAdditionSizeConstraint());
		setExtensible(operandAVisitor.isExtensible());
	}
	
	@Override
	public void visit(ConstraintExtension constraint) {
		super.visit(constraint);
		
		PERRestrictedCharacterStringSizeConstraintVisitor rootVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(this); 
		PERRestrictedCharacterStringSizeConstraintVisitor additionVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(this); 

		constraint.getExtensionRootConstraint().accept(rootVisitor);
		
		if (constraint.hasExtensionAdditionConstraint()) {
			constraint.getExtensionAdditionConstraint().accept(additionVisitor);
		}
		
		// if (rootVisitor.hasEffectiveSizeConstraint()) {
			setExtensible(true);
		// }
		
		setEffectiveExtensionRootSizeConstraint(rootVisitor.getEffectiveExtensionRootSizeConstraint());
		setEffectiveExtensionAdditionSizeConstraint(additionVisitor.getEffectiveExtensionRootSizeConstraint());
	}
}