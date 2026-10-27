package com.erronka.intermediate;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Connect4AI: Konektatu 4 jokorako Bot Heuristikoa.
 * Ebaluazio-funtzio aurreratu bat erabiltzen du taularen egoerak baloratzeko:
 * - Garaipen zuzenak lehenetsi (+10.000)
 * - Aurkariaren garaipen zuzenak blokeatu (+5.000)
 * - 4ko leiho lerragarriak baloratu (3 fitxa, 2 fitxa)
 * - Erdialdeko zutabea estrategikoki baloratu (+8 fitxako)
 * - Tranpak saihestu (aurkariari goiko gelaxkan garaipena ez oparitu, -4.000)
 */
public class Connect4AI {

    /**
     * Botearentzako zutaberik egokiena kalkulatzen du heuristika bidez.
     *
     * @param eredua Uneko jokoaren eredua.
     * @return 0 eta 6 arteko zutabe indize hoberena.
     */
    public static int kalkulatuMugimendurikOnena(Connect4Model eredua) {
        int[][] taula = eredua.getTaularenKopia();

        // 1. ZUTABE BALIOZKOAK BILATU
        List<Integer> zutabeBaliozkoak = new ArrayList<>();
        for (int c = 0; c < Connect4Model.ZUTABEAK; c++) {
            if (!zutabeaBeteaDago(taula, c)) {
                zutabeBaliozkoak.add(c);
            }
        }

        if (zutabeBaliozkoak.isEmpty()) {
            return -1;
        }

        // 2. LEHENTASUN KRITIKOA 1: AIak berehala irabaz dezakeen egiaztatu
        for (int c : zutabeBaliozkoak) {
            int r = simulatuErorketa(taula, c, Connect4Model.JOKALARI_IA);
            if (r != -1) {
                if (Connect4Model.bilatu4koLerroa(taula, Connect4Model.JOKALARI_IA) != null) {
                    taula[r][c] = Connect4Model.HUTSA; // Simulazioa desegin
                    return c; // Berehalako garaipena!
                }
                taula[r][c] = Connect4Model.HUTSA;
            }
        }

        // 3. LEHENTASUN KRITIKOA 2: Aurkariak hurrengo txandan irabaztea saihestu (BLOKEOA)
        for (int c : zutabeBaliozkoak) {
            int r = simulatuErorketa(taula, c, Connect4Model.JOKALARI_GIZAKIA);
            if (r != -1) {
                if (Connect4Model.bilatu4koLerroa(taula, Connect4Model.JOKALARI_GIZAKIA) != null) {
                    taula[r][c] = Connect4Model.HUTSA; // Simulazioa desegin
                    return c; // Beharrezko defentsa!
                }
                taula[r][c] = Connect4Model.HUTSA;
            }
        }

        // 4. EBALUAZIO HEURISTIKO OROKORRA
        int puntuaziorikAltueena = Integer.MIN_VALUE;
        int zutaberikOnena = zutabeBaliozkoak.get(0);

        for (int c : zutabeBaliozkoak) {
            int r = simulatuErorketa(taula, c, Connect4Model.JOKALARI_IA);
            if (r != -1) {
                int puntuazioa = ebaluatuTaula(taula, Connect4Model.JOKALARI_IA);

                // TRANPEN ZIGORRA: Aurkariari berehala gainean irabazteko aukera uzten bazaio
                if (r - 1 >= 0) {
                    taula[r - 1][c] = Connect4Model.JOKALARI_GIZAKIA;
                    if (Connect4Model.bilatu4koLerroa(taula, Connect4Model.JOKALARI_GIZAKIA) != null) {
                        puntuazioa -= 4000; // Zigor gogorra tranpa saihesteko
                    }
                    taula[r - 1][c] = Connect4Model.HUTSA;
                }

                taula[r][c] = Connect4Model.HUTSA; // Simulazioa desegin

                if (puntuazioa > puntuaziorikAltueena) {
                    puntuaziorikAltueena = puntuazioa;
                    zutaberikOnena = c;
                }
            }
        }

        return zutaberikOnena;
    }

