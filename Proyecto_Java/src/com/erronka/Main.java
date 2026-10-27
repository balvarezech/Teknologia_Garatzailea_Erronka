package com.erronka;

import com.erronka.advanced.GameLoop;
import com.erronka.advanced.SnakeController;
import com.erronka.advanced.SnakeModel;
import com.erronka.advanced.SnakeView;
import com.erronka.basic.TicTacToeController;
import com.erronka.basic.TicTacToeModel;
import com.erronka.basic.TicTacToeView;
import com.erronka.common.UITheme;
import com.erronka.intermediate.Connect4Controller;
import com.erronka.intermediate.Connect4Model;
import com.erronka.intermediate.Connect4View;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Main: Aplikazioaren sarrera-puntu nagusia eta Abiarazle Orokorra (Launcher Hub).
 * Hiru minijokoak bateratzen dituen Swing leiho modernoa erakusten du.
 * Leiho bakoitza modu independentean abiarazten da (DISPOSE_ON_CLOSE erabiliz),
 * aplikazio nagusia inoiz ez ixteko edo blokeatzeko.
 */
public class Main extends JFrame {

    public Main() {
        super("Teknologia Berritzaileak: Java + IA Minijokoen Suite Nagusia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 640);
        setMinimumSize(new Dimension(680, 560));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.ATZEALDE_NAGUSIA);
        setLayout(new BorderLayout(15, 15));

        // 1. GOIBURUA: Titulua eta Azpititulua
        JPanel goiburua = new JPanel();
        goiburua.setLayout(new BoxLayout(goiburua, BoxLayout.Y_AXIS));
        goiburua.setBackground(UITheme.TXARTEL_ATZEALDEA);
        goiburua.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel etiketaTitulua = new JLabel("JAVA + ADIMEN ARTIFIZIALA: MINIJOKOEN SUITE-A");
        etiketaTitulua.setFont(UITheme.LETRA_TITULUA);
        etiketaTitulua.setForeground(UITheme.ZIAN_DISTIRA);
        etiketaTitulua.setAlignmentX(CENTER_ALIGNMENT);

        JLabel etiketaAzpititulua = new JLabel("Talde Bakarra (3 Ikasle Elkarlanean) • Ebaluazio Errubrika Bikaina (10 Puntu)");
        etiketaAzpititulua.setFont(UITheme.LETRA_AZPITITULUA);
        etiketaAzpititulua.setForeground(UITheme.TESTU_MUTUA);
        etiketaAzpititulua.setAlignmentX(CENTER_ALIGNMENT);

        goiburua.add(etiketaTitulua);
        goiburua.add(Box.createRigidArea(new Dimension(0, 8)));
        goiburua.add(etiketaAzpititulua);
        add(goiburua, BorderLayout.NORTH);

        // 2. ERDIKO PANELA: 3 Jokoen Txartelak (Cards)
        JPanel txartelPanela = new JPanel(new GridLayout(3, 1, 14, 14));
        txartelPanela.setBackground(UITheme.ATZEALDE_NAGUSIA);
        txartelPanela.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // 1. Txartela: Hiru Lerroan (Oinarrizkoa)
        txartelPanela.add(sortuJokoTxartela(
            "1. Ariketa (Oinarrizkoa): Hiru Lerroan (Tic-Tac-Toe)",
            "3x3 sareta, Minimax algoritmoa sakonera-zigorrarekin, zailtasun-hautatzailea eta aholku adimendunak (pistas).",
            "▶️ Jokatu Hiru Lerroan",
            UITheme.ZIAN_DISTIRA,
            () -> abiaraziHiruLerroan()
        ));

        // 2. Txartela: Konektatu 4 (Tartekoa)
        txartelPanela.add(sortuJokoTxartela(
            "2. Ariketa (Tartekoa): Konektatu 4 (Connect 4)",
            "6x7 matrizea grabitate fisikoarekin, 4 noranzkoetako garaipena eta bot heuristiko aurreratua (blokeoak eta erdialdeko balioa).",
            "▶️ Jokatu Konektatu 4",
            UITheme.AMBAR_HORIA,
            () -> abiaraziKonektatu4()
        ));

        // 3. Txartela: Snake 2D (Aurreratua)
        txartelPanela.add(sortuJokoTxartela(
            "3. Ariketa (Aurreratua): Snake 2D (Hariekin / Real-Time)",
            "Game Loop multihiloa (Thread), flicker-free grafikoak eta Auto-Play modua (A* / BFS bide-bilaketa adimentsua).",
            "▶️ Jokatu Snake 2D",
            UITheme.ESMERALDA_BERDEA,
            () -> abiaraziSnake()
        ));

        add(txartelPanela, BorderLayout.CENTER);

        // 3. BEHEKO PANELA: Oina eta Irten botoia
        JPanel behekoPanela = new JPanel(new BorderLayout());
        behekoPanela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        behekoPanela.setBorder(BorderFactory.createEmptyBorder(12, 25, 14, 25));

        JLabel oinaTestua = new JLabel("Teknologia Berritzaileak • Garapena Java Purean (JDK 21+) eta Swing MVC");
        oinaTestua.setFont(UITheme.LETRA_ARRUNTA);
        oinaTestua.setForeground(UITheme.TESTU_MUTUA);

        JButton botoiaIrten = new JButton("🚪 Irten");
        UITheme.estilizatuBotoia(botoiaIrten, UITheme.PANEL_ATZEALDEA);
        botoiaIrten.addActionListener(e -> System.exit(0));

        behekoPanela.add(oinaTestua, BorderLayout.WEST);
        behekoPanela.add(botoiaIrten, BorderLayout.EAST);
        add(behekoPanela, BorderLayout.SOUTH);
    }

