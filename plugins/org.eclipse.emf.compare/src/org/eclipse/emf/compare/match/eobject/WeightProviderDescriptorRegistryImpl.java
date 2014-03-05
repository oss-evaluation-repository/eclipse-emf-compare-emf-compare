/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.match.eobject.internal.WeightProviderDescriptorImpl;
import org.eclipse.emf.ecore.EPackage;

/**
 * This will contain all of the EMF Compare Weight Providers Descriptors.
 * 
 * @since 3.1.0
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class WeightProviderDescriptorRegistryImpl implements WeightProvider.Descriptor.Registry {

	/** List of all the weight providers contributed through extension point. */
	private final Map<String, WeightProvider.Descriptor> weightProviders;

	/**
	 * Creates a new extension registry.
	 */
	public WeightProviderDescriptorRegistryImpl() {
		weightProviders = Maps.newHashMap();
	}

	/**
	 * Returns a registry filled with the default weight provider {@link DefaultWeightProvider} and the ecore
	 * weight provider {@link EcoreWeightProvider} provided by EMF Compare.
	 * 
	 * @return A registry filled with the default & ecore weight providers provided by EMF Compare.
	 */
	public static WeightProvider.Descriptor.Registry createStandaloneInstance() {
		final WeightProviderDescriptorRegistryImpl registry = new WeightProviderDescriptorRegistryImpl();

		final DefaultWeightProvider defaultWeightProvider = new DefaultWeightProvider();
		final WeightProviderDescriptorImpl dwpDescriptor = new WeightProviderDescriptorImpl(
				defaultWeightProvider, 100, Pattern.compile(".*")); //$NON-NLS-1$
		final EcoreWeightProvider ecoreWeightProvider = new EcoreWeightProvider();
		final WeightProviderDescriptorImpl ewpDescriptor = new WeightProviderDescriptorImpl(
				ecoreWeightProvider, 101, Pattern.compile("http://www.eclipse.org/emf/2002/Ecore")); //$NON-NLS-1$

		registry.put(dwpDescriptor.getNsURI().toString(), dwpDescriptor);
		registry.put(ewpDescriptor.getNsURI().toString(), ewpDescriptor);

		return registry;
	}

	/**
	 * {@inheritDoc}
	 */
	public WeightProvider.Descriptor put(String key, WeightProvider.Descriptor weightProvider) {
		return weightProviders.put(key, weightProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		weightProviders.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public ImmutableList<WeightProvider.Descriptor> getDescriptors() {
		return ImmutableList.copyOf(weightProviders.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public WeightProvider.Descriptor remove(String key) {
		return weightProviders.remove(key);
	}

	/**
	 * {@inheritDoc}
	 */
	public WeightProvider getHighestRankingWeightProvider(EPackage ePackage) {
		Iterator<WeightProvider.Descriptor> descriptors = getWeightProviderDescriptors(ePackage).iterator();

		WeightProvider ret = null;

		if (descriptors.hasNext()) {
			WeightProvider.Descriptor highestRanking = descriptors.next();
			while (descriptors.hasNext()) {
				WeightProvider.Descriptor desc = descriptors.next();
				if (desc.getRanking() > highestRanking.getRanking()) {
					highestRanking = desc;
				}
			}
			ret = highestRanking.getWeightProvider();
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	public ImmutableList<WeightProvider> getWeightProviders(EPackage ePackage) {
		final ImmutableList.Builder<WeightProvider> weightProvidersBuilder = ImmutableList.builder();
		for (WeightProvider.Descriptor descriptor : getWeightProviderDescriptors(ePackage)) {
			weightProvidersBuilder.add(descriptor.getWeightProvider());
		}
		return weightProvidersBuilder.build();
	}

	/**
	 * Retrieve the weight providers descriptors from a given ePackage.
	 * 
	 * @param ePackage
	 *            the given ePackage.
	 * @return the weight providers descriptors from a given ePackage.
	 */
	private ImmutableList<WeightProvider.Descriptor> getWeightProviderDescriptors(EPackage ePackage) {
		final ImmutableList.Builder<WeightProvider.Descriptor> weightProvidersBuilder = ImmutableList
				.builder();
		for (WeightProvider.Descriptor descriptor : getDescriptors()) {
			Pattern nsURIPattern = descriptor.getNsURI();
			if (nsURIPattern != null) {
				String nsURI = ePackage.getNsURI();
				if (nsURIPattern.matcher(nsURI).matches()) {
					weightProvidersBuilder.add(descriptor);
				}
			}
		}
		return weightProvidersBuilder.build();
	}

}
