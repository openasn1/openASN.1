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
package com.github.openasn1.compiler.utils.reflectivevisitor;

import java.lang.reflect.Method;

public class ReflectiveVisitorDispatcherImpl implements ReflectiveVisitorDispatcher {
	private static ReflectiveVisitorDispatcher instance;
	
	private ReflectiveVisitorDispatcherImpl() {}

	static {
		instance = new ReflectiveVisitorDispatcherImpl(); 
	}

	public static ReflectiveVisitorDispatcher getInstance() {
		return instance;
	}
	
	/**
	 * @see visitors.ReflectiveVisitorDispatcher#dispatch(visitors.ReflectiveVisitor, java.lang.Object)
	 */
	public boolean dispatch(ReflectiveVisitor reflectiveVisitor, Object object) {
		return dispatch(reflectiveVisitor, object, object.getClass());
	}
	
	private boolean dispatch(ReflectiveVisitor reflectiveVisitor, Object object, Class clazz) {
		try {
			Method method = reflectiveVisitor.getClass().getMethod("visit", new Class[] { clazz });
			method.invoke(reflectiveVisitor, new Object[]{object});
			dispatchingSuccessful(method, reflectiveVisitor, object, clazz);
			return true;
		} catch (NoSuchMethodException e) {
			return dispatchingUnsuccessful(reflectiveVisitor, object, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected boolean dispatchingUnsuccessful(ReflectiveVisitor reflectiveVisitor, Object object, Class clazz) {
		if (clazz.getSuperclass() != null) {
			return dispatch(reflectiveVisitor, object, clazz.getSuperclass());
		}

		for (Class interfaze : clazz.getInterfaces()) {
			return dispatch(reflectiveVisitor, object, interfaze);
		}
		
		return false;
	}

	protected void dispatchingSuccessful(Method method, ReflectiveVisitor reflectiveVisitor, Object object, Class clazz) {
		// Empty stub
	}
}