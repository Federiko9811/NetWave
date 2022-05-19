package Query;

import db_connection.Connector;

import java.sql.*;
import java.util.Scanner;

import static utils.ConsoleColors.makeGreen;
import static utils.ConsoleColors.makeRed;

public class Interrogazioni {

    /**
     * Nomi e Cognomi dei tecnici che hanno effettuato un'istallazione il primo giorno di lavoro
     */
    public static void query1() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select nome, cognome
                    from (dipendente
                        join tecnico t on dipendente.codice_fiscale = t.dipendente
                        join contratto c on dipendente.codice_fiscale = c.dipendente
                        join svolgimento_installazioni si on t.dipendente = si.tecnico) as temp
                    where temp.data_inizio in
                          (select data_inizio
                           from (lavoro
                               join installazione i on lavoro.codice = i.codice_lavoro
                               join svolgimento_installazioni s on i.codice_lavoro = s.installazione) as temp2
                           where temp2.tecnico = temp.codice_fiscale)""";
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non sono stati trovati tecnici che hanno effettuato installazioni il loro primo giorno di lavoro"));
            } else {
                System.out.println("+-----------------------+");
                while (rs.next()) {
                    System.out.println(rs.getString(1) + " " + rs.getString(2));
                    System.out.println("+-----------------------+");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Codice fiscale dei dipendenti che hanno effettuato un abbonamento con NetWave
     */
    public static void query2() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select cliente.codice_fiscale, cliente.email from cliente
                        join dipendente d on cliente.codice_fiscale = d.codice_fiscale
                        join abbonamento a on cliente.email = a.cliente
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non sono stati trovati dipendenti che hanno un abbonamento con NetWave"));
            } else {
                System.out.println("+-----------------------+");
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                    System.out.println(rs.getString(2));
                    System.out.println("+-----------------------+");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Numero di furgoni utilizzati nelle istallazioni di (mese e anno), utilizzati da tecnici
     */
    public static void query3() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;
        Scanner scan = new Scanner(System.in);

        try {
            System.out.println(makeGreen("Inserisci mese e anno nel formato yyyy-mm: "));
            String meseAnno = scan.nextLine();
            meseAnno += "-01";

            String sql = """
                    select count(targa) as numero_furgoni from mezzo_aziendale
                      join installazione i on mezzo_aziendale.targa = i.mezzo_aziendale
                      join lavoro l on l.codice = i.codice_lavoro
                      where tipo = 'Furgone'
                      and
                      extract(month from data_inizio) = extract(month from ?::date)
                      and
                      extract(year from data_inizio) = extract(year from ?::date)
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            ps.setString(1, meseAnno);
            ps.setString(2, meseAnno);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non sono stati utilizzati furgoni in quella data"));
            } else {
                System.out.println("+-----------------------+");
                while (rs.next()) {
                    System.out.println("Numero Furgoni: " + rs.getString(1));
                    System.out.println("+-----------------------+");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nome ed Email dei clienti che hanno effettuato un abbonamento il giorno di scadenza di una Tariffa
     */
    public static void query4() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;
        Scanner scan = new Scanner(System.in);

        try {
            String sql = """
                    select clienti_abbonati.nome, clienti_abbonati.email
                    from clienti_abbonati
                             join tariffa t on clienti_abbonati.tariffa = t.nome
                    where clienti_abbonati.data_adesione = t.data_fine;
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non sono trovati clienti"));
            } else {
                System.out.println("+-----------------------+");
                while (rs.next()) {
                    System.out.println("Nome: " + rs.getString(1));
                    System.out.println("Email: " + rs.getString(2));
                    System.out.println("+-----------------------+");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
