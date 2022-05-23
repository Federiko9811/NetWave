package QueryCreazione;

import db_connection.Connector;
import exceptions.TabellaNonTrovataException;
import utils.ConsoleColors;
import utils.InformazioniTabella;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static utils.ConsoleColors.makeRed;

public class Creazione {

    /**
     * Ricava nomi e tipi delle colonne dal nome della tabella passata in input
     *
     * @return informazioni sulla tabella
     */
    public static InformazioniTabella getMetaDataTabella(String nomeTabella) throws TabellaNonTrovataException {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

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
                if (metaData.getColumnTypeName(i).equals("serial")) {
                    continue;
                }
                listaNomiColonne.add(metaData.getColumnName(i));
                listaTipiColonne.add(metaData.getColumnTypeName(i));
            }

            informazioniTabella.setNomiColonne(listaNomiColonne);
            informazioniTabella.setTipiColonne(listaTipiColonne);

            return informazioniTabella;

        } catch (SQLException e) {
            throw new TabellaNonTrovataException();
        }
    }

    /**
     * Dalle informazioni passate in input crea la query sql
     * Effettua un cast su tutti i tipi anche se servirebbe farlo solo su alcuni ma si
     * evitano gli if
     *
     * @return sql query
     */
    private static List<String> creaInsertString(InformazioniTabella informazioniTabella) {
        StringBuilder s = new StringBuilder("insert into " + informazioniTabella.getNomeTabella() + " (");
        StringBuilder s2 = new StringBuilder("insert into " + informazioniTabella.getNomeTabella() + " (");

        int numeroColonne = informazioniTabella.getNomiColonne().size();

        for (int i = 0; i < numeroColonne; i++) {
            if (i == numeroColonne - 1) {
                s.append(informazioniTabella.getNomiColonne().get(i)).append(") ");
                s2.append(informazioniTabella.getNomiColonne().get(i)).append(") ");
            } else {
                s.append(informazioniTabella.getNomiColonne().get(i)).append(", ");
                s2.append(informazioniTabella.getNomiColonne().get(i)).append(", ");
            }
        }

        s.append("values (");
        s2.append("values (");

        for (int i = 0; i < numeroColonne; i++) {
            String tipo = informazioniTabella.getTipiColonne().get(i);
            if (i == numeroColonne - 1) {
                s.append("?").append("::").append(tipo).append(")");
                s2.append(tipo).append(")");
            } else {
                s.append("?").append("::").append(tipo).append(", ");
                s2.append(tipo).append(", ");
            }
        }

        return Arrays.asList(s.toString(), s2.toString());
    }

    /**
     * Inserisce all'interno del database il record
     */
    public static void inserimento(InformazioniTabella informazioniTabella) {
        Scanner scan = new Scanner(System.in);
        Connection link = Connector.connect();
        PreparedStatement ps;

        try {
            List<String> listaQuery = creaInsertString(informazioniTabella);
            String sql = listaQuery.get(0);
            System.out.println(ConsoleColors.RED_BOLD + "Query: " + listaQuery.get(1) + ConsoleColors.RESET);
//            System.out.println(ConsoleColors.RED_BOLD + "Query: " + listaQuery.get(0) + ConsoleColors.RESET);
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

            String nomeTabellaCap = informazioniTabella.getNomeTabella()
                    .substring(0, 1)
                    .toUpperCase() + informazioniTabella.getNomeTabella().substring(1);

            System.out.println(ConsoleColors.GREEN_BOLD + nomeTabellaCap + " inserito con successo");
            System.out.println("+---------------------------------------+" + ConsoleColors.RESET);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}