package com.erronka.advanced;

import com.erronka.common.UITheme;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 * SnakePanel: Snake 2D jokoaren marrazketa-panela.
 * Graphics2D, Antialiasing eta Swing-eko buffer bikoitza erabiltzen ditu,
 * mugimendu guztiz leun eta dardararik gabea (flicker-free) bermatzeko.
 */
public class SnakePanel extends JPanel {

    public static final int GELAXKA_TAMAINA = 20; // Píxel bakoitzeko neurria
    public static final int PANELA_TAMAINA = SnakeModel.SARETA_TAMAINA * GELAXKA_TAMAINA; // 500x500 px

    private final SnakeModel eredua;
    private final GameLoop gameLoop;

    public SnakePanel(SnakeModel eredua, GameLoop gameLoop) {
        this.eredua = eredua;
        this.gameLoop = gameLoop;

        setPreferredSize(new Dimension(PANELA_TAMAINA, PANELA_TAMAINA));
        setBackground(UITheme.ATZEALDE_NAGUSIA);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Sareta fin birtuala marraztu
        g2.setColor(new Color(30, 41, 59, 120));
        for (int i = 0; i <= SnakeModel.SARETA_TAMAINA; i++) {
            int pos = i * GELAXKA_TAMAINA;
            g2.drawLine(pos, 0, pos, PANELA_TAMAINA);
            g2.drawLine(0, pos, PANELA_TAMAINA, pos);
        }

        // 2. Sagarra marraztu (Rubi gorri distiratsua hosto berdearekin)
        Point sagarra = eredua.getSagarra();
        if (sagarra != null) {
            int sx = sagarra.x * GELAXKA_TAMAINA;
            int sy = sagarra.y * GELAXKA_TAMAINA;

            // Sagarraren gorputza
            g2.setColor(UITheme.GORRI_BIZIA);
            g2.fillOval(sx + 2, sy + 2, GELAXKA_TAMAINA - 4, GELAXKA_TAMAINA - 4);

            // Hosto txikia
            g2.setColor(UITheme.ESMERALDA_BERDEA);
            g2.fillOval(sx + (GELAXKA_TAMAINA / 2), sy, 4, 6);
        }

        // 3. Sugea marraztu
        LinkedList<Point> sugea = eredua.getSugearenKopia();
        if (!sugea.isEmpty()) {
            Point burua = sugea.getFirst();

            // Gorputzaren segmentuak
            for (int i = sugea.size() - 1; i >= 1; i--) {
                Point p = sugea.get(i);
                int px = p.x * GELAXKA_TAMAINA;
                int py = p.y * GELAXKA_TAMAINA;

                // Gradiente efektu txikia buztanetik burura
                int berdeIntentsitatea = Math.max(140, 185 - (i * 2));
                g2.setColor(new Color(16, berdeIntentsitatea, 129));
                g2.fillRoundRect(px + 1, py + 1, GELAXKA_TAMAINA - 2, GELAXKA_TAMAINA - 2, 6, 6);
            }

            // Burua marraztu (kolore argiagoa eta begiak)
            int bx = burua.x * GELAXKA_TAMAINA;
            int by = burua.y * GELAXKA_TAMAINA;
            g2.setColor(new Color(52, 211, 153)); // Esmeralda argia (#34D399)
            g2.fillRoundRect(bx + 1, by + 1, GELAXKA_TAMAINA - 2, GELAXKA_TAMAINA - 2, 8, 8);

            // Begiak norabidearen arabera marraztu
            g2.setColor(Color.BLACK);
            SnakeModel.Norabidea n = eredua.getUnekoNorabidea();
            int begiTamaina = 3;

            if (n == SnakeModel.Norabidea.ESKUINERA) {
                g2.fillOval(bx + 13, by + 4, begiTamaina, begiTamaina);
                g2.fillOval(bx + 13, by + 13, begiTamaina, begiTamaina);
            } else if (n == SnakeModel.Norabidea.EZKERRERA) {
                g2.fillOval(bx + 4, by + 4, begiTamaina, begiTamaina);
                g2.fillOval(bx + 4, by + 13, begiTamaina, begiTamaina);
            } else if (n == SnakeModel.Norabidea.GORA) {
                g2.fillOval(bx + 4, by + 4, begiTamaina, begiTamaina);
                g2.fillOval(bx + 13, by + 4, begiTamaina, begiTamaina);
            } else { // BEHERA
                g2.fillOval(bx + 4, by + 13, begiTamaina, begiTamaina);
                g2.fillOval(bx + 13, by + 13, begiTamaina, begiTamaina);
            }
        }

        // 4. Auto-Play plaka marraztu aktibatuta badago
        if (eredua.isAutoPlayAktibatuta()) {
            g2.setColor(new Color(168, 85, 247, 220)); // More bizia
            g2.fillRoundRect(PANELA_TAMAINA - 165, 12, 150, 28, 12, 12);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString("🤖 AUTO-PLAY (IA)", PANELA_TAMAINA - 152, 31);
        }

        // 5. Gainezarritako mezuak (Pausa edo Joko Amaiera)
        if (eredua.isAmaituta()) {
            marraztuMezuGainezarria(g2, "JOKOA AMAITU DA!", "Sakatu 'R' berriz hasteko", UITheme.GORRI_BIZIA);
        } else if (gameLoop != null && gameLoop.isPausatuta()) {
            marraztuMezuGainezarria(g2, "PAUSATUTA", "Sakatu 'P' edo Espazioa jarraitzeko", UITheme.AMBAR_HORIA);
        }
    }

    private void marraztuMezuGainezarria(Graphics2D g2, String titulu, String azpititulu, Color kolorea) {
        // Atzeko itzal erdi-gardena
        g2.setColor(new Color(15, 23, 42, 210));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Titulua
        g2.setColor(kolorea);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
        FontMetrics fm1 = g2.getFontMetrics();
        int tx = (getWidth() - fm1.stringWidth(titulu)) / 2;
        int ty = (getHeight() / 2) - 10;
        g2.drawString(titulu, tx, ty);

        // Azpititulua
        g2.setColor(UITheme.TESTU_ZURIA);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        FontMetrics fm2 = g2.getFontMetrics();
        int ax = (getWidth() - fm2.stringWidth(azpititulu)) / 2;
        int ay = ty + 30;
        g2.drawString(azpititulu, ax, ay);
    }
}
