package com.erronka.intermediate;

import com.erronka.common.UITheme;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Connect4View: Konektatu 4 jokoaren interfaze grafikoa Swing bidez.
 * 6x7 taula zulatua kalitate grafiko handiz marrazten du eta fitxa irabazleak nabarmentzen ditu.
 */
public class Connect4View extends JFrame {

    private final JButton[] botoiakZutabeak;
    private final JLabel etiketaEgoera;
    private final JButton botoiaBerrezarri;
    private final TaulaPanela taulaPanela;

    private final Connect4Model eredua;

    public Connect4View(Connect4Model eredua) {
        super("2. Ariketa: Konektatu 4 (Connect 4) + Bot Heuristikoa");
        this.eredua = eredua;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(580, 620);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.ATZEALDE_NAGUSIA);
        setLayout(new BorderLayout(8, 8));

        // 1. GOIKO PANELA: 7 Zutabeen jaitsiera-botoiak (↓)
        JPanel goikoPanela = new JPanel(new GridLayout(1, 7, 6, 0));
        goikoPanela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        goikoPanela.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        botoiakZutabeak = new JButton[7];
        for (int c = 0; c < 7; c++) {
            JButton b = new JButton("↓ " + (c + 1));
            b.setFont(new Font("Segoe UI", Font.BOLD, 12));
            UITheme.estilizatuBotoia(b, UITheme.PANEL_ATZEALDEA);
            botoiakZutabeak[c] = b;
            goikoPanela.add(b);
        }
        add(goikoPanela, BorderLayout.NORTH);

        // 2. ERDIKO PANELA: Taula pertsonalizatua (zulatu klasikoa)
        taulaPanela = new TaulaPanela();
        add(taulaPanela, BorderLayout.CENTER);

        // 3. BEHEKO PANELA: Egoera eta Berrezarri botoia
        JPanel behekoPanela = new JPanel(new BorderLayout(10, 10));
        behekoPanela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        behekoPanela.setBorder(BorderFactory.createEmptyBorder(10, 20, 12, 20));

        etiketaEgoera = new JLabel("Zure txanda da (Fitxa Gorriak)", SwingConstants.CENTER);
        etiketaEgoera.setForeground(UITheme.GORRI_BIZIA);
        etiketaEgoera.setFont(UITheme.LETRA_AZPITITULUA);

        botoiaBerrezarri = new JButton("🔄 Partida Berria");
        UITheme.estilizatuBotoia(botoiaBerrezarri, UITheme.PANEL_ATZEALDEA);

        behekoPanela.add(etiketaEgoera, BorderLayout.CENTER);
        behekoPanela.add(botoiaBerrezarri, BorderLayout.EAST);
        add(behekoPanela, BorderLayout.SOUTH);
    }

    /**
     * Taula pertsonalizatua marrazten duen barne-panela.
     */
    private class TaulaPanela extends JPanel {

        public TaulaPanela() {
            setBackground(UITheme.ATZEALDE_NAGUSIA);
            setPreferredSize(new Dimension(540, 460));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int zabalera = getWidth();
            int altuera = getHeight();

            int marjinaX = 20;
            int marjinaY = 10;
            int taulaZabalera = zabalera - (marjinaX * 2);
            int taulaAltuera = altuera - (marjinaY * 2);

            // Taularen egitura urdina marraztu
            Color taulaUrdina = new Color(30, 58, 138); // #1E3A8A (Urdin ilun sendoa)
            g2.setColor(taulaUrdina);
            g2.fillRoundRect(marjinaX, marjinaY, taulaZabalera, taulaAltuera, 20, 20);

            int gelaxkaZab = taulaZabalera / 7;
            int gelaxkaAlt = taulaAltuera / 6;
            int diametroa = Math.min(gelaxkaZab, gelaxkaAlt) - 14;

            List<Point> irabazleak = eredua.getIrabazlePosizioak();

            // Zuloak eta fitxak marraztu
            for (int r = 0; r < 6; r++) {
                for (int c = 0; c < 7; c++) {
                    int x = marjinaX + (c * gelaxkaZab) + (gelaxkaZab - diametroa) / 2;
                    int y = marjinaY + (r * gelaxkaAlt) + (gelaxkaAlt - diametroa) / 2;

                    int balioa = eredua.getGelaxka(r, c);

                    if (balioa == Connect4Model.JOKALARI_GIZAKIA) {
                        g2.setColor(UITheme.GORRI_BIZIA);
                    } else if (balioa == Connect4Model.JOKALARI_IA) {
                        g2.setColor(UITheme.AMBAR_HORIA);
                    } else {
                        g2.setColor(UITheme.ATZEALDE_NAGUSIA); // Hutsik dagoen zuloa
                    }

                    g2.fillOval(x, y, diametroa, diametroa);

                    // 3D sakonera efektua (itzal txikia ertzean)
                    g2.setColor(new Color(0, 0, 0, 70));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(x, y, diametroa, diametroa);

                    // Fitxa irabazlea bada, urrezko distiraz nabarmendu
                    if (irabazleak != null && irabazleak.contains(new Point(c, r))) {
                        g2.setColor(Color.WHITE);
                        g2.setStroke(new BasicStroke(4));
                        g2.drawOval(x - 2, y - 2, diametroa + 4, diametroa + 4);
                    }
                }
            }
        }
    }

    public void birmarraztuTaula() {
        taulaPanela.repaint();
    }

    public void ezarriEgoeraTestua(String mezua, Color kolorea) {
        etiketaEgoera.setText(mezua);
        etiketaEgoera.setForeground(kolorea);
    }

    public void gaituZutabeBotoiak(boolean gaituta) {
        for (int c = 0; c < 7; c++) {
            if (gaituta) {
                // Zutabea ez badago beteta, gaitu
                botoiakZutabeak[c].setEnabled(!eredua.zutabeaBeteaDago(c));
            } else {
                botoiakZutabeak[c].setEnabled(false);
            }
        }
    }

    public void gehituZutabeEntzulea(int col, ActionListener entzulea) {
        botoiakZutabeak[col].addActionListener(entzulea);
    }

    public void gehituBerrezarriEntzulea(ActionListener entzulea) {
        botoiaBerrezarri.addActionListener(entzulea);
    }
}
