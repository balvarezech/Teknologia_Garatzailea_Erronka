package com.erronka.advanced;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * SnakeAI: Snake 2D jokorako Auto-Play nabigazio adimentsua.
 * BFS (Zabalerako Bilaketa) erabiltzen du sagarrerainoko bide laburrena aurkitzeko,
 * eta Flood Fill heuristika bide zuzenik ez dagoenean biziraupena bermatzeko (espazio libre handiena bilatuz).
 */
public class SnakeAI {

    /**
     * Sugeak eman behar duen hurrengo norabide optimoa kalkulatzen du.
     *
     * @param eredua Jokoaren uneko egoera.
     * @return Norabide optimoa (GORA, BEHERA, EZKERRERA, ESKUINERA).
     */
    public static SnakeModel.Norabidea kalkulatuHurrengoNorabidea(SnakeModel eredua) {
        LinkedList<Point> sugea = eredua.getSugearenKopia();
        if (sugea.isEmpty()) {
            return eredua.getUnekoNorabidea();
        }

        Point burua = sugea.getFirst();
        Point sagarra = eredua.getSagarra();
        SnakeModel.Norabidea unekoNora = eredua.getUnekoNorabidea();

        // 1. Gorputz osoa oztopo gisa definitu (azken buztana salbu, hurrengo pausoan mugituko delako)
        Set<Point> oztopoak = new HashSet<>();
        for (int i = 0; i < sugea.size() - 1; i++) {
            oztopoak.add(sugea.get(i));
        }

        // 2. BFS erabiliz sagarrerainoko bide laburrena bilatu
        List<Point> bidea = bilatuBideaBFS(burua, sagarra, oztopoak);

        if (bidea != null && bidea.size() >= 2) {
            Point hurrengoGelaxka = bidea.get(1);
            SnakeModel.Norabidea proposatua = puntuToNorabidea(burua, hurrengoGelaxka);

            // Bideak tranpa batera ez garamatzala egiaztatu (Flood Fill proba txikia)
            if (espazioaNahikoaDa(hurrengoGelaxka, oztopoak, sugea.size())) {
                return proposatua;
            }
        }

        // 3. SAGARRARAINO BIDERIK EZ BADAGO EDO ARRISKUTSUA BADA:
        // Biziraupen-heuristika: bizilagun seguruen artean espazio libre handiena eskaintzen duena aukeratu
        return aukeratuEspazioLibreHandiena(burua, oztopoak, unekoNora);
    }

    /**
     * BFS bidez bi punturen arteko biderik laburrena kalkulatzen du sareta barruan.
     */
    private static List<Point> bilatuBideaBFS(Point hasiera, Point helburua, Set<Point> oztopoak) {
        Queue<Point> ilara = new ArrayDeque<>();
        Map<Point, Point> aurrekoak = new HashMap<>();
        Set<Point> bisitatuak = new HashSet<>();

        ilara.add(hasiera);
        bisitatuak.add(hasiera);

        boolean aurkitua = false;

        while (!ilara.isEmpty()) {
            Point unekoa = ilara.poll();

            if (unekoa.equals(helburua)) {
                aurkitua = true;
                break;
            }

            for (SnakeModel.Norabidea n : SnakeModel.Norabidea.values()) {
                Point bizilaguna = new Point(unekoa.x + n.dx, unekoa.y + n.dy);

                if (posizioaBaliozkoaDa(bizilaguna) && !oztopoak.contains(bizilaguna) && !bisitatuak.contains(bizilaguna)) {
                    bisitatuak.add(bizilaguna);
                    aurrekoak.put(bizilaguna, unekoa);
                    ilara.add(bizilaguna);
                }
            }
        }

        if (!aurkitua) {
            return null;
        }

        // Bidea berreraiki helburutik hasieraraino
        List<Point> bidea = new ArrayList<>();
        Point kurtsorea = helburua;
        while (kurtsorea != null) {
            bidea.add(kurtsorea);
            kurtsorea = aurrekoak.get(kurtsorea);
        }
        Collections.reverse(bidea);
        return bidea;
    }

    /**
     * Bizilagun seguruen artean konektatutako azalera libre handiena duena hautatzen du (Flood Fill bidez).
     */
    private static SnakeModel.Norabidea aukeratuEspazioLibreHandiena(Point burua, Set<Point> oztopoak, SnakeModel.Norabidea unekoa) {
        SnakeModel.Norabidea onena = null;
        int espazioGehien = -1;

        for (SnakeModel.Norabidea n : SnakeModel.Norabidea.values()) {
            // Kontrako norabidera ezin da berehala biratu
            if (unekoa.daKontrakoa(n)) {
                continue;
            }

            Point bizilaguna = new Point(burua.x + n.dx, burua.y + n.dy);
            if (posizioaBaliozkoaDa(bizilaguna) && !oztopoak.contains(bizilaguna)) {
                int espazioa = kalkulatuAzaleraLibrea(bizilaguna, oztopoak);
                if (espazioa > espazioGehien) {
                    espazioGehien = espazioa;
                    onena = n;
                }
            }
        }

        return (onena != null) ? onena : unekoa;
    }

    /**
     * Flood Fill bidez irisgarri dagoen gelaxka libreen kopurua zenbatzen du.
     */
    private static int kalkulatuAzaleraLibrea(Point hasiera, Set<Point> oztopoak) {
        Queue<Point> ilara = new ArrayDeque<>();
        Set<Point> bisitatuak = new HashSet<>(oztopoak);

        ilara.add(hasiera);
        bisitatuak.add(hasiera);

        int kopurua = 0;
        int maxEgiaztapenak = 120; // Azkartasun muga segurua (<1ms)

        while (!ilara.isEmpty() && kopurua < maxEgiaztapenak) {
            Point p = ilara.poll();
            kopurua++;

            for (SnakeModel.Norabidea n : SnakeModel.Norabidea.values()) {
                Point ondokoa = new Point(p.x + n.dx, p.y + n.dy);
                if (posizioaBaliozkoaDa(ondokoa) && !bisitatuak.contains(ondokoa)) {
                    bisitatuak.add(ondokoa);
                    ilara.add(ondokoa);
                }
            }
        }
        return kopurua;
    }

    private static boolean espazioaNahikoaDa(Point hasiera, Set<Point> oztopoak, int sugeLuzera) {
        int espazioLibrea = kalkulatuAzaleraLibrea(hasiera, oztopoak);
        return espazioLibrea >= Math.min(sugeLuzera, 20);
    }

    private static boolean posizioaBaliozkoaDa(Point p) {
        return p.x >= 0 && p.x < SnakeModel.SARETA_TAMAINA && p.y >= 0 && p.y < SnakeModel.SARETA_TAMAINA;
    }

    private static SnakeModel.Norabidea puntuToNorabidea(Point a, Point b) {
        int dx = b.x - a.x;
        int dy = b.y - a.y;
        for (SnakeModel.Norabidea n : SnakeModel.Norabidea.values()) {
            if (n.dx == dx && n.dy == dy) {
                return n;
            }
        }
        return SnakeModel.Norabidea.ESKUINERA;
    }
}
