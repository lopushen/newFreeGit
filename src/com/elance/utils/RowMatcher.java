package com.elance.utils;


import java.util.ArrayList;
import java.util.List;

public class RowMatcher {
    public List<Row> findMatchingRows(List<Row> inputRows) {
        List<Row> matchedRows = new ArrayList<>();
        CsvParser csvParser = new CsvParser();
        List<Row> parsedRows = csvParser.parseRows(PropertiesReader.getInstance().getProperty("database"));
        for (Row inputRow:inputRows)
            for (Row parsedRow:parsedRows) {
                if (match(inputRow, parsedRow)) {
                    matchedRows.add(parsedRow);
                }
            }
        return matchedRows;
    }

    private boolean match(Row inputRow, Row parsedRow) {
        return inputRow.getCompanyName().equalsIgnoreCase(parsedRow.getCompanyName()) &&
                parsedRow.getUrl().endsWith(inputRow.getFormat().toLowerCase()) &&
                inputRow.getSourceType().equalsIgnoreCase(parsedRow.getSourceType()) &&
                inputRow.getYear().equalsIgnoreCase(inputRow.getYear());
    }
}
