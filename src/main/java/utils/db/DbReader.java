package utils.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.data.Row;

import java.util.List;

public class DbReader {
    private Logger log = LoggerFactory.getLogger(DbReader.class);

    public Object[][] readDbData(String tableName) {
        Connection connection = DbConnector.getConnection();
        String query = String.format("SELECT * FROM %s", tableName);
        List<Row> rowList = connection.createQuery(query).executeAndFetchTable().rows();
        int dataSize = rowList.size();
        int argumentsSize = rowList.get(0).asMap().size();
        Object[][] elements = new Object[dataSize][argumentsSize];
        log.debug("Db DataProvider returned [{}][{}]", dataSize, argumentsSize);
        for (int i = 0; i < dataSize; i++) {
            Row row = rowList.get(i);
            for (int j = 0; j < argumentsSize; j++) {
                elements[i][j] = row.getString(j);
            }
        }
        return elements;
    }
}
