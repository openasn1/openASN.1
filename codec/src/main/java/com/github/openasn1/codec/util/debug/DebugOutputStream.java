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
package com.github.openasn1.codec.util.debug;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Marc Weyland
 *
 */
public class DebugOutputStream extends FilterOutputStream {

	private String startToken = "";
	private String endToken = "";
	private int tokenLimit = 0;
	int writtenBytes = 0;	

	public DebugOutputStream(OutputStream outputStream) {
		super(outputStream);
	}

	protected String getEndToken() {
		return this.endToken;
	}

	protected int getTokenLimit() {
		return this.tokenLimit;
	}

	protected void setTokenLimit(int tokenLimit) {
		this.tokenLimit = tokenLimit;
	}

	protected void setEndToken(String endToken) {
		this.endToken = endToken;
	}

	protected String getStartToken() {
		return this.startToken;
	}

	protected void setStartToken(String startToken) {
		this.startToken = startToken;
	}

	protected int getWrittenBytes() {
		return this.writtenBytes;
	}

	@Override
	public void write(int arg0) throws IOException {
		this.writtenBytes++;
		
		if (null != super.out) {
			super.write(arg0);
		}
	}
}