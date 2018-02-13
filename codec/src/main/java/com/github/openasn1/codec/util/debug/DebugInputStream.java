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
package com.github.openasn1.codec.util.debug;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author Marc Weyland
 *
 */
public class DebugInputStream extends FilterInputStream {
	private String startToken = "";
	private String endToken = ".";
	private int tokenLimit = 0;
	private int readBytes = 0;	
	
	public DebugInputStream(InputStream arg0) {
		super(arg0);
	}

	protected int getTokenLimit() {
		return this.tokenLimit;
	}

	protected void setTokenLimit(int tokenLimit) {
		this.tokenLimit = tokenLimit;
	}
	
	protected String getEndToken() {
		return this.endToken;
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
	
	protected int getReadBytes() {
		return this.readBytes;
	}	
	
	@Override
	public int read() throws IOException {
		this.readBytes++;
		
		if (null != super.in) {
			return super.read();
		}
		
		throw new IOException("No input source selected");
	}
}
