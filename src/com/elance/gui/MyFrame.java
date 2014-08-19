package com.elance.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.elance.data.CompanyDataFileParser;
import com.elance.data.KeyValueData;
import com.elance.data.ReportTypeFileParser;
import com.elance.utils.PropertiesReader;
import com.elance.utils.ReportsController;
import com.elance.utils.Row;

public class MyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1254269072443504913L;

	private Box leftContainerComponents;
	private Box rightContainerComponents;

	private CompanyPanel companyPanel;
	private KeyWordsPanel keyWordsPanel;
	private FormatPanel formatPanel;
	private CheckBoxReportTypePanel reportTypePanel;
	private YearRangePanel yearRangePanel;
	private JPanel goButtonPanel;

	private FileChooserPanel inFileChooserPanel;
	private FileChooserPanel outFileChooserPanel;
	private ResultTablePanel resultTablePanel;

	private JButton goButton;

	private List<KeyValueData> companiesData;
	private List<String> reportTypes;

	private int frameWidth = 850;
	private int frameHeight = 720;
	
	public MyFrame() {

		setTitle("Reports");
		
		Toolkit kit=Toolkit.getDefaultToolkit();
		Dimension screen=kit.getScreenSize();
		
		setLocation(screen.width/2-frameWidth/2,screen.height/2-frameHeight/2);
		setSize(frameWidth, frameHeight);

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initData();
		initComponensts();

		addComponentsToLeftBoxContainer();

		Box parentContainer = Box.createHorizontalBox();
		parentContainer.add(leftContainerComponents);
		parentContainer.add(rightContainerComponents);
		add(parentContainer);

	}

	private void initData() {
		companiesData = CompanyDataFileParser.parseData();
		reportTypes = ReportTypeFileParser.parseData();
	}

	private void initComponensts() {

		leftContainerComponents = Box.createVerticalBox();
		rightContainerComponents = Box.createVerticalBox();

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
		reportTypePanel = new CheckBoxReportTypePanel(reportTypes);
		reportTypePanel.setBorder(new TitledBorder(ReportTypePanel.PANEL_NAME));

		inFileChooserPanel = new FileChooserPanel(
				PropertiesReader.INPUT_PROPERTY_NAME);
		inFileChooserPanel.setBorder(new TitledBorder("Input file"));

		outFileChooserPanel = new OutFileChooserPanel(
				PropertiesReader.OUTPUT_PROPERTY_NAME);
		outFileChooserPanel.setBorder(new TitledBorder("Output folder"));

		resultTablePanel = new ResultTablePanel();
		resultTablePanel.setBorder(new TitledBorder("Results"));
	}

	private void addComponentsToLeftBoxContainer() {
		leftContainerComponents.add(companyPanel);
		leftContainerComponents.add(keyWordsPanel);
		leftContainerComponents.add(formatPanel);
		leftContainerComponents.add(reportTypePanel);
		leftContainerComponents.add(yearRangePanel);
		leftContainerComponents.add(goButtonPanel);

		rightContainerComponents.add(inFileChooserPanel);
		rightContainerComponents.add(outFileChooserPanel);
		rightContainerComponents.add(resultTablePanel);

	}

	private Set<String> getCompaniesNames(List<KeyValueData> companies) {
		Set<String> res = new TreeSet<>();
		for (KeyValueData data : companies) {
			res.add(data.getKey());
		}

		return res;
	}

	private JButton createGoButton() {
		JButton goButton = new JButton("GO");

		goButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String company = companyPanel.getCompanyName();
				List<String> reportTypes = reportTypePanel
						.getSelectedReportTypes();
				List<String> years = yearRangePanel.getYears();
				String format = formatPanel.getFormat();

				checkInputs(company, reportTypes, years, format);
			}

			private void checkInputs(String company, List<String> reportTypes,
					List<String> years, String format) {
				if (reportTypes.size() > 0) {
					List<Row> foundReports = ReportsController.findReports(
							company, reportTypes, years, format);
					checkNoZeroListSizeResult(foundReports);
				} else {
					JOptionPane.showMessageDialog(null, "Check report type");
				}
			}

			private void checkNoZeroListSizeResult(List<Row> foundReports) {
				if (foundReports.size() > 0) {
					resultTablePanel.setData(foundReports);
					MyFrame.this.paintComponents(MyFrame.this.getGraphics());
				} else {
					JOptionPane.showMessageDialog(null, "Reports not found");
				}
			}
		});

		return goButton;
	}
}
