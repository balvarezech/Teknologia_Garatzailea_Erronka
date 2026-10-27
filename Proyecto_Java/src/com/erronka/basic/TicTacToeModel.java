package com.erronka.basic;

/**
 * TicTacToeModel: Hiru Lerroan jokoaren egoera eta negozio-logika kudeatzen dituen klasea.
 * MVC arkitekturaren eredu garbia: ez du inolako GUI edo Swing menpekotasunik.
 */
public class TicTacToeModel {

    // Jokoaren egoera posibleren enumerazioa
    public enum JokoEgoera {
        JOKATZEN,
        X_IRABAZLE,
        O_IRABAZLE,
        BERDINKETA
    }

    // Konstantanteak
    public static final char HUTSA = ' ';
    public static final char JOKALARI_X = 'X'; // Gizakia
    public static final char JOKALARI_O = 'O'; // Adimen Artifiziala (Bota)

    // Taularen matrizea (3x3)
    private final char[][] taula;
    private char unekoTxanda;
    private JokoEgoera egoera;

    /**
     * Ereduaren hasieratzailea.
     */
    public TicTacToeModel() {
        this.taula = new char[3][3];
        berrezarriJokoa();
    }

    /**
     * Taula garbitu eta hasierako egoerara itzultzen du.
     */
    public void berrezarriJokoa() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                taula[i][j] = HUTSA;
            }
        }
        this.unekoTxanda = JOKALARI_X;
        this.egoera = JokoEgoera.JOKATZEN;
    }

    /**
     * Mugimendu bat egiten saiatzen da emandako koordenatuetan.
     *
     * @param errenkada 0 eta 2 arteko errenkada indizea.
     * @param zutabea 0 eta 2 arteko zutabe indizea.
     * @return true mugimendua baliozkoa bada eta egin bada; false bestela.
     */
    public boolean eginMugimendua(int errenkada, int zutabea) {
        if (egoera != JokoEgoera.JOKATZEN) {
            return false;
        }

        if (errenkada < 0 || errenkada >= 3 || zutabea < 0 || zutabea >= 3) {
            return false;
        }

        if (taula[errenkada][zutabea] != HUTSA) {
            return false;
        }

        // Fitxa ezarri
        taula[errenkada][zutabea] = unekoTxanda;

        // Egoera berria egiaztatu
        eguneratuJokoEgoera();

        // Oraindik jokatzen bada, txanda aldatu
        if (egoera == JokoEgoera.JOKATZEN) {
            unekoTxanda = (unekoTxanda == JOKALARI_X) ? JOKALARI_O : JOKALARI_X;
        }

        return true;
    }

    /**
     * Jokoaren egoera aztertzen du (garaipena, berdinketa edo jokatzen jarraitzea).
     */
    private void eguneratuJokoEgoera() {
        char irabazlea = egiaztatuIrabazleaTaulan(this.taula);
        if (irabazlea == JOKALARI_X) {
            this.egoera = JokoEgoera.X_IRABAZLE;
        } else if (irabazlea == JOKALARI_O) {
            this.egoera = JokoEgoera.O_IRABAZLE;
        } else if (taulaBeteaDago(this.taula)) {
            this.egoera = JokoEgoera.BERDINKETA;
        } else {
            this.egoera = JokoEgoera.JOKATZEN;
        }
    }

    /**
     * Taula batean irabazlerik dagoen aztertzen du estatistikoki.
     * Metodo estatikoa, Minimax algoritmoak erraz simulatu ahal izateko.
     */
    public static char egiaztatuIrabazleaTaulan(char[][] t) {
        // Errenkadak aztertu
        for (int i = 0; i < 3; i++) {
            if (t[i][0] != HUTSA && t[i][0] == t[i][1] && t[i][1] == t[i][2]) {
                return t[i][0];
            }
        }

        // Zutabeak aztertu
        for (int j = 0; j < 3; j++) {
            if (t[0][j] != HUTSA && t[0][j] == t[1][j] && t[1][j] == t[2][j]) {
                return t[0][j];
            }
        }

        // Diagonal nagusia (\)
        if (t[0][0] != HUTSA && t[0][0] == t[1][1] && t[1][1] == t[2][2]) {
            return t[0][0];
        }

        // Alderantzizko diagonala (/)
        if (t[0][2] != HUTSA && t[0][2] == t[1][1] && t[1][1] == t[2][0]) {
            return t[0][2];
        }

        return HUTSA;
    }

    /**
     * Taula guztiz beteta dagoen aztertzen du.
     */
    public static boolean taulaBeteaDago(char[][] t) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (t[i][j] == HUTSA) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Taularen kopia sakona itzultzen du, kapsulazioa mantentzeko.
     */
    public char[][] getTaularenKopia() {
        char[][] kopia = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(this.taula[i], 0, kopia[i], 0, 3);
        }
        return kopia;
    }

    // Getterrak
    public char getGelaxka(int errenkada, int zutabea) {
        return taula[errenkada][zutabea];
    }

    public char getUnekoTxanda() {
        return unekoTxanda;
    }

    public JokoEgoera getEgoera() {
        return egoera;
    }
}
