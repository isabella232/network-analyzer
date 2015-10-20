package de.mpg.mpi_inf.bioinf.netanalyzer.ui.filter;

/*
 * #%L
 * Cytoscape NetworkAnalyzer Impl (network-analyzer-impl)
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2006 - 2013
 *   Max Planck Institute for Informatics, Saarbruecken, Germany
 *   The Cytoscape Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 2.1 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.GroupLayout.Alignment.CENTER;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;

import java.awt.Window;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import de.mpg.mpi_inf.bioinf.netanalyzer.data.IntHistogram;
import de.mpg.mpi_inf.bioinf.netanalyzer.data.filter.ComplexParamFilter;
import de.mpg.mpi_inf.bioinf.netanalyzer.data.filter.IntHistogramFilter;
import de.mpg.mpi_inf.bioinf.netanalyzer.data.settings.IntHistogramGroup;
import de.mpg.mpi_inf.bioinf.netanalyzer.ui.Utils;

/**
 * Dialog for creating {@link de.mpg.mpi_inf.bioinf.netanalyzer.data.filter.IntHistogramFilter} based on
 * user's input.
 * 
 * @author Yassen Assenov
 */
@SuppressWarnings("serial")
public class IntHistogramFilterDialog extends ComplexParamFilterDialog {

	/** Spinner to choose the maximum observation value to display. */
	private JSpinner minSpinner;
	/** Spinner to choose the maximum observation value to display. */
	private JSpinner maxSpinner;
	
	/**
	 * Initializes a new instance of <code>IntHistogramFilterDialog</code> based on the given IntHistgoram
	 * instance.
	 * 
	 * @param owner The <code>Window</code> from which this dialog is displayed.
	 * @param title Title of the dialog.
	 * @param histogram Histogram instance, based on which the ranges for the minimum and maximum degrees are
	 *            to be chosen.
	 * @param settings Visual settings for <code>histogram</code>.
	 */
	public IntHistogramFilterDialog(Window owner, String title, IntHistogram histogram, IntHistogramGroup settings) {
		super(owner, title);

		populate(histogram, settings);
		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	/**
	 * Creates and initializes a filter instance based on user's choice for minimum and maximum degree.
	 * 
	 * @return Instance of <code>IntHistogramFilter</code> reflecting the user's filtering criteria.
	 */
	@Override
	protected ComplexParamFilter createFilter() {
		return new IntHistogramFilter(Utils.getSpinnerInt(minSpinner), Utils.getSpinnerInt(maxSpinner));
	}

	/**
	 * Creates and lays out the two spinner controls for choosing minimum and maximum degree.
	 * 
	 * @param histogram Histogram instance, based on which the ranges for the minimum and maximum degrees are
	 *            to be chosen.
	 * @param settings Visual settings for <code>histogram</code>.
	 */
	private void populate(IntHistogram histogram, IntHistogramGroup settings) {
		final int[] range = histogram.getObservedRange();

		// Add a spinner for minimum observation
		final JLabel minLabel = new JLabel(settings.filter.getMinObservationLabel() + ":", SwingConstants.RIGHT);
		final SpinnerModel minSettings = new SpinnerNumberModel(range[0], range[0], range[1], 1);
		minSpinner = new JSpinner(minSettings);

		// Add a spinner for maximum observation
		final JLabel maxLabel = new JLabel(settings.filter.getMaxObservationLabel() + ":", SwingConstants.RIGHT);
		final SpinnerModel maxSettings = new SpinnerNumberModel(range[1], range[0], range[1], 1);
		maxSpinner = new JSpinner(maxSettings);
		
		final GroupLayout layout = new GroupLayout(centralPane);
		centralPane.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGap(0, 0, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(TRAILING)
						.addComponent(minLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(maxLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(LEADING)
						.addComponent(minSpinner, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(maxSpinner, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGap(0, 0, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(minLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(minSpinner, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(maxLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(maxSpinner, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
		);
	}
}
