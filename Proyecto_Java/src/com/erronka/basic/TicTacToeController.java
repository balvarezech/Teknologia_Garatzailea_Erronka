package com.erronka.basic;

import com.erronka.common.UITheme;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * TicTacToeController: Hiru Lerroan jokoaren kontrolatzailea (MVC).
 * Ereduaren eta Bistaren arteko komunikazioa kudeatzen du, gertaerak entzuten ditu
 * eta salbuespen guztiak modu seguruan babesten ditu (Crash-Free).
 */
public class TicTacToeController {

    private final TicTacToeModel eredua;
    private final TicTacToeView bista;

    public TicTacToeController(TicTacToeModel eredua, TicTacToeView bista) {
        this.eredua = eredua;
        this.bista = bista;

        hasieratuGertaerak();
        eguneratuBistaOsorik();
    }

    /**
     * Erabiltzailearen ekintzen entzuleak prestatzen ditu.
     */
    private void hasieratuGertaerak() {
        try {
            // 9 gelaxken klikak entzun
            for (int i = 0; i < 3; i++) {
                final int errenkada = i;
                for (int j = 0; j < 3; j++) {
                    final int zutabea = j;
                    bista.gehituGelaxkaEntzulea(errenkada, zutabea, (ActionEvent e) -> kudeatuGelaxkaKlik(errenkada, zutabea));
                }
            }

            // Aholku adimendunaren botoia
            bista.gehituAholkuEntzulea(e -> kudeatuAholkua());

            // Berrezarri partida botoia
            bista.gehituBerrezarriEntzulea(e -> berrezarriPartida());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(bista,
                "Errorea kontrolatzailea hasieratzean: " + ex.getMessage(),
                "Errore Kritikoa", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Jokalariak gelaxka bat sakatzean exekutatzen da.
     */
    private void kudeatuGelaxkaKlik(int r, int c) {
        try {
            if (eredua.getEgoera() != TicTacToeModel.JokoEgoera.JOKATZEN) {
                return;
            }

            // Gizakiaren mugimendua saiatu ('X')
            if (eredua.getUnekoTxanda() == TicTacToeModel.JOKALARI_X) {
                boolean mugitua = eredua.eginMugimendua(r, c);
                if (mugitua) {
                    bista.ezarriGelaxkaIkurra(r, c, TicTacToeModel.JOKALARI_X);
                    egiaztatuEgoeraEtaHurrengoTxanda();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(bista,
                "Ustekabeko errorea klikatzean: " + ex.getMessage(),
                "Errorea", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Egoera egiaztatzen du eta, behar izanez gero, AIaren txanda pizten du.
     */
    private void egiaztatuEgoeraEtaHurrengoTxanda() {
        TicTacToeModel.JokoEgoera egoera = eredua.getEgoera();

        if (egoera == TicTacToeModel.JokoEgoera.X_IRABAZLE) {
            bista.ezarriEgoeraTestua("🎉 ZORIONAK! Irabazi duzu!", UITheme.ESMERALDA_BERDEA);
        } else if (egoera == TicTacToeModel.JokoEgoera.O_IRABAZLE) {
            bista.ezarriEgoeraTestua("🤖 AIak irabazi du!", UITheme.GORRI_BIZIA);
        } else if (egoera == TicTacToeModel.JokoEgoera.BERDINKETA) {
            bista.ezarriEgoeraTestua("🤝 Berdinketa! Partida bikaina.", UITheme.AMBAR_HORIA);
        } else {
            // Partidak jarraitzen badu eta botaren txanda bada
            if (eredua.getUnekoTxanda() == TicTacToeModel.JOKALARI_O) {
                bista.ezarriEgoeraTestua("🤖 AIa pentsatzen ari da...", UITheme.TESTU_MUTUA);
                bista.gaituGelaxkak(false);

                // 350ms-ko atzerapena simulazio errealistarako
                Timer botPentsatzen = new Timer(350, evt -> exekutatuAIMugimendua());
                botPentsatzen.setRepeats(false);
                botPentsatzen.start();
            } else {
                bista.ezarriEgoeraTestua("Zure txanda da (X)", UITheme.ZIAN_DISTIRA);
                bista.gaituGelaxkak(true);
            }
        }
    }

    /**
     * Adimen Artifizialaren mugimendua kalkulatu eta taulan jartzen du.
     */
    private void exekutatuAIMugimendua() {
        try {
            if (eredua.getEgoera() != TicTacToeModel.JokoEgoera.JOKATZEN) {
                bista.gaituGelaxkak(true);
                return;
            }

            char[][] kopia = eredua.getTaularenKopia();
            TicTacToeAI.ZailtasunMaila zailtasuna = bista.getHautatutakoZailtasuna();

            int[] mugimendua = TicTacToeAI.kalkulatuMugimendua(kopia, zailtasuna, TicTacToeModel.JOKALARI_O);
            if (mugimendua != null) {
                eredua.eginMugimendua(mugimendua[0], mugimendua[1]);
                bista.ezarriGelaxkaIkurra(mugimendua[0], mugimendua[1], TicTacToeModel.JOKALARI_O);
            }

            bista.gaituGelaxkak(true);
            egiaztatuEgoeraEtaHurrengoTxanda();

        } catch (Exception ex) {
            bista.gaituGelaxkak(true);
            JOptionPane.showMessageDialog(bista,
                "Errorea AIaren mugimenduan: " + ex.getMessage(),
                "AI Errorea", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Jokalariari aholku adimenduna (Smart Hint) erakusten dio.
     */
    private void kudeatuAholkua() {
        try {
            if (eredua.getEgoera() != TicTacToeModel.JokoEgoera.JOKATZEN || eredua.getUnekoTxanda() != TicTacToeModel.JOKALARI_X) {
                return;
            }

            char[][] kopia = eredua.getTaularenKopia();
            int[] aholkua = TicTacToeAI.lortuAholkuAdimentsua(kopia, TicTacToeModel.JOKALARI_X);
            if (aholkua != null) {
                bista.nabarmenduAholkua(aholkua[0], aholkua[1]);
                bista.ezarriEgoeraTestua("💡 Aholkua: gelaxka horia gomendatzen da!", UITheme.AMBAR_HORIA);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(bista,
                "Ezin izan da aholkua kalkulatu: " + ex.getMessage(),
                "Aholku Errorea", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Partida berrabiarazten du.
     */
    private void berrezarriPartida() {
        try {
            eredua.berrezarriJokoa();
            eguneratuBistaOsorik();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(bista,
                "Errorea partida berrezartzean: " + ex.getMessage(),
                "Errorea", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Bista osoa ereduarekin sinkronizatzen du.
     */
    private void eguneratuBistaOsorik() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bista.ezarriGelaxkaIkurra(i, j, eredua.getGelaxka(i, j));
            }
        }
        bista.gaituGelaxkak(true);
        bista.ezarriEgoeraTestua("Zure txanda da (X)", UITheme.ZIAN_DISTIRA);
    }
}
