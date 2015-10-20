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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import de.mpg.mpi_inf.bioinf.netanalyzer.InnerException;
import de.mpg.mpi_inf.bioinf.netanalyzer.data.Points2D;
import de.mpg.mpi_inf.bioinf.netanalyzer.data.filter.ComplexParamFilter;
import de.mpg.mpi_inf.bioinf.netanalyzer.data.filter.Points2DFilter;
import de.mpg.mpi_inf.bioinf.netanalyzer.data.settings.Points2DGroup;

/**
 * Dialog for creating {@link de.mpg.mpi_inf.bioinf.netanalyzer.data.filter.Points2DFilter} based on user's input.
 * 
 * @author Yassen Assenov
 */
@SuppressWarnings("serial")
public class Points2DFilterDialog extends ComplexParamFilterDialog implements PropertyChangeListener {

	/**
	 * Number formatting style used in the text fields.
	 */
	private static final NumberFormat formatter = NumberFormat.getInstance(Locale.US);
	
	/** Text field for typing in the value for minimum x. */
	private JFormattedTextField minTextField;
	/** Text field for typing in the value for maximum x. */
	private JFormattedTextField maxTextField;

	/**
	 * Range of allowed values to be typed for X. The element at index <code>0</code> in this array is the
	 * minimum, and the element at index <code>1</code> - the maximum.
	 */
	private double[] rangeX;

	/** New value to be used for minimum x, as typed by the user. */
	private double xMin;
	/** New value to be used for maximum x, as typed by the user. */
	private double xMax;
	
	/**
	 * Initializes a new instance of <code>Points2DFilterDialog</code> based on the given
	 * <code>Points2D</code> instance.
	 * 
	 * @param owner The <code>Window</code> from which this dialog is displayed.
	 * @param title Title of the dialog.
	 * @param points Data points, based on which the ranges for the minimum and maximum coordinate values are
	 *        to be chosen.
	 * @param settings Visual settings for <code>points</code>.
	 */
	public Points2DFilterDialog(Window owner, String title, Points2D points, Points2DGroup settings) {
		super(owner, title);

		populate(points, settings);
		pack();
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		updateStatus();
	}

	@Override
	protected ComplexParamFilter createFilter() {
		try {
			final double xmin = formatter.parse(minTextField.getText()).doubleValue();
			final double xmax = formatter.parse(maxTextField.getText()).doubleValue();
			return new Points2DFilter(xmin, xmax);
		} catch (ParseException ex) {
			throw new InnerException(ex);
		}
	}

	/**
	 * @param points Data points, based on which the ranges for the minimum and maximum coordinate values are
	 *        to be chosen.
	 * @param settings Visual settings for <code>points</code>.
	 */
	private void populate(Points2D points, Points2DGroup settings) {
		rangeX = points.getRangeX();

		formatter.setParseIntegerOnly(false);
		formatter.setMaximumFractionDigits(12);

		// Add a text field for minimum x
		final JLabel minLabel = new JLabel(settings.filter.getMinXLabel() + ":", SwingConstants.RIGHT);
		minTextField = new JFormattedTextField(formatter);
		minTextField.setColumns(9);
		minTextField.setHorizontalAlignment(JFormattedTextField.RIGHT);
		minTextField.setValue(new Double(xMin = rangeX[0]));
		minTextField.addPropertyChangeListener("value", this);

		// Add a text field for maximum x
		final JLabel maxLabel = new JLabel(settings.filter.getMaxXLabel() + ":", SwingConstants.RIGHT);
		maxTextField = new JFormattedTextField(formatter);
		maxTextField.setColumns(9);
		maxTextField.setHorizontalAlignment(JFormattedTextField.RIGHT);
		maxTextField.setValue(new Double(xMax = rangeX[1]));
		maxTextField.addPropertyChangeListener("value", this);

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
						.addComponent(minTextField, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE)
						.addComponent(maxTextField, PREFERRED_SIZE, PREFERRED_SIZE, PREFERRED_SIZE)
				)
				.addGap(0, 0, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(minLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(minTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(maxLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(maxTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
		);
	}

	/**
	 * Updates the status of the OK button based on the values entered in the text fields of this dialog.
	 */
	private void updateStatus() {
		try {
			xMin = formatter.parse(minTextField.getText()).doubleValue();
			if (xMin < rangeX[0] || xMin > rangeX[1]) {
				xMin = Math.min(Math.max(xMin, rangeX[0]), rangeX[1]);
				minTextField.setText(String.valueOf(xMin));
				minTextField.commitEdit();
			}
		} catch (Exception ex) {
			xMin = Double.MAX_VALUE;
		}
		try {
			xMax = formatter.parse(maxTextField.getText()).doubleValue();
			if (xMax < rangeX[0] || xMax > rangeX[1]) {
				xMax = Math.min(Math.max(xMax, rangeX[0]), rangeX[1]);
				maxTextField.setText(String.valueOf(xMax));
				maxTextField.commitEdit();
			}
		} catch (Exception ex) {
			xMax = Double.MIN_VALUE;
		}
		btnOK.setEnabled(xMin <= xMax);
	}
}
