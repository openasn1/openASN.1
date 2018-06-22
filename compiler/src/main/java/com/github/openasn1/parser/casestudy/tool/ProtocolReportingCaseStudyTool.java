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
package com.github.openasn1.parser.casestudy.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Clayton Hoss
 * 
 */
public class ProtocolReportingCaseStudyTool extends CaseStudyTool {

	private List<String> failedProtocols = new ArrayList<String>();

	private List<String> passedProtocols = new ArrayList<String>();

	public ProtocolReportingCaseStudyTool(String dir) {
		super(dir);
	}

	@Override
	protected void newProtocolEncountered() {
		if (getLastProtocolPackage().equals("")) {
			return;
		}
		if (isLastProtocolWithoutError()) {
			getPassedProtocols().add(getLastProtocolPackage());
		} else {
			getFailedProtocols().add(getLastProtocolPackage());
		}
	}

	@Override
	public void scandir() {
		super.scandir();
		System.out.println();
		System.out.println("Passed Protocols");
		System.out.println("----------------");
		for(String s: getPassedProtocols()) {
			System.out.println(s);
		}
		
		System.out.println();
		System.out.println("Failed Protocols");
		System.out.println("----------------");
		for(String s: getFailedProtocols()) {
			System.out.println(s);
		}

	}

	/**
	 * @return the failedProtocols
	 */
	private List<String> getFailedProtocols() {
		return this.failedProtocols;
	}

	/**
	 * @return the passedProtocols
	 */
	private List<String> getPassedProtocols() {
		return this.passedProtocols;
	}

}
