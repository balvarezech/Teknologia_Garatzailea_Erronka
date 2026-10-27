package com.erronka.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TicTacToeAI: Hiru Lerroan jokorako Adimen Artifizialaren algoritmoa.
 * Minimax algoritmoa sakonera-zigorrarekin inplementatzen du, 3 zailtasun-maila eskaintzen ditu,
 * eta jokalariari gomendio adimendunak (pistas) emateko metodoa barneratzen du.
 */
public class TicTacToeAI {

    /**
     * Zailtasun mailak
     */
    public enum ZailtasunMaila {
        ERRAZA("Erraza (Ausazkoa)"),
        TARTEKOA("Tartekoa (Nahastua)"),
        GARAIEZINA("Garaiezina (Minimax)");

        private final String izena;

        ZailtasunMaila(String izena) {
            this.izena = izena;
        }

        @Override
        public String toString() {
            return this.izena;
        }
    }

    private static final Random AUSAZKOA = new Random();

    /**
     * Bota zein gelaxkatara mugituko den kalkulatzen du hautatutako zailtasunaren arabera.
     *
     * @param taula Uneko taularen matrizea.
     * @param zailtasuna Hautatutako ZailtasunMaila.
     * @param bota Botaren ikurra ('O').
     * @return int[2] non [0] = errenkada eta [1] = zutabea diren.
     */
    public static int[] kalkulatuMugimendua(char[][] taula, ZailtasunMaila zailtasuna, char bota) {
        List<int[]> hutsik = lortuHutsikDaudenGelaxkak(taula);
        if (hutsik.isEmpty()) {
            return null;
        }

        switch (zailtasuna) {
            case ERRAZA:
                return hutsik.get(AUSAZKOA.nextInt(hutsik.size()));

            case TARTEKOA:
                // %50 aukera mugimendu optimoa egiteko, %50 ausazkoa
                if (AUSAZKOA.nextBoolean()) {
                    return kalkulatuMinimaxOptimua(taula, bota);
                } else {
                    return hutsik.get(AUSAZKOA.nextInt(hutsik.size()));
                }

            case GARAIEZINA:
            default:
                return kalkulatuMinimaxOptimua(taula, bota);
        }
    }

    /**
     * Minimax algoritmo hutsaren bidez mugimendurik onena zehazten du.
     */
    private static int[] kalkulatuMinimaxOptimua(char[][] taula, char bota) {
        int puntuaziorikOnena = Integer.MIN_VALUE;
        int[] mugimendurikOnena = null;

        List<int[]> hutsik = lortuHutsikDaudenGelaxkak(taula);

        for (int[] pos : hutsik) {
            int i = pos[0];
            int j = pos[1];

            // Mugimendua simulatu
            taula[i][j] = bota;
            // Minimax deitu aurkariaren txandarako (isMaximizing = false)
            int puntuazioa = minimax(taula, 0, false, bota);
            // Simulazioa desegin
            taula[i][j] = TicTacToeModel.HUTSA;

            if (puntuazioa > puntuaziorikOnena) {
                puntuaziorikOnena = puntuazioa;
                mugimendurikOnena = new int[]{i, j};
            }
        }

        return (mugimendurikOnena != null) ? mugimendurikOnena : hutsik.get(0);
    }

    /**
     * Minimax algoritmo errekurtsiboa sakonera-zigorrarekin (depth penalty).
     *
     * @param taula Simulatzen ari den taularen egoera.
     * @param depth Errekurtsioaren sakonera maila.
     * @param isMaximizing true botaren txanda bada (baliorik altuena bilatu); false aurkariarena bada.
     * @param bota Botaren ikurra ('O').
     * @return Taularen balio kuantitatiboa.
     */
    private static int minimax(char[][] taula, int depth, boolean isMaximizing, char bota) {
        char aurkaria = (bota == TicTacToeModel.JOKALARI_X) ? TicTacToeModel.JOKALARI_O : TicTacToeModel.JOKALARI_X;
        char irabazlea = TicTacToeModel.egiaztatuIrabazleaTaulan(taula);

        // Oinarrizko kasuak (egoera terminalak)
        if (irabazlea == bota) {
            return 10 - depth; // Zenbat eta azkarrago irabazi, orduan eta puntuazio hobea
        }
        if (irabazlea == aurkaria) {
            return -10 + depth; // Garaipen berantiarra lehenetsi galtzea saihesteko
        }
        if (TicTacToeModel.taulaBeteaDago(taula)) {
            return 0; // Berdinketa
        }

        List<int[]> hutsik = lortuHutsikDaudenGelaxkak(taula);

        if (isMaximizing) {
            int puntuaziorikOnena = Integer.MIN_VALUE;
            for (int[] pos : hutsik) {
                taula[pos[0]][pos[1]] = bota;
                int balioa = minimax(taula, depth + 1, false, bota);
                taula[pos[0]][pos[1]] = TicTacToeModel.HUTSA;
                puntuaziorikOnena = Math.max(puntuaziorikOnena, balioa);
            }
            return puntuaziorikOnena;
        } else {
            int puntuaziorikBaxuena = Integer.MAX_VALUE;
            for (int[] pos : hutsik) {
                taula[pos[0]][pos[1]] = aurkaria;
                int balioa = minimax(taula, depth + 1, true, bota);
                taula[pos[0]][pos[1]] = TicTacToeModel.HUTSA;
                puntuaziorikBaxuena = Math.min(puntuaziorikBaxuena, balioa);
            }
            return puntuaziorikBaxuena;
        }
    }

    /**
     * Jokalariari (edozeini) aholku adimenduna (Smart Hint) kalkulatzen dio.
     * Minimax algoritmoa jokalari horren ikuspuntutik exekutatzen du.
     *
     * @param taula Uneko taula.
     * @param jokalaria Aholkua eskatzen duen jokalaria ('X').
     * @return int[2] gomendatutako koordenatuak.
     */
    public static int[] lortuAholkuAdimentsua(char[][] taula, char jokalaria) {
        return kalkulatuMinimaxOptimua(taula, jokalaria);
    }

    /**
     * Hutsik dauden gelaxken zerrenda itzultzen du.
     */
    private static List<int[]> lortuHutsikDaudenGelaxkak(char[][] taula) {
        List<int[]> zerrenda = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (taula[i][j] == TicTacToeModel.HUTSA) {
                    zerrenda.add(new int[]{i, j});
                }
            }
        }
        return zerrenda;
    }
}
