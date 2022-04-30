package Query;

import db_connection.Connector;

import java.sql.*;

public class Test {
    public static void testQuery() {
        Connection link = Connector.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM dipendente where nome=?";
            assert link != null;
            ps = link.prepareStatement(sql);
            ps.setString(1, "Federico");
            
            rs = ps.executeQuery();

            int colonne = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= colonne; i++) {
                    System.out.println(rs.getString(i));
                }
                System.out.println("------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
