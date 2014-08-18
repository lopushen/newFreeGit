package com.elance.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.elance.data.CompanyDataFileParser;
import com.elance.data.KeyValueData;
import com.elance.data.ReportTypeFileParser;

public class MyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1254269072443504913L;
	
	private Box componentsContainer;
	
	private CompanyPanel companyPanel;
	private KeyWordsPanel keyWordsPanel;
	private FormatPanel formatPanel;
	private ReportTypePanel reportTypePanel;
	private YearRangePanel yearRangePanel;
	private JPanel goButtonPanel;

	private JButton goButton;
	
	private List<KeyValueData> companiesData;
	private List<String> reportTypes;
	
	public MyFrame() {

		setSize(400, 720);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initData();
		initComponensts();
		addComponentsToBoxContainer();

		add(componentsContainer);
	}
	
	private void initData(){
		companiesData = CompanyDataFileParser.parseData();
		reportTypes = ReportTypeFileParser.parseData();
	}
	
	private void initComponensts(){
		
		componentsContainer = Box.createVerticalBox();

		goButton = createGoButton();
		goButton.setPreferredSize(new Dimension(380, 30));
		goButtonPanel = new JPanel();
		goButtonPanel.add(goButton);
		
		companyPanel = new CompanyPanel(getCompaniesNames(companiesData));
		companyPanel.setBorder(new TitledBorder(CompanyPanel.PANEL_NAME));
		keyWordsPanel = new KeyWordsPanel();
		keyWordsPanel.setBorder(new TitledBorder(KeyWordsPanel.PANEL_NAME));
		formatPanel = new FormatPanel();
		formatPanel.setBorder(new TitledBorder(FormatPanel.PANEL_NAME));
		yearRangePanel = new YearRangePanel();
		yearRangePanel.setBorder(new TitledBorder(YearRangePanel.PANEL_NAME));
		reportTypePanel = new ReportTypePanel(reportTypes);
		reportTypePanel.setBorder(new TitledBorder(ReportTypePanel.PANEL_NAME));
	}
	
	private void addComponentsToBoxContainer(){
		componentsContainer.add(companyPanel);
		componentsContainer.add(keyWordsPanel);
		componentsContainer.add(formatPanel);
		componentsContainer.add(reportTypePanel);
		componentsContainer.add(yearRangePanel);
		componentsContainer.add(goButtonPanel);
	}
	
	private Set<String> getCompaniesNames(List<KeyValueData> companies){
		Set<String> res = new TreeSet<>();
		for (KeyValueData data : companies) {
			res.add(data.getKey());
		}
		
		return res;
	}
	
	private JButton createGoButton(){
		JButton goButton = new JButton("GO");
		goButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		return goButton;
	}
}
