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
package com.github.openasn1.compiler.stages.omast.codeoutput.utils;

public class ChainedNamingStrategy implements TypeNamingStrategy {
	TypeNamingStrategy strategyA;
	TypeNamingStrategy strategyB;
	
	public ChainedNamingStrategy(TypeNamingStrategy strategyA, TypeNamingStrategy strategyB) {
		this.strategyA = strategyA;
		this.strategyB = strategyB;
	}

	public String getName(String name, int depth) {
		return getStrategyB().getName(getStrategyA().getName(name, depth), depth);
	}

	public TypeNamingStrategy getStrategyA() {
		return strategyA;
	}

	public void setStrategyA(TypeNamingStrategy strategyA) {
		this.strategyA = strategyA;
	}

	public TypeNamingStrategy getStrategyB() {
		return strategyB;
	}

	public void setStrategyB(TypeNamingStrategy strategyB) {
		this.strategyB = strategyB;
	}
}