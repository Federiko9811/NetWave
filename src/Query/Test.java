package Query;

import db_connection.Connector;
import entità.Dipendente;

import java.sql.*;

public class Test {
    public static void testQuery() {
        Connection link = Connector.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM dipendente";
            assert link != null;
            ps = link.prepareStatement(sql);

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

    public static void inserisciDipendente(Dipendente dipendente) {
        Connection link = Connector.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "insert into " +
                    "dipendente (codice_fiscale, nome, cognome, data_nascita, email, telefono, iban, documento) " +
                    "values (?, ?, ?, ?::date, ?, ?, ?, ?::bytea)";
            assert link != null;
            ps = link.prepareStatement(sql);
            ps.setString(1, dipendente.getCodiceFiscale());
            ps.setString(2, dipendente.getNome());
            ps.setString(3, dipendente.getCognome());
            ps.setString(4, dipendente.getDataNascita());
            ps.setString(5, dipendente.getEmail());
            ps.setString(6, dipendente.getTelefono());
            ps.setString(7, dipendente.getIban());
            ps.setString(8, dipendente.getDocumento());

            ps.execute();
            System.out.println("Dipendente inserito con successo");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
