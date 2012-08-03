/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.provider.ReferenceChangeItemProvider;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * Specialized {@link ReferenceChangeItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ReferenceChangeItemProviderSpec extends ReferenceChangeItemProvider {

	/**
	 * Constructor calling super {@link #ReferenceChangeItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public ReferenceChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		final ReferenceChange refChange = (ReferenceChange)object;

		final String valueText = getValueText(refChange);
		final String referenceText = getReferenceText(refChange);

		String remotely = "";
		if (refChange.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely ";
		}

		String ret = "";
		switch (refChange.getKind()) {
			case ADD:
				ret = valueText + " has been " + remotely + "added to " + referenceText;
				break;
			case DELETE:
				ret = valueText + " has been " + remotely + "deleted from " + referenceText;
				break;
			case CHANGE:
				ret = referenceText + " " + valueText + " has been " + remotely + "changed";
				break;
			case MOVE:
				ret = valueText + " has been " + remotely + "moved in " + referenceText;
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + refChange.getKind());
		}

		return ret;
	}

	protected String getReferenceText(final ReferenceChange refChange) {
		return refChange.getReference().getName();
	}

	protected String getValueText(final ReferenceChange refChange) {
		String value = CompareItemProviderAdapterFactorySpec.getText(getRootAdapterFactory(), refChange
				.getValue());
		if (value == null) {
			value = "<null>";
		} else {
			value = Strings.elide(value, 20, "...");
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		ReferenceChange refChange = (ReferenceChange)object;

		Object image = CompareItemProviderAdapterFactorySpec.getImage(getRootAdapterFactory(), refChange
				.getValue());

		return image;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Collection<?> superChildren = super.getChildren(object);
		List<? super Object> ret = newArrayList(superChildren);
		ReferenceChange referenceChange = (ReferenceChange)object;
		EReference reference = referenceChange.getReference();

		if (reference.isContainment()) {
			Match match = referenceChange.getMatch().getComparison().getMatch(referenceChange.getValue());
			if (match != null) {
				ITreeItemContentProvider matchItemContentProvider = (ITreeItemContentProvider)adapterFactory
						.adapt(match, ITreeItemContentProvider.class);
				if (matchItemContentProvider != null) {
					Collection<Object> children = newArrayList(matchItemContentProvider.getChildren(match));

					children.remove(referenceChange);

					Iterator<?> childrenIterator = children.iterator();
					while (childrenIterator.hasNext()) {
						Object child = childrenIterator.next();
						if (child instanceof Match) {
							if (!matchItemContentProvider.hasChildren(child)) {
								childrenIterator.remove();
							}
						}

					}
					ret.addAll(children);
				}
			}
		}

		return ret;
	}
}
