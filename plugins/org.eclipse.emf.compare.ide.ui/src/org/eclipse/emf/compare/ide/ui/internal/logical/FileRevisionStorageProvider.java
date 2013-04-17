/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.history.IFileRevision;

/** Allows access to the underlying IFileRevision's storage. */
public class FileRevisionStorageProvider implements IStorageProvider {
	/** Wrapped file revision. */
	private final IFileRevision revision;

	/**
	 * Wraps a file revision as a storage provider.
	 * 
	 * @param revision
	 *            The wrapped file revision.
	 */
	public FileRevisionStorageProvider(IFileRevision revision) {
		this.revision = revision;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.logical.IStorageProvider#getStorage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStorage getStorage(IProgressMonitor monitor) throws CoreException {
		return revision.getStorage(monitor);
	}
}
