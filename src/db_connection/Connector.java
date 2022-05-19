package db_connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(
                    "jdbc:postgresql://netwave-server.postgres.database.azure.com:5432/postgres",
                    "postgres@netwave-server",
                    "Netwave2022@"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return null;
    }
}
