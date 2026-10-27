package com.erronka.advanced;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

/**
 * SnakeController: Snake 2D jokoaren kontrolatzailea.
 * Teklatuaren sarrerak, botoien gertaerak eta hariaren (GameLoop) bizi-zikloa
 * modu seguruan sinkronizatzen ditu.
 */
public class SnakeController {

    private final SnakeModel eredua;
    private final SnakeView bista;
    private final GameLoop gameLoop;

    public SnakeController(SnakeModel eredua, SnakeView bista, GameLoop gameLoop) {
        this.eredua = eredua;
        this.bista = bista;
        this.gameLoop = gameLoop;

        hasieratuGertaerak();
        gameLoop.abiarazi();
    }

    private void hasieratuGertaerak() {
        try {
            // 1. Teklatuaren entzulea panel nagusian
            bista.getSnakePanel().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    kudeatuTeklatua(e);
                }
            });

            // 2. Botoien ekintzak
            bista.gehituAutoPlayEntzulea(e -> txandakatuAutoPlay());
            bista.gehituPausaEntzulea(e -> txandakatuPausa());
            bista.gehituBerrezarriEntzulea(e -> berrezarriJokoa());

            // 3. Leihoa ixtean haria gelditu
            bista.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    gameLoop.gelditu();
                }
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(bista,
                "Errorea kontrolatzailea hasieratzean: " + ex.getMessage(),
                "Errorea", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kudeatuTeklatua(KeyEvent e) {
        try {
            int kodea = e.getKeyCode();

            // Norabide aldaketak (Geziak edo WASD)
            if (kodea == KeyEvent.VK_UP || kodea == KeyEvent.VK_W) {
                eredua.ezarriNorabidea(SnakeModel.Norabidea.GORA);
                eredua.setAutoPlayAktibatuta(false); // Eskuz jokatzen hasi
                bista.eguneratuAutoPlayBotoia(false);
            } else if (kodea == KeyEvent.VK_DOWN || kodea == KeyEvent.VK_S) {
                eredua.ezarriNorabidea(SnakeModel.Norabidea.BEHERA);
                eredua.setAutoPlayAktibatuta(false);
                bista.eguneratuAutoPlayBotoia(false);
            } else if (kodea == KeyEvent.VK_LEFT || kodea == KeyEvent.VK_A) {
                eredua.ezarriNorabidea(SnakeModel.Norabidea.EZKERRERA);
                eredua.setAutoPlayAktibatuta(false);
                bista.eguneratuAutoPlayBotoia(false);
            } else if (kodea == KeyEvent.VK_RIGHT || kodea == KeyEvent.VK_D) {
                eredua.ezarriNorabidea(SnakeModel.Norabidea.ESKUINERA);
                eredua.setAutoPlayAktibatuta(false);
                bista.eguneratuAutoPlayBotoia(false);
            } else if (kodea == KeyEvent.VK_SPACE) {
                // Zuriunea: Auto-Play piztu/itzali
                txandakatuAutoPlay();
            } else if (kodea == KeyEvent.VK_P) {
                // P: Pausatu
                txandakatuPausa();
            } else if (kodea == KeyEvent.VK_R) {
                // R: Berrezarri
                berrezarriJokoa();
            }

            bista.getSnakePanel().repaint();

        } catch (Exception ex) {
            System.err.println("Errorea tekla sakatzean: " + ex.getMessage());
        }
    }

    private void txandakatuAutoPlay() {
        eredua.txandakatuAutoPlay();
        bista.eguneratuAutoPlayBotoia(eredua.isAutoPlayAktibatuta());
        bista.getSnakePanel().requestFocusInWindow();
        bista.getSnakePanel().repaint();
    }

    private void txandakatuPausa() {
        gameLoop.txandakatuPausa();
        bista.eguneratuPausaBotoia(gameLoop.isPausatuta());
        bista.getSnakePanel().requestFocusInWindow();
        bista.getSnakePanel().repaint();
    }

    private void berrezarriJokoa() {
        eredua.berrezarriJokoa();
        bista.eguneratuMarkagailua(eredua.getPuntuazioa(), eredua.getErrekorra());
        bista.eguneratuPausaBotoia(gameLoop.isPausatuta());
        bista.getSnakePanel().requestFocusInWindow();
        bista.getSnakePanel().repaint();
    }

    public void birmarraztu() {
        bista.eguneratuMarkagailua(eredua.getPuntuazioa(), eredua.getErrekorra());
        bista.getSnakePanel().repaint();
    }

    public void kudeatuJokoAmaiera() {
        bista.getSnakePanel().repaint();
    }
}
