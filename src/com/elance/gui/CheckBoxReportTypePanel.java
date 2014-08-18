package com.elance.gui;


import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 19.08.14
 * Time: 1:29
 */
public class CheckBoxReportTypePanel extends JPanel {
    private List<JCheckBox> checkBoxes = new ArrayList<>();

    public CheckBoxReportTypePanel(List<String> reportTypes) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (String reportType : reportTypes) {
            JCheckBox checkBox = new JCheckBox(reportType);
            add(checkBox);
            checkBoxes.add(checkBox);
        }
    }

    public List<String> getSelectedReportTypes() {
        List<String> selectedReportTypes = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected())
                selectedReportTypes.add(checkBox.getText());
        }
        return selectedReportTypes;
    }
}
