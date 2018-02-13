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
 *   along with openASN.1. If not, see <http://www.gnu.com.github.licenses/>.
 * 
 */
package com.github.openasn1.codec.constraints;

/**
 * @author Marc Weyland
 *
 */
abstract public class BinaryConstraintOperation implements Constraint {
	private Constraint operandA;
	private Constraint operandB;

	public BinaryConstraintOperation(Constraint operandA, Constraint operandB) {
		this.operandA = operandA;
		this.operandB = operandB;
	}

	public Constraint getOperandA() {
		return this.operandA;
	}

	public boolean hasOperandA() {
		return null != getOperandA();
	}

	public Constraint getOperandB() {
		return this.operandB;
	}

	public boolean hasOperandB() {
		return null != getOperandB();
	}

}