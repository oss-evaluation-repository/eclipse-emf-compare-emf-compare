/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.fullcomparison.data.distance;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DistanceMatchInputData extends AbstractInputData {

	public Resource getCompareLeft() throws IOException {
		return loadFromClassloader("compareLeft.ecore");
	}

	public Resource getCompareRight() throws IOException {
		return loadFromClassloader("compareRight.ecore");
	}

	public Resource getNominalUMLLeft() throws IOException {
		return loadFromClassloader("agencyLeft.uml");
	}

	public Resource getNominalUMLRight() throws IOException {
		return loadFromClassloader("agencyRight.uml");
	}

	public Resource getNominalUMLOrigin() throws IOException {
		return loadFromClassloader("agencyOrigin.uml");
	}

	public Resource getVerySmallLeft() throws IOException {
		return loadFromClassloader("verySmallLeft.ecore");
	}

	public Resource getVerySmallRight() throws IOException {
		return loadFromClassloader("verySmallRight.ecore");
	}

}
