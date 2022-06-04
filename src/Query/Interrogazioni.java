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
                    select distinct cliente.codice_fiscale, cliente.email from cliente
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

    /**
     * Numero di Sim in possesso da clienti che hanno sia un abbonamento con tariffa fissa che mobile
     */
    public static void query5() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select count(*) as numero_sim
                    from cliente
                             join sim s on cliente.email = s.cliente
                    where email in
                          (select clienti_linea_fissa.email from clienti_linea_fissa)
                      and email in (select clienti_linea_mobile.email from clienti_linea_mobile);
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non sono trovate sim in possesso da clienti con abbonamenti sia mobili che fissi"));
            } else {
                System.out.println("+-----------------------+");
                while (rs.next()) {
                    System.out.println("Numero Sim: " + rs.getString(1));
                    System.out.println("+-----------------------+");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Numero di Installazioni che hanno coinvolto un'azienda collaboratrice ma che non hanno necessitato di un furgone
     */
    public static void query6() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select count(*) as numero_installazioni
                    from partecipazione
                             join installazione i on i.codice_lavoro = partecipazione.installazione
                             join mezzo_aziendale ma on ma.targa = i.mezzo_aziendale
                    where ma.tipo != 'Furgone';
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();


            System.out.println("+-----------------------+");
            while (rs.next()) {
                if (rs.getString(1).equals("0")) {
                    System.out.println(makeRed("Non sono state trovate installazioni che non hanno necessitato di furgoni"));
                } else {
                    System.out.println("Numero Installazioni: " + rs.getString(1));
                }
                System.out.println("+-----------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nomi dei fornitori che hanno effettuato il maggior numero di consegne
     */
    public static void query7() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select nome
                    from fornitore
                             join fornitura f on fornitore.partita_iva = f.fornitore
                    group by nome
                    having count(*) = (select max(numero_forniture)
                                       from (select count(*) as numero_forniture, nome
                                             from fornitore
                                                      join fornitura f on fornitore.partita_iva = f.fornitore
                                             group by nome) as consegne);
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("+-----------------------+");
            while (rs.next()) {
                System.out.println("Nome Fornitore: " + rs.getString(1));
                System.out.println("+-----------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nomi e Cognomi di Assistenti che non hanno partecipato a nessun lavoro nell'ultimo mese
     */
    public static void query9() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select nome, cognome
                    from assistente_clienti
                             join dipendente d on d.codice_fiscale = assistente_clienti.dipendente
                    where d.codice_fiscale not in (select * from lavoratori_ultimo_mese_assistenti);
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("+-----------------------+");
            while (rs.next()) {
                System.out.println("Nome : " + rs.getString(1));
                System.out.println("Cognome : " + rs.getString(2));
                System.out.println("+-----------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Targhe e Comuni delle Sedi delle Automobili con il maggior numero di chilometri registrati
     */
    public static void query10() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select c.nome, targa
                    from mezzo_aziendale
                             join area_operativa ao on ao.id = mezzo_aziendale.area_operativa
                             join comune c on c.id = ao.comune
                             join chilometraggio c2 on mezzo_aziendale.targa = c2.mezzo_aziendale
                    where chilometri = (select max(chilometri) as max_chilometri
                                        from chilometraggio)
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("+-----------------------+");
            while (rs.next()) {
                System.out.println("Comune : " + rs.getString(1));
                System.out.println("Targa : " + rs.getString(2));
                System.out.println("+-----------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Media degli stipendi per ogni categoria di dipendente
     */
    public static void query11() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select retribuzionemediatecnici, retribuzionemediaassistenti
                    from stipendioMedioTecnici, stipendioMedioAssistenti
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("+-----------------------+");
            while (rs.next()) {
                System.out.println("Tecnici : " + rs.getString(1) + " €/anno");
                System.out.println("Assistenti : " + rs.getString(2) + " €/anno");
                System.out.println("+-----------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fatture e clienti delle installazioni a cui hanno partecipato più di due aziende collaboratrici e il cui cliente dispone del solo abbonamento fisso
     */
    public static void query12() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select l.cliente, fattura.data_emissione, fattura.costo_lavoro
                    from fattura
                             join lavoro l on l.codice = fattura.installazione
                    where installazione in (select l.codice
                                            from lavoro l
                                                     join cliente c on c.email = l.cliente
                                            where c.email in (select email
                                                              from clienti_linea_fissa)
                                              and c.email not in (select email
                                                                  from clienti_linea_mobile)
                                              and l.codice in (select installazione
                                                               from installazione
                                                                        join partecipazione p on installazione.codice_lavoro = p.installazione
                                                               group by installazione
                                                               having count(azienda_collaboratrice) > 2))
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println("+-----------------------+");
                while (rs.next()) {
                    System.out.println("Cliente : " + rs.getString(1));
                    System.out.println("Data Emissione : " + rs.getString(2));
                    System.out.println("Costo : " + rs.getString(3) + " €");
                    System.out.println("+-----------------------+");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
