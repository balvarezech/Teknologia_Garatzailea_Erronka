package com.erronka.intermediate;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Connect4Model: Konektatu 4 jokoaren egoera eta taularen logika kudeatzen dituen klasea.
 * 6 errenkada eta 7 zutabeko matrizea grabitate fisikoarekin kudeatzen du.
 * MVC arkitekturan oinarrituta, ez du Swing edo bistako logikarik nahasten.
 */
public class Connect4Model {

    public static final int ERRENKADAK = 6;
    public static final int ZUTABEAK = 7;

    public static final int HUTSA = 0;
    public static final int JOKALARI_GIZAKIA = 1; // Gorria
    public static final int JOKALARI_IA = 2;       // Horia

    public enum JokoEgoera {
        JOKATZEN,
        GIZAKIA_IRABAZLE,
        IA_IRABAZLE,
        BERDINKETA
    }

    private final int[][] taula;
    private int unekoTxanda;
    private JokoEgoera egoera;
    private final List<Point> irabazlePosizioak;

    public Connect4Model() {
        this.taula = new int[ERRENKADAK][ZUTABEAK];
        this.irabazlePosizioak = new ArrayList<>();
        berrezarriJokoa();
    }

    /**
     * Taula garbitu eta hasierako txandara itzultzen da.
     */
    public void berrezarriJokoa() {
        for (int r = 0; r < ERRENKADAK; r++) {
            for (int c = 0; c < ZUTABEAK; c++) {
                taula[r][c] = HUTSA;
            }
        }
        this.unekoTxanda = JOKALARI_GIZAKIA;
        this.egoera = JokoEgoera.JOKATZEN;
        this.irabazlePosizioak.clear();
    }

    /**
     * Zutabe batek fitxa gehiago onartzen dituen aztertzen du.
     */
    public boolean zutabeaBeteaDago(int zutabea) {
        if (zutabea < 0 || zutabea >= ZUTABEAK) {
            return true;
        }
        return taula[0][zutabea] != HUTSA;
    }

    /**
     * Fitxa bat zutabe batean erortzen uzten du (grabitate-logika).
     *
     * @param zutabea Fitxa sartzeko zutabea (0-6).
     * @param jokalaria Zein jokalarik botatzen duen (1 edo 2).
     * @return Fitxa geratu den errenkada-indizea, edo -1 zutabea beteta badago.
     */
    public int botaFitxa(int zutabea, int jokalaria) {
        if (zutabea < 0 || zutabea >= ZUTABEAK || zutabeaBeteaDago(zutabea) || egoera != JokoEgoera.JOKATZEN) {
            return -1;
        }

        // Behetik gora hutsik dagoen lehen gelaxka bilatu
        for (int r = ERRENKADAK - 1; r >= 0; r--) {
            if (taula[r][zutabea] == HUTSA) {
                taula[r][zutabea] = jokalaria;
                eguneratuJokoEgoera(jokalaria);
                if (egoera == JokoEgoera.JOKATZEN) {
                    unekoTxanda = (unekoTxanda == JOKALARI_GIZAKIA) ? JOKALARI_IA : JOKALARI_GIZAKIA;
                }
                return r;
            }
        }
        return -1;
    }

    /**
     * Egiaztatzen du jokalari horrek irabazi duen ala taula bete den.
     */
    private void eguneratuJokoEgoera(int azkenJokalaria) {
        List<Point> irabazleak = bilatu4koLerroa(this.taula, azkenJokalaria);
        if (irabazleak != null && !irabazleak.isEmpty()) {
            this.irabazlePosizioak.clear();
            this.irabazlePosizioak.addAll(irabazleak);
            this.egoera = (azkenJokalaria == JOKALARI_GIZAKIA) ? JokoEgoera.GIZAKIA_IRABAZLE : JokoEgoera.IA_IRABAZLE;
            return;
        }

        // Taula betea dagoen aztertu
        boolean betea = true;
        for (int c = 0; c < ZUTABEAK; c++) {
            if (taula[0][c] == HUTSA) {
                betea = false;
                break;
            }
        }

        if (betea) {
            this.egoera = JokoEgoera.BERDINKETA;
        } else {
            this.egoera = JokoEgoera.JOKATZEN;
        }
    }

