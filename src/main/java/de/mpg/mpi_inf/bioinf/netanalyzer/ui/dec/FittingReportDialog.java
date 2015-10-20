package de.mpg.mpi_inf.bioinf.netanalyzer.ui.dec;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.GroupLayout.Alignment.CENTER;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;

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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.cytoscape.util.swing.LookAndFeelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mpg.mpi_inf.bioinf.netanalyzer.data.Messages;
import de.mpg.mpi_inf.bioinf.netanalyzer.ui.Utils;

/**
 * Dialog window which reports the results after fitting a function.
 * 
 * @author Yassen Assenov
 */
@SuppressWarnings("serial")
public class FittingReportDialog extends JDialog {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FittingReportDialog.class);
	
	private JButton btnClose;

	/**
	 * Initializes the common controls of <code>FittingReportDialog</code>.
	 * 
	 * @param owner The <code>Window</code> from which this dialog is displayed.
	 * @param title Dialog's title.
	 * @param data Fit data to be displayed, encapsulated in a <code>FitData</code> instance.
	 */
	public FittingReportDialog(Window owner, String title, FitData data) {
		super(owner, title, ModalityType.APPLICATION_MODAL);
		initControls(data);
		setLocationRelativeTo(owner);
		setResizable(false);
	}

	private void initControls(final FitData data) {
		// Display informative message to the user
		final JLabel infoLabel = new JLabel(data.getMessage(), SwingConstants.CENTER);
		
		final JPanel formPanel = createFormPanel(data);
		
		// Add "Close" and "Help" buttons
		btnClose = Utils.createButton(new AbstractAction(Messages.DI_CLOSE) {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		}, null);
		
		final JPanel bottomPanel = LookAndFeelUtil.createOkCancelPanel(null, btnClose);
		
		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createParallelGroup(CENTER, true)
				.addComponent(infoLabel)
				.addComponent(formPanel, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(bottomPanel, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(infoLabel)
				.addComponent(formPanel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				.addComponent(bottomPanel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
		);
		
		LookAndFeelUtil.setDefaultOkCancelKeyStrokes(rootPane, btnClose.getAction(), btnClose.getAction());
		getRootPane().setDefaultButton(btnClose);
		
		pack();
	}
	
	private JPanel createFormPanel(final FitData data) {
		// Display coefficients of the fitted function
		final JLabel aLabel = new JLabel("a:");
		final JLabel bLabel = new JLabel("b:");
		final JTextField aTextField = createTextField(Utils.doubleToString(data.getCoefs().x, 6, 3));
		final JTextField bTextField = createTextField(Utils.doubleToString(data.getCoefs().y, 6, 3));
		
		final JPanel panel = new JPanel();
		panel.setBorder(LookAndFeelUtil.createPanelBorder());
		
		final GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		final ParallelGroup hGroup = layout.createParallelGroup(CENTER, true);
		final SequentialGroup vGroup = layout.createSequentialGroup();
		
		final ParallelGroup labelGroup = layout.createParallelGroup(TRAILING);
		final ParallelGroup fieldGroup = layout.createParallelGroup(LEADING);
		
		layout.setHorizontalGroup(hGroup
				.addGroup(layout.createSequentialGroup()
						.addGap(0, 0, Short.MAX_VALUE)
						.addGroup(labelGroup
								.addComponent(aLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
								.addComponent(bLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						)
						.addGroup(fieldGroup
								.addComponent(aTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
								.addComponent(bTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						)
						.addGap(0, 0, Short.MAX_VALUE)
				)
		);
		layout.setVerticalGroup(vGroup
				.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(aLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(aTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
				.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(bLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(bTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
				)
		);
		
		// Display goodness fit measures
		if (data.getCorrelation() != null || data.getRSquared() != null) {
			if (data.getCorrelation() != null) {
				final JLabel label = new JLabel(Messages.DI_CORR, SwingConstants.TRAILING);
				final JTextField textField = createTextField(Utils.doubleToString(data.getCorrelation(), 6, 3));

				labelGroup.addComponent(label, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
				fieldGroup.addComponent(textField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
				vGroup.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(label, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(textField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE));
			}

			if (data.getRSquared() != null) {
				final JLabel label = new JLabel(Messages.DI_RSQUARED, SwingConstants.TRAILING);
				final JTextField textField = createTextField(Utils.doubleToString(data.getRSquared(), 6, 3));

				labelGroup.addComponent(label, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
				fieldGroup.addComponent(textField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
				vGroup.addGroup(layout.createParallelGroup(CENTER, false)
						.addComponent(label, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
						.addComponent(textField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE));
			}
		}
		
		// Display a note
		final String note = data.getNote();
		
		if (note != null && !note.trim().isEmpty()) {
			final JLabel noteLabel = new JLabel("<html><b>Note:</b> " + note + "</html>");
			noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			hGroup.addComponent(noteLabel, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE);
			
			vGroup.addGap(20);
			vGroup.addComponent(noteLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
		}
		
		return panel;
	}

	private JTextField createTextField(final String value) {
		final JTextField textField = new JTextField(value, 9);
		textField.setEditable(false);
		textField.setHorizontalAlignment(JTextField.RIGHT);
		
		return textField;
	}
}
