package com.erronka.advanced;

import com.erronka.common.UITheme;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * SnakeView: Snake 2D jokoaren leiho nagusia.
 * Panel grafikoa, kontrol-botoiak eta markagailu dinamikoa integratzen ditu.
 */
public class SnakeView extends JFrame {

    private final SnakePanel snakePanel;
    private final JLabel etiketaPuntuazioa;
    private final JLabel etiketaErrekorra;
    private final JButton botoiaAutoPlay;
    private final JButton botoiaPausa;
    private final JButton botoiaBerrezarri;

    public SnakeView(SnakeModel eredua, GameLoop gameLoop) {
        super("3. Ariketa: Snake 2D (Hariekin) + Auto-Play (A* IA)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(UITheme.ATZEALDE_NAGUSIA);
        setLayout(new BorderLayout());

        // 1. GOIKO MARKA-PANELA: Puntuazioa, Errekorra eta Kontrol-botoiak
        JPanel goikoPanela = new JPanel(new BorderLayout());
        goikoPanela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        goikoPanela.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        // Ezkerreko markagailua
        JPanel markagailua = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        markagailua.setOpaque(false);

        etiketaPuntuazioa = new JLabel("Puntuak: 0");
        etiketaPuntuazioa.setForeground(UITheme.ESMERALDA_BERDEA);
        etiketaPuntuazioa.setFont(new Font("Segoe UI", Font.BOLD, 15));

        etiketaErrekorra = new JLabel("Errekorra: 0");
        etiketaErrekorra.setForeground(UITheme.AMBAR_HORIA);
        etiketaErrekorra.setFont(new Font("Segoe UI", Font.BOLD, 14));

        markagailua.add(etiketaPuntuazioa);
        markagailua.add(etiketaErrekorra);
        goikoPanela.add(markagailua, BorderLayout.WEST);

        // Eskuineko botoiak
        JPanel botoiPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoiPanela.setOpaque(false);

        botoiaAutoPlay = new JButton("🤖 Auto-Play (IA)");
        UITheme.estilizatuBotoia(botoiaAutoPlay, UITheme.MORE_ARGIA);

        botoiaPausa = new JButton("⏸️ Pausa");
        UITheme.estilizatuBotoia(botoiaPausa, UITheme.PANEL_ATZEALDEA);

        botoiaBerrezarri = new JButton("🔄 Berrezarri");
        UITheme.estilizatuBotoia(botoiaBerrezarri, UITheme.PANEL_ATZEALDEA);

        botoiPanela.add(botoiaAutoPlay);
        botoiPanela.add(botoiaPausa);
        botoiPanela.add(botoiaBerrezarri);
        goikoPanela.add(botoiPanela, BorderLayout.EAST);

        add(goikoPanela, BorderLayout.NORTH);

        // 2. ERDIKO PANELA: Jokoaren renderizazioa
        snakePanel = new SnakePanel(eredua, gameLoop);
        add(snakePanel, BorderLayout.CENTER);

        // 3. BEHEKO PANELA: Argibideak
        JPanel behekoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER));
        behekoPanela.setBackground(UITheme.TXARTEL_ATZEALDEA);
        behekoPanela.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JLabel etiketaArgibideak = new JLabel(
            "Teklak: Geziak / WASD  |  Zuriunea: Auto-Play aktibatu  |  P: Pausa  |  R: Berrezarri",
            SwingConstants.CENTER
        );
        etiketaArgibideak.setForeground(UITheme.TESTU_MUTUA);
        etiketaArgibideak.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        behekoPanela.add(etiketaArgibideak);

        add(behekoPanela, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public SnakePanel getSnakePanel() {
        return snakePanel;
    }

    public void eguneratuMarkagailua(int puntuak, int errekorra) {
        etiketaPuntuazioa.setText("Puntuak: " + puntuak);
        etiketaErrekorra.setText("Errekorra: " + errekorra);
    }

    public void eguneratuAutoPlayBotoia(boolean aktibo) {
        if (aktibo) {
            botoiaAutoPlay.setText("👤 Eskuzko Modua");
            botoiaAutoPlay.setBackground(UITheme.AMBAR_HORIA);
        } else {
            botoiaAutoPlay.setText("🤖 Auto-Play (IA)");
            botoiaAutoPlay.setBackground(UITheme.MORE_ARGIA);
        }
    }

    public void eguneratuPausaBotoia(boolean pausatuta) {
        botoiaPausa.setText(pausatuta ? "▶️ Jarraitu" : "⏸️ Pausa");
    }

    public void gehituAutoPlayEntzulea(ActionListener entzulea) {
        botoiaAutoPlay.addActionListener(entzulea);
    }

    public void gehituPausaEntzulea(ActionListener entzulea) {
        botoiaPausa.addActionListener(entzulea);
    }

    public void gehituBerrezarriEntzulea(ActionListener entzulea) {
        botoiaBerrezarri.addActionListener(entzulea);
    }
}
