package test.data;

import org.testng.annotations.DataProvider;
import utils.CsvReader;
import utils.XmlReader;
import utils.db.DbReader;

public class EmailTestDataProvider {
    @DataProvider(name = "simpleDataProvider")
    public static Object[][] getData() {
        return new Object[][] {
                {"daria-test2@tut.by", "11111111", "test subj", "test body", "daria-test3@yandex.ru", "123123qa"},
                {"daria-test3@yandex.ru", "123123qa", "test2 subj2", "test2 body2", "daria-test2@tut.by", "11111111"}};
    }

    @DataProvider(name = "xmlDataProvider")
    public static Object[][] getXmlData() {
        String filePath = "./src/test/resources/EmailTestData.xml";
        return new XmlReader().readXml(filePath);
    }

    @DataProvider(name = "dbDataProvider")
    public static Object[][] getDblData() {
        return new DbReader().readDbData("java_test");
    }

    @DataProvider(name = "csvDataProvider")
    public static Object[][] getCsvData() {
        return new CsvReader().readCsv("./src/test/resources/EmailTestData.csv");
    }
}
