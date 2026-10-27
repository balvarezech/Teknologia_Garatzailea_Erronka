package com.erronka.basic;

import com.erronka.common.UITheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * TicTacToeView: Hiru Lerroan jokoaren interfaze grafikoa Swing bidez.
 * MVC arkitekturaren bista: ez du joko-logikarik exekutatzen, kontrolatzailearen bidez kudeatzen da.
 */
public class TicTacToeView extends JFrame {

    private final JButton[][] gelaxkak;
    private final JLabel etiketaEgoera;
    private final JComboBox<TicTacToeAI.ZailtasunMaila> hautatzaileZailtasuna;
    private final JButton botoiaAholkua;
    private final JButton botoiaBerrezarri;

    public TicTacToeView() {
        super("1. Ariketa: Hiru Lerroan (Tic-Tac-Toe) + Minimax IA");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 560);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.ATZEALDE_NAGUSIA);
        setLayout(new BorderLayout(10, 10));

        // 1. GOIKO PANELA: Zailtasuna eta Aholku Adimendunaren botoia
        JPanel goikoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        goikoPanela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        goikoPanela.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JLabel etiketaZailtasuna = new JLabel("Zailtasuna:");
        etiketaZailtasuna.setForeground(UITheme.TESTU_ZURIA);
        etiketaZailtasuna.setFont(UITheme.LETRA_ARRUNTA);

        hautatzaileZailtasuna = new JComboBox<>(TicTacToeAI.ZailtasunMaila.values());
        hautatzaileZailtasuna.setSelectedItem(TicTacToeAI.ZailtasunMaila.GARAIEZINA);
        hautatzaileZailtasuna.setBackground(UITheme.PANEL_ATZEALDEA);
        hautatzaileZailtasuna.setForeground(UITheme.TESTU_ZURIA);
        hautatzaileZailtasuna.setFont(UITheme.LETRA_ARRUNTA);

        botoiaAholkua = new JButton("💡 Aholkua (IA)");
        UITheme.estilizatuBotoia(botoiaAholkua, UITheme.MORE_ARGIA);

        goikoPanela.add(etiketaZailtasuna);
        goikoPanela.add(hautatzaileZailtasuna);
        goikoPanela.add(botoiaAholkua);
        add(goikoPanela, BorderLayout.NORTH);

        // 2. ERDIKO PANELA: 3x3 Gelaxken Sareta
        JPanel saretaPanela = new JPanel(new GridLayout(3, 3, 8, 8));
        saretaPanela.setBackground(UITheme.ATZEALDE_NAGUSIA);
        saretaPanela.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        gelaxkak = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton botoia = new JButton("");
                botoia.setFont(UITheme.LETRA_JOKOA_IKURRA);
                botoia.setFocusPainted(false);
                botoia.setBackground(UITheme.TXARTEL_ATZEALDEA);
                botoia.setForeground(UITheme.TESTU_ZURIA);
                botoia.setBorder(BorderFactory.createLineBorder(UITheme.PANEL_ATZEALDEA, 2));
                botoia.setPreferredSize(new Dimension(100, 100));
                gelaxkak[i][j] = botoia;
                saretaPanela.add(botoia);
            }
        }
        add(saretaPanela, BorderLayout.CENTER);

        // 3. BEHEKO PANELA: Egoera eta Berrezarri botoia
        JPanel behekoPanela = new JPanel(new BorderLayout(8, 8));
        behekoPanela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        behekoPanela.setBorder(BorderFactory.createEmptyBorder(10, 16, 12, 16));

        etiketaEgoera = new JLabel("Zure txanda da (X)", SwingConstants.CENTER);
        etiketaEgoera.setForeground(UITheme.ZIAN_DISTIRA);
        etiketaEgoera.setFont(UITheme.LETRA_AZPITITULUA);

        botoiaBerrezarri = new JButton("🔄 Partida Berria");
        UITheme.estilizatuBotoia(botoiaBerrezarri, UITheme.PANEL_ATZEALDEA);

        behekoPanela.add(etiketaEgoera, BorderLayout.CENTER);
        behekoPanela.add(botoiaBerrezarri, BorderLayout.EAST);
        add(behekoPanela, BorderLayout.SOUTH);
    }

    /**
     * Gelaxka bateko testua eta kolorea eguneratzen ditu.
     */
    public void ezarriGelaxkaIkurra(int r, int c, char ikurra) {
        JButton b = gelaxkak[r][c];
        b.setText(String.valueOf(ikurra));
        if (ikurra == TicTacToeModel.JOKALARI_X) {
            b.setForeground(UITheme.ZIAN_DISTIRA);
        } else if (ikurra == TicTacToeModel.JOKALARI_O) {
            b.setForeground(UITheme.GORRI_BIZIA);
        } else {
            b.setForeground(UITheme.TESTU_ZURIA);
            b.setBackground(UITheme.TXARTEL_ATZEALDEA);
        }
    }

    /**
     * Aholku adimenduna bistaratzen du gomendatutako gelaxka horiz argituz denbora batez.
     */
    public void nabarmenduAholkua(int r, int c) {
        JButton b = gelaxkak[r][c];
        Color jatorrizkoKolorea = b.getBackground();
        b.setBackground(UITheme.AMBAR_HORIA);

        // 1.2 segundoren ostean berreskuratu jatorrizko kolorea
        Timer tenporizadorea = new Timer(1200, evt -> b.setBackground(jatorrizkoKolorea));
        tenporizadorea.setRepeats(false);
        tenporizadorea.start();
    }

    public void ezarriEgoeraTestua(String mezua, Color kolorea) {
        etiketaEgoera.setText(mezua);
        etiketaEgoera.setForeground(kolorea);
    }

    public void gaituGelaxkak(boolean gaituta) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gelaxkak[i][j].setEnabled(gaituta);
            }
        }
    }

    // Ekintzen entzuleak erregistratzeko metodoak
    public void gehituGelaxkaEntzulea(int r, int c, ActionListener entzulea) {
        gelaxkak[r][c].addActionListener(entzulea);
    }

    public void gehituAholkuEntzulea(ActionListener entzulea) {
        botoiaAholkua.addActionListener(entzulea);
    }

    public void gehituBerrezarriEntzulea(ActionListener entzulea) {
        botoiaBerrezarri.addActionListener(entzulea);
    }

    public TicTacToeAI.ZailtasunMaila getHautatutakoZailtasuna() {
        return (TicTacToeAI.ZailtasunMaila) hautatzaileZailtasuna.getSelectedItem();
    }
}