    /**
     * Taula batean 4 lerroan dauden aztertzen du estatistikoki 4 noranzkoetan.
     * Metodo estatikoa, bota simulatzerakoan berrerabili ahal izateko.
     *
     * @param t Aztertzeko matrizea.
     * @param p Jokalariaren zenbakia (1 edo 2).
     * @return 4 puntuen zerrenda, edo null ez badago 4 lerroan.
     */
    public static List<Point> bilatu4koLerroa(int[][] t, int p) {
        // 1. Horizontala (-)
        for (int r = 0; r < ERRENKADAK; r++) {
            for (int c = 0; c <= ZUTABEAK - 4; c++) {
                if (t[r][c] == p && t[r][c + 1] == p && t[r][c + 2] == p && t[r][c + 3] == p) {
                    List<Point> pts = new ArrayList<>();
                    pts.add(new Point(c, r));
                    pts.add(new Point(c + 1, r));
                    pts.add(new Point(c + 2, r));
                    pts.add(new Point(c + 3, r));
                    return pts;
                }
            }
        }

        // 2. Bertikala (|)
        for (int r = 0; r <= ERRENKADAK - 4; r++) {
            for (int c = 0; c < ZUTABEAK; c++) {
                if (t[r][c] == p && t[r + 1][c] == p && t[r + 2][c] == p && t[r + 3][c] == p) {
                    List<Point> pts = new ArrayList<>();
                    pts.add(new Point(c, r));
                    pts.add(new Point(c, r + 1));
                    pts.add(new Point(c, r + 2));
                    pts.add(new Point(c, r + 3));
                    return pts;
                }
            }
        }

        // 3. Diagonala beherantz (\)
        for (int r = 0; r <= ERRENKADAK - 4; r++) {
            for (int c = 0; c <= ZUTABEAK - 4; c++) {
                if (t[r][c] == p && t[r + 1][c + 1] == p && t[r + 2][c + 2] == p && t[r + 3][c + 3] == p) {
                    List<Point> pts = new ArrayList<>();
                    pts.add(new Point(c, r));
                    pts.add(new Point(c + 1, r + 1));
                    pts.add(new Point(c + 2, r + 2));
                    pts.add(new Point(c + 3, r + 3));
                    return pts;
                }
            }
        }

        // 4. Diagonala gorantz (/)
        for (int r = 3; r < ERRENKADAK; r++) {
            for (int c = 0; c <= ZUTABEAK - 4; c++) {
                if (t[r][c] == p && t[r - 1][c + 1] == p && t[r - 2][c + 2] == p && t[r - 3][c + 3] == p) {
                    List<Point> pts = new ArrayList<>();
                    pts.add(new Point(c, r));
                    pts.add(new Point(c + 1, r - 1));
                    pts.add(new Point(c + 2, r - 2));
                    pts.add(new Point(c + 3, r - 3));
                    return pts;
                }
            }
        }

        return null;
    }

    /**
     * Taularen kopia sakona itzultzen du.
     */
    public int[][] getTaularenKopia() {
        int[][] kopia = new int[ERRENKADAK][ZUTABEAK];
        for (int r = 0; r < ERRENKADAK; r++) {
            System.arraycopy(this.taula[r], 0, kopia[r], 0, ZUTABEAK);
        }
        return kopia;
    }

    // Getterrak
    public int getGelaxka(int r, int c) {
        return taula[r][c];
    }

    public int getUnekoTxanda() {
        return unekoTxanda;
    }

    public JokoEgoera getEgoera() {
        return egoera;
    }

    public List<Point> getIrabazlePosizioak() {
        return new ArrayList<>(irabazlePosizioak);
    }
}
