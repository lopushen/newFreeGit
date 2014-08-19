package com.elance.gui;

import javax.swing.JFileChooser;

public class OutFileChooserPanel extends FileChooserPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7028083678144074246L;
	
	public OutFileChooserPanel(String propertyName){
		super(propertyName);
	}
	
	protected void initFileChooser(){
		fileChooser = new JFileChooser(".");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
}
