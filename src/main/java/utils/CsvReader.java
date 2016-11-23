package utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {
    private Logger log = LoggerFactory.getLogger(CsvReader.class);

    public Object[][] readCsv(String path) {
        try {
            File csv = new File(path);
            CSVParser parser = new CSVParser(new FileReader(csv), CSVFormat.DEFAULT);
            List<CSVRecord> list = parser.getRecords();
            int dataSize = list.size();
            int argumentsSize = 0;
            if (dataSize > 0) {
                argumentsSize = list.get(0).size();
            }
            Object[][] elements = new Object[dataSize][argumentsSize];
            for (int i = 0; i < dataSize; i++) {
                for (int j = 0; j < argumentsSize; j++) {
                    elements[i][j] = list.get(i).get(j);
                }
            }
            return elements;
        } catch (IOException io) {
            log.error("File not found. {}", io.getLocalizedMessage());
        }
        log.warn("Empty dataProvider will be set.");
        return new Object[][] {};
    }
}
