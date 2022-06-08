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
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Nome:  " + rs.getString(1)));
                    System.out.println(makeGreen("Cognome: " + rs.getString(2)));
                    System.out.println(makeGreen("+-----------------------+"));
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
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("CF: " + rs.getString(1)));
                    System.out.println(makeGreen("Email: " + rs.getString(2)));
                    System.out.println(makeGreen("+-----------------------+"));
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
                    select count(targa) as numero_furgoni
                    from furgoni_aziendali f
                             join installazione i on f.targa = i.mezzo_aziendale
                             join lavoro l on l.codice = i.codice_lavoro
                    where extract(month from data_inizio) = extract(month from ?::date)
                      and extract(year from data_inizio) = extract(year from ?::date);
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            ps.setString(1, meseAnno);
            ps.setString(2, meseAnno);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non sono stati utilizzati furgoni in quella data"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Numero Furgoni: " + rs.getString(1)));
                    System.out.println(makeGreen("+-----------------------+"));
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
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Nome: " + rs.getString(1)));
                    System.out.println(makeGreen("Email: " + rs.getString(2)));
                    System.out.println(makeGreen("+-----------------------+"));
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
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Numero Sim: " + rs.getString(1)));
                    System.out.println(makeGreen("+-----------------------+"));
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

            System.out.println(makeGreen("+-----------------------+"));
            while (rs.next()) {
                System.out.println(makeGreen("Numero Installazioni: " + rs.getString(1)));
                System.out.println(makeGreen("+-----------------------+"));
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

            System.out.println(makeGreen("+-----------------------+"));
            while (rs.next()) {
                System.out.println(makeGreen("Nome Fornitore: " + rs.getString(1)));
                System.out.println(makeGreen("+-----------------------+"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Comune e via delle sedi che hanno almeno 2000 materiali di ogni tipo
     */
    public static void query8() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select c.nome, via
                    from area_operativa a join comune c on c.id = a.comune
                    where (select count(distinct tipo)
                           from materiale m,
                                disponibilita d
                           where m.id = d.materiale
                             and a.id = d.area_operativa
                             and d.quantita >= 2000) = (select count(distinct tipo)
                                                        from materiale)
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Comune : " + rs.getString(1)));
                    System.out.println(makeGreen("Via : " + rs.getString(2)));
                    System.out.println(makeGreen("+-----------------------+"));
                }
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

            System.out.println(makeGreen("+-----------------------+"));
            while (rs.next()) {
                System.out.println(makeGreen("Nome : " + rs.getString(1)));
                System.out.println(makeGreen("Cognome : " + rs.getString(2)));
                System.out.println(makeGreen("+-----------------------+"));
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

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Comune : " + rs.getString(1)));
                    System.out.println(makeGreen("Targa : " + rs.getString(2)));
                    System.out.println(makeGreen("+-----------------------+"));
                }
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

            System.out.println(makeGreen("+-----------------------+"));
            while (rs.next()) {
                System.out.println(makeGreen("Tecnici : " + rs.getString(1) + " €/anno"));
                System.out.println(makeGreen("Assistenti : " + rs.getString(2) + " €/anno"));
                System.out.println(makeGreen("+-----------------------+"));
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
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Cliente : " + rs.getString(1)));
                    System.out.println(makeGreen("Data Emissione : " + rs.getString(2)));
                    System.out.println(makeGreen("Costo : " + rs.getString(3) + " €"));
                    System.out.println(makeGreen("+-----------------------+"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Età media dei dipendenti che sono stati assunti nel 2019 con un contratto FullTime
     */
    public static void query13() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select avg(extract(year from current_date) - extract(year from data_nascita))
                    from dipendenti_assunti_FullTime_2019;
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    String s = String.format("%.1f", Float.parseFloat(rs.getString(1)));
                    System.out.println(makeGreen("Età media : " + s));
                    System.out.println(makeGreen("+-----------------------+"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nomi delle tariffe più economiche con durata di disponibilità maggiore
     */
    public static void query14() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select tariffa.nome
                    from tariffa
                    where (select (data_fine - tariffa.data_inizio) / costo_mensile) = (select max((data_fine-data_inizio)::double precision/costo_mensile)
                                                                                        from tariffa)
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Tariffa : " + rs.getString(1)));
                    System.out.println(makeGreen("+-----------------------+"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Email dei clienti con una spesa mensile degli abbonamenti più elevata
     */
    public static void query15() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select cliente
                    from abbonamento a
                             join tariffa t on a.tariffa = t.nome
                    group by cliente
                    having sum(costo_mensile) >= ALL (select sum(costo_mensile)
                                                      from abbonamento a
                                                               join tariffa t on a.tariffa = t.nome
                                                      group by cliente)
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Cliente : " + rs.getString(1)));
                    System.out.println(makeGreen("+-----------------------+"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comuni con sede operativa che hanno il minor numero di furgoni ma il maggior numero di tecnici
     */
    public static void query16() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select nome
                    from comune join area_operativa ao on comune.id = ao.comune
                    where ao.id in (select id
                                    from aree_operative_min_furgoni)
                    and ao.id in (select id
                                  from aree_operative_max_tecnici)
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Comune : " + rs.getString(1)));
                    System.out.println(makeGreen("+-----------------------+"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Aziende che hanno ricevuto assistenza da un dipendente il cui contratto era in scadenza (stesso mese della richiesta)
     */
    public static void query17() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select nome
                    from cliente join lavoro l on cliente.email = l.cliente join contratto on assistente = contratto.dipendente
                    where cliente.tipo = 'Azienda' and l.tipo_lavoro = 'Assistenza' and to_char(l.data_inizio, 'mon-yyyy') = to_char(contratto.data_fine, 'mon-yyyy')
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Nome : " + rs.getString(1)));
                    System.out.println(makeGreen("+-----------------------+"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Email dei tecnici che hanno effettuato il maggior numero d'installazioni e hanno un contratto valido da meno tempo
     */
    public static void query18() {
        Connection link = Connector.connect();
        PreparedStatement ps;
        ResultSet rs;

        try {
            String sql = """
                    select email
                    from tecnico t join dipendente d on t.dipendente = d.codice_fiscale
                    where (select count(*)
                        from svolgimento_installazioni si
                        where si.tecnico = t.dipendente) = (select max(installazioni)
                                                            from (select count(*) as installazioni
                                                                  from svolgimento_installazioni join tecnico t2 on svolgimento_installazioni.tecnico = t2.dipendente
                                                                  group by t2.dipendente) as installazioniDip)
                    and  current_date - (select max(data_inizio)
                          from contratto c
                          where t.dipendente = c.dipendente) <= ALL (select current_date - max(data_inizio)
                                                                     from contratto join tecnico t3 on contratto.dipendente = t3.dipendente
                                                                     group by t3.dipendente);
                    """;
            assert link != null;
            ps = link.prepareStatement(sql);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println(makeRed("Non ci sono risultati per questa query"));
            } else {
                System.out.println(makeGreen("+-----------------------+"));
                while (rs.next()) {
                    System.out.println(makeGreen("Email : " + rs.getString(1)));
                    System.out.println(makeGreen("+-----------------------+"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
