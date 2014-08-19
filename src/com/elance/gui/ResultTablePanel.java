package com.elance.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.elance.utils.Row;

public class ResultTablePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8555091637741108014L;

	private static final String[] COLUMN_NAMES = { "company name",
			"report type", "year", "URL" };
	private static final int MIN_COLUMN_WIDTH = 110; 

	private DefaultTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;

	public ResultTablePanel() {
		setLayout(new BorderLayout());

		initJTable(new Object[][] {});
	}

	private void initJTable(Object[][] data) {
		if (scrollPane != null) {
			remove(scrollPane);
		}
		tableModel = new DefaultTableModel(data, COLUMN_NAMES);
		table = new JTable(tableModel);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		for (int column = 0; column < table.getColumnCount(); column++) {
			TableColumn tableColumn = table.getColumnModel().getColumn(column);
			int preferredWidth = tableColumn.getMinWidth();
			int maxWidth = tableColumn.getMaxWidth();

			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer cellRenderer = table.getCellRenderer(row,
						column);
				Component c = table.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width
						+ table.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);

				if (preferredWidth >= maxWidth) {
					preferredWidth = maxWidth;
					break;
				}
			}

			if(data.length < 1){
				preferredWidth = (preferredWidth < MIN_COLUMN_WIDTH) ? MIN_COLUMN_WIDTH : preferredWidth;
			}
			tableColumn.setPreferredWidth(preferredWidth);
		}

		scrollPane = new JScrollPane(table);
		add(scrollPane);
	}

	private Object[][] listToObjectArray(List<Row> list) {
		Object[][] result = new Object[list.size()][];

		for (int i = 0; i < list.size(); i++) {
			result[i] = new Object[4];
			Row row = list.get(i);
			result[i][0] = row.getCompanyName();
			result[i][1] = row.getSourceType();
			result[i][2] = row.getYear();
			result[i][3] = row.getUrl();
		}

		return result;
	}

	public void setData(List<Row> data) {
		initJTable(listToObjectArray(data));
	}
}