    /**
     * Taula baten kalitatea kuantifikatzen duen funtzio heuristikoa.
     */
    private static int ebaluatuTaula(int[][] t, int bota) {
        int puntuazioa = 0;
        int aurkaria = (bota == Connect4Model.JOKALARI_IA) ? Connect4Model.JOKALARI_GIZAKIA : Connect4Model.JOKALARI_IA;

        // 1. Erdialdeko zutabeari (3. zutabea) bonusa eman
        int erdialdekoKopurua = 0;
        for (int r = 0; r < Connect4Model.ERRENKADAK; r++) {
            if (t[r][3] == bota) {
                erdialdekoKopurua++;
            }
        }
        puntuazioa += erdialdekoKopurua * 8;

        // 2. Leiho lerragarriak baloratu (4 gelaxkako multzoak)
        // Horizontalak
        for (int r = 0; r < Connect4Model.ERRENKADAK; r++) {
            for (int c = 0; c <= Connect4Model.ZUTABEAK - 4; c++) {
                int[] leihoa = {t[r][c], t[r][c + 1], t[r][c + 2], t[r][c + 3]};
                puntuazioa += ebaluatuLeihoa(leihoa, bota, aurkaria);
            }
        }

        // Bertikalak
        for (int r = 0; r <= Connect4Model.ERRENKADAK - 4; r++) {
            for (int c = 0; c < Connect4Model.ZUTABEAK; c++) {
                int[] leihoa = {t[r][c], t[r + 1][c], t[r + 2][c], t[r + 3][c]};
                puntuazioa += ebaluatuLeihoa(leihoa, bota, aurkaria);
            }
        }

        // Diagonalak beherantz (\)
        for (int r = 0; r <= Connect4Model.ERRENKADAK - 4; r++) {
            for (int c = 0; c <= Connect4Model.ZUTABEAK - 4; c++) {
                int[] leihoa = {t[r][c], t[r + 1][c + 1], t[r + 2][c + 2], t[r + 3][c + 3]};
                puntuazioa += ebaluatuLeihoa(leihoa, bota, aurkaria);
            }
        }

        // Diagonalak gorantz (/)
        for (int r = 3; r < Connect4Model.ERRENKADAK; r++) {
            for (int c = 0; c <= Connect4Model.ZUTABEAK - 4; c++) {
                int[] leihoa = {t[r][c], t[r - 1][c + 1], t[r - 2][c + 2], t[r - 3][c + 3]};
                puntuazioa += ebaluatuLeihoa(leihoa, bota, aurkaria);
            }
        }

        return puntuazioa;
    }

    /**
     * 4 gelaxkako leiho baten arriskua edo potentziala kalkulatzen du.
     */
    private static int ebaluatuLeihoa(int[] leihoa, int bota, int aurkaria) {
        int botaKop = 0;
        int aurkariKop = 0;
        int hutsKop = 0;

        for (int balioa : leihoa) {
            if (balioa == bota) botaKop++;
            else if (balioa == aurkaria) aurkariKop++;
            else hutsKop++;
        }

        int puntuak = 0;
        if (botaKop == 4) {
            puntuak += 10000;
        } else if (botaKop == 3 && hutsKop == 1) {
            puntuak += 120;
        } else if (botaKop == 2 && hutsKop == 2) {
            puntuak += 20;
        }

        // Aurkariaren mehatxua baloratu
        if (aurkariKop == 3 && hutsKop == 1) {
            puntuak -= 100;
        }

        return puntuak;
    }

    /**
     * Zutabe batean fitxa botatzea simulatzen du taulan zuzenean.
     */
    private static int simulatuErorketa(int[][] t, int c, int jokalaria) {
        for (int r = Connect4Model.ERRENKADAK - 1; r >= 0; r--) {
            if (t[r][c] == Connect4Model.HUTSA) {
                t[r][c] = jokalaria;
                return r;
            }
        }
        return -1;
    }

    private static boolean zutabeaBeteaDago(int[][] t, int c) {
        return t[0][c] != Connect4Model.HUTSA;
    }
}
