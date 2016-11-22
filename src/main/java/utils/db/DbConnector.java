package utils.db;


import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private static Logger log = LoggerFactory.getLogger(DbConnector.class);
    private static Connection connection;
    private final static String CONNECTION_STRING = "jdbc:mysql://127.0.0.1:3306/rum_db_new";
    private final static String USER = "root";
    private final static String PASSWORD = "";

    static {
        registerDriver();
        Sql2o connectionDao = new Sql2o(CONNECTION_STRING, USER, PASSWORD);
        connection = connectionDao.open();
        log.info("Connected to database @ {}", CONNECTION_STRING);
    }

    private DbConnector() {
        //disabled constructor
    }

    private static void registerDriver() {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            log.error("Failed to register My SQL driver.");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