    /**
     * Joko bakoitzaren txartel bisuala eraikitzen duen metodo laguntzailea.
     */
    private JPanel sortuJokoTxartela(String titulu, String azalpen, String botoiTestu, Color azentuKolorea, Runnable ekintza) {
        JPanel txartela = new JPanel(new BorderLayout(15, 10));
        txartela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        txartela.setBorder(UITheme.sortuTxartelErtza(azentuKolorea));

        // Ezkerreko testuak
        JPanel testuPanela = new JPanel();
        testuPanela.setLayout(new BoxLayout(testuPanela, BoxLayout.Y_AXIS));
        testuPanela.setOpaque(false);

        JLabel tLabel = new JLabel(titulu);
        tLabel.setFont(UITheme.LETRA_AZPITITULUA);
        tLabel.setForeground(azentuKolorea);

        JLabel aLabel = new JLabel("<html>" + azalpen + "</html>");
        aLabel.setFont(UITheme.LETRA_ARRUNTA);
        aLabel.setForeground(UITheme.TESTU_ZURIA);

        testuPanela.add(tLabel);
        testuPanela.add(Box.createRigidArea(new Dimension(0, 4)));
        testuPanela.add(aLabel);
        txartela.add(testuPanela, BorderLayout.CENTER);

        // Eskuineko botoia
        JPanel botoiPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoiPanela.setOpaque(false);

        JButton botoia = new JButton(botoiTestu);
        botoia.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UITheme.estilizatuBotoia(botoia, azentuKolorea);
        botoia.addActionListener(e -> {
            try {
                ekintza.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Errorea jokoa abiaraztean: " + ex.getMessage(),
                    "Errorea", JOptionPane.ERROR_MESSAGE);
            }
        });

        botoiPanela.add(botoia);
        txartela.add(botoiPanela, BorderLayout.EAST);

        return txartela;
    }

    /**
     * 1. Ariketa abiarazten du (Hiru Lerroan).
     */
    private void abiaraziHiruLerroan() {
        TicTacToeModel eredua = new TicTacToeModel();
        TicTacToeView bista = new TicTacToeView();
        new TicTacToeController(eredua, bista);
        bista.setVisible(true);
    }

    /**
     * 2. Ariketa abiarazten du (Konektatu 4).
     */
    private void abiaraziKonektatu4() {
        Connect4Model eredua = new Connect4Model();
        Connect4View bista = new Connect4View(eredua);
        new Connect4Controller(eredua, bista);
        bista.setVisible(true);
    }

    /**
     * 3. Ariketa abiarazten du (Snake 2D).
     */
    private void abiaraziSnake() {
        SnakeModel eredua = new SnakeModel();

        // Aurrez kontrolatzaileari erreferentzia egiteko edukiontzia
        final SnakeController[] kontrolatzailea = new SnakeController[1];

        GameLoop gameLoop = new GameLoop(
            eredua,
            () -> {
                if (kontrolatzailea[0] != null) {
                    kontrolatzailea[0].birmarraztu();
                }
            },
            () -> {
                if (kontrolatzailea[0] != null) {
                    kontrolatzailea[0].kudeatuJokoAmaiera();
                }
            }
        );

        SnakeView bista = new SnakeView(eredua, gameLoop);
        kontrolatzailea[0] = new SnakeController(eredua, bista, gameLoop);

        bista.setVisible(true);
        bista.getSnakePanel().requestFocusInWindow();
    }

    /**
     * Aplikazioaren abio nagusia.
     */
    public static void main(String[] args) {
        // Swing Event Dispatch Thread (EDT) barruan hasieratu
        SwingUtilities.invokeLater(() -> {
            try {
                Main abiarazlea = new Main();
                abiarazlea.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Errorea aplikazio nagusia irekitzean: " + e.getMessage(),
                    "Errore Larria", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
