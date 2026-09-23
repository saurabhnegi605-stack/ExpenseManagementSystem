package org.expense.util;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String userName = "root";

    private static final String password = "S@ur@bh_9012";

    private static final String url = "jdbc:mysql://localhost:3306/expense_manager";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url,userName,password) ;
    }
}
