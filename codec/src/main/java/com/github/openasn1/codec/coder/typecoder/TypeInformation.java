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
package com.github.openasn1.codec.coder.typecoder;

import java.util.ArrayList;

import com.github.openasn1.codec.coder.Tag;
import com.github.openasn1.codec.constraints.Constraint;


/**
 * @author Marc Weyland
 *
 */
public class TypeInformation implements Cloneable {
	private ArrayList<Tag> tagList;
	private Constraint constraint;
	private Object defaultValue;
	private boolean isOptional = false;
	private boolean isExtensible = false;
	private boolean isExtensionAddition = false;
	
	public TypeInformation() {
	}

	/**
	 * @return the isExtensionAddition
	 */
	public boolean isExtensionAddition() {
		return this.isExtensionAddition;
	}

	/**
	 * @param isExtensionAddition the isExtensionAddition to set
	 */
	public void setExtensionAddition(boolean isExtensionAddition) {
		this.isExtensionAddition = isExtensionAddition;
	}

	/**
	 * Returns the extension addition group index in which the component is in.
	 * 
	 * @return integer The group index of the extension addition. 0 if the component is in the extension root.
	 */
	public int getExtensionAdditionGroupIndex() {
		return 0;
	}
	
	/**
	 * Returns if the component has one or two extension markers.
	 *  
	 * @return boolean true if one or two extension markers are present, false otherwise 
	 */
	public boolean isExtensible() {
		return this.isExtensible;
	}
	
	/**
	 * @param isExtensible the isExtensible to set
	 */
	public void setExtensible(boolean isExtensible) {
		this.isExtensible = isExtensible;
	}


	/**
	 * Returns if the component is defined optional.
	 * 
	 * @return boolean true if the component is defined optional, false otherwise
	 */
	public boolean isOptional() {
		return this.isOptional;
	}
	
	/**
	 * @param isOptional the isOptional to set
	 */
	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	/**
	 * Returns if the component has a default value.
	 * 
	 * @return boolean true if a default value is set, false otherwise  
	 */
	public boolean hasDefaultValue() {
		return this.defaultValue != null;
	}

	/**
	 * Returns the defined default value of the component.
	 * 
	 * @return Object The default value of the component
	 */
	public Object getDefaultValue() {
		return this.defaultValue;
	}
	
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Returns the taglist of the component.
	 * 
	 * @return int The taglist of the component
	 */
	public ArrayList<Tag> getTagList() { 
		return this.tagList;
	}

	/**
	 * Returns a taglist is set
	 * 
	 * @return If the taglist is set
	 */
	public boolean hasTagList() { 
		return getTagList() != null;
	}
	
	/**
	 * @param tagList the tagList to set
	 */
	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
	}

	/**
	 * Returns the constraint associated to the component.
	 * 
	 * @return Constraint A list of type constraints
	 */
	public Constraint getConstraint() {
		return this.constraint;
	}

	/**
	 * @param constraint the constraint to set
	 */
	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	/**
	 * Returns is constraits are set for this component
	 * 
	 * @return boolean true if a constraint is present
	 */
	public boolean hasConstraint() {
		return null != this.getConstraint();
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TypeInformation clone() throws CloneNotSupportedException {
		TypeInformation clone = (TypeInformation)super.clone();
		
		clone.setConstraint(null);
		clone.setDefaultValue(null);
		clone.setTagList(null);
		
		return clone;
	}	
}