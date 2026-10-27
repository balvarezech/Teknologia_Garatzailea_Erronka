package com.erronka.intermediate;

import com.erronka.common.UITheme;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Connect4Controller: Konektatu 4 jokoaren kontrolatzailea (MVC).
 * Bistarako eta Ereduaren arteko zubi segurua egiten du, gertaerak kudeatzen ditu
 * eta botaren txandak modu asinkronoan koordinatzen ditu (EDT ez blokeatzeko).
 */
public class Connect4Controller {

    private final Connect4Model eredua;
    private final Connect4View bista;

    public Connect4Controller(Connect4Model eredua, Connect4View bista) {
        this.eredua = eredua;
        this.bista = bista;

        hasieratuGertaerak();
        eguneratuBistaOsorik();
    }

    /**
     * Erabiltzailearen 7 zutabe-botoiak eta berrezarri botoia entzuten ditu.
     */
    private void hasieratuGertaerak() {
        try {
            for (int c = 0; c < 7; c++) {
                final int zutabea = c;
                bista.gehituZutabeEntzulea(zutabea, (ActionEvent e) -> kudeatuZutabeKlik(zutabea));
            }

            bista.gehituBerrezarriEntzulea(e -> berrezarriPartida());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(bista,
                "Errorea kontrolatzailea hasieratzean: " + ex.getMessage(),
                "Errorea", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Erabiltzaileak zutabe batean fitxa botatzean exekutatzen da.
     */
    private void kudeatuZutabeKlik(int zutabea) {
        try {
            if (eredua.getEgoera() != Connect4Model.JokoEgoera.JOKATZEN) {
                return;
            }

            if (eredua.getUnekoTxanda() == Connect4Model.JOKALARI_GIZAKIA) {
                int errenkada = eredua.botaFitxa(zutabea, Connect4Model.JOKALARI_GIZAKIA);
                if (errenkada != -1) {
                    bista.birmarraztuTaula();
                    egiaztatuEgoeraEtaHurrengoa();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(bista,
                "Ustekabeko errorea fitxa botatzean: " + ex.getMessage(),
                "Errorea", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Egoera aztertu eta beharrezkoa bada AIaren txanda jaurtitzen du.
     */
    private void egiaztatuEgoeraEtaHurrengoa() {
        Connect4Model.JokoEgoera egoera = eredua.getEgoera();

        if (egoera == Connect4Model.JokoEgoera.GIZAKIA_IRABAZLE) {
            bista.ezarriEgoeraTestua("🎉 ZORIONAK! 4 lerroan lortu duzu!", UITheme.ESMERALDA_BERDEA);
            bista.gaituZutabeBotoiak(false);
            bista.birmarraztuTaula();
        } else if (egoera == Connect4Model.JokoEgoera.IA_IRABAZLE) {
            bista.ezarriEgoeraTestua("🤖 AIak irabazi du 4 lerroan!", UITheme.GORRI_BIZIA);
            bista.gaituZutabeBotoiak(false);
            bista.birmarraztuTaula();
        } else if (egoera == Connect4Model.JokoEgoera.BERDINKETA) {
            bista.ezarriEgoeraTestua("🤝 Taula beteta! Berdinketa gertatu da.", UITheme.AMBAR_HORIA);
            bista.gaituZutabeBotoiak(false);
        } else {
            // Partidak jarraitzen badu eta botaren txanda bada
            if (eredua.getUnekoTxanda() == Connect4Model.JOKALARI_IA) {
                bista.ezarriEgoeraTestua("🤖 AI bota kalkulatzen ari da...", UITheme.TESTU_MUTUA);
                bista.gaituZutabeBotoiak(false);

                // 400ms-ko atzerapena pentsatze-simulaziorako
                Timer botTimer = new Timer(400, evt -> exekutatuAIMugimendua());
                botTimer.setRepeats(false);
                botTimer.start();
            } else {
                bista.ezarriEgoeraTestua("Zure txanda da (Fitxa Gorriak)", UITheme.GORRI_BIZIA);
                bista.gaituZutabeBotoiak(true);
            }
        }
    }

    /**
     * Adimen Artifizialaren zutabe optimoa kalkulatu eta fitxa botatzen du.
     */
    private void exekutatuAIMugimendua() {
        try {
            if (eredua.getEgoera() != Connect4Model.JokoEgoera.JOKATZEN) {
                bista.gaituZutabeBotoiak(true);
                return;
            }

            int zutabeOptimoa = Connect4AI.kalkulatuMugimendurikOnena(eredua);
            if (zutabeOptimoa != -1) {
                eredua.botaFitxa(zutabeOptimoa, Connect4Model.JOKALARI_IA);
                bista.birmarraztuTaula();
            }

            egiaztatuEgoeraEtaHurrengoa();
        } catch (Exception ex) {
            bista.gaituZutabeBotoiak(true);
            JOptionPane.showMessageDialog(bista,
                "Errorea botaren kalkuluan: " + ex.getMessage(),
                "AI Errorea", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Jokoa berrezartzen du.
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

    private void eguneratuBistaOsorik() {
        bista.birmarraztuTaula();
        bista.gaituZutabeBotoiak(true);
        bista.ezarriEgoeraTestua("Zure txanda da (Fitxa Gorriak)", UITheme.GORRI_BIZIA);
    }
}
