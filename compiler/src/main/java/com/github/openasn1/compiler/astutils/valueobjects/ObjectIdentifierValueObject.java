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
package com.github.openasn1.compiler.astutils.valueobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Clayton Hoss
 *
 */
public class ObjectIdentifierValueObject {
    private List<Integer> value = new ArrayList<Integer>();

    public ObjectIdentifierValueObject(List<Integer> oid) {
        this.value = new ArrayList<Integer>(oid);
    }

    public ObjectIdentifierValueObject(Integer... oid) {
        this.value = java.util.Arrays.asList(oid);
    }

    public List<Integer> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (Integer i : getValue()) {
            s.append(i);
            s.append(" ");
        }
        return s.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ObjectIdentifierValueObject other = (ObjectIdentifierValueObject) obj;
        if (this.value == null) {
            if (other.value != null)
                return false;
        } else if (!this.value.equals(other.value))
            return false;
        return true;
    }
}
