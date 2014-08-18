package com.elance.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.elance.utils.Row;


public class ResultTablePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8555091637741108014L;

	private static final String[] COLUMN_NAMES = {"company name", "report type", "year", "URL"};
	
	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;
	
	
	public ResultTablePanel() {
		setLayout(new BorderLayout());

		initJTable(new Object[][]{});
	}

	private void initJTable(Object[][] data){
		if(scrollPane != null){
			remove(scrollPane);
		}
		tableModel = new DefaultTableModel(data, COLUMN_NAMES);
		table = new JTable(tableModel);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
	}
	
	private Object[][] listToObjectArray(List<Row> list){
		Object[][] result = new Object[list.size()][];
		
		for (int i = 0; i < list.size(); i++) {
			Row row = list.get(i);
			result[i][0] = row.getCompanyName();
			result[i][1] = row.getSourceType();
			result[i][2] = row.getYear();
			result[i][3] = row.getUrl();
		}
		
		return result;
	}
	
	public void setData(List<Row> data){
		initJTable(listToObjectArray(data));
	}
}
