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
package com.github.openasn1.compiler.astutils.helpers;

import com.github.openasn1.compiler.astutils.visitors.GetNextNestedNodeTokenVisitor;
import com.github.openasn1.parser.generated.syntaxtree.Node;

/**
 * @author Clayton Hoss
 * 
 */
public class LogginHelper {

    public static class PositionInFileInformation {
        private int line;

        private int column;

        public PositionInFileInformation(int line, int column) {
            super();
            this.line = line;
            this.column = column;
        }

        /**
         * @return the column
         */
        public int getColumn() {
            return this.column;
        }

        /**
         * @return the line
         */
        public int getLine() {
            return this.line;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return this.line + ":" + this.column;
        }

    }

    public static PositionInFileInformation getCurrentLine(Node n) {
        GetNextNestedNodeTokenVisitor tv = new GetNextNestedNodeTokenVisitor();
        n.accept(tv);
        return new PositionInFileInformation(tv.getNodeToken().beginLine, tv
                .getNodeToken().beginColumn);
    }
}
