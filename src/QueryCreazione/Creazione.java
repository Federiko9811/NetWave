package QueryCreazione;

import db_connection.Connector;
import utils.InformazioniTabella;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Creazione {

    /**
     * Ricava nomi e tipi delle colonne dal nome della tabella passata in input
     * @param nomeTabella
     * @return informazioni sulla tabella
     */
    public static InformazioniTabella getMetaDataTabella(String nomeTabella) {
        Connection link = Connector.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        InformazioniTabella informazioniTabella = new InformazioniTabella();
        informazioniTabella.setNomeTabella(nomeTabella.toLowerCase());

        try {
            String sql = "select * from " + nomeTabella.toLowerCase() + " limit 0";
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();

            List<String> listaNomiColonne = new ArrayList<>();
            List<String> listaTipiColonne = new ArrayList<>();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                listaNomiColonne.add(metaData.getColumnName(i));
                listaTipiColonne.add(metaData.getColumnTypeName(i));
            }

            informazioniTabella.setNomiColonne(listaNomiColonne);
            informazioniTabella.setTipiColonne(listaTipiColonne);

            return informazioniTabella;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return informazioniTabella;
    }

    /**
     * Dalle informazioni passate in input crea la query sql
     * Effettua un cast su tutti i tipi anche se servirebbe farlo solo su alcuni ma si
     * evitano gli if
     *
     * @param informazioniTabella
     * @return sql query
     */
    private static String creaInsertString(InformazioniTabella informazioniTabella) {
        StringBuilder s = new StringBuilder("insert into " + informazioniTabella.getNomeTabella() + " (");

        int numeroColonne = informazioniTabella.getNomiColonne().size();

        for (int i = 0; i < numeroColonne; i++) {
            if (i == numeroColonne - 1) {
                s.append(informazioniTabella.getNomiColonne().get(i)).append(") ");
            } else {
                s.append(informazioniTabella.getNomiColonne().get(i)).append(", ");
            }
        }

        s.append("values (");

        for (int i = 0; i < numeroColonne; i++) {
            String tipo = informazioniTabella.getTipiColonne().get(i);
            if (i == numeroColonne - 1) {
                s.append("?").append("::").append(tipo).append(")");
            } else {
                s.append("?").append("::").append(tipo).append(", ");
            }
        }

        return s.toString();
    }

    public static void inserimento(InformazioniTabella informazioniTabella) {
        Scanner scan = new Scanner(System.in);
        Connection link = Connector.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = creaInsertString(informazioniTabella);
            System.out.println(sql);
            assert link != null;
            ps = link.prepareStatement(sql);

            int numeroColonne = informazioniTabella.getNomiColonne().size();

            for (int i = 0; i < numeroColonne; i++) {
                System.out.println(informazioniTabella.getNomiColonne().get(i) + ": ");
                String value = scan.nextLine();
                if (value.equals("null")) {
                    ps.setNull(i + 1, Types.NULL);
                } else {
                    ps.setString(i + 1, value);
                }
            }

            ps.execute();
            System.out.println(informazioniTabella.getNomeTabella() +" inserito con successo");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
