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
package com.github.openasn1.compiler.astutils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Clayton Hoss
 *
 */
public class GenericNodeInfos<Node> {

    private Map<Node, Map<String, Object>> nodeInfos = new HashMap<Node, Map<String, Object>>();

    public void insertInfoIntoNode(Node n, String ident, Object o) {
        Map<String, Object> nodeInfos = getNodeInfoMap(n);
        nodeInfos.put(ident, o);
    }

    public Map<String, Object> getNodeInfoMap(Node n) {
        if (!getNodeInfos().containsKey(n)) {
            getNodeInfos().put(n, new HashMap<String, Object>());
        }
        return getNodeInfos().get(n);
    }

    public Object returnInfoFromNode(Node n, String key) {
        return getNodeInfoMap(n).get(key);
    }

    public Object deleteInfoFromNode(Node n, String key) {
        return getNodeInfoMap(n).remove(key);
    }

    /**
     * @return the nodeinfos
     */
    private Map<Node, Map<String, Object>> getNodeInfos() {
        return this.nodeInfos;
    }
}
