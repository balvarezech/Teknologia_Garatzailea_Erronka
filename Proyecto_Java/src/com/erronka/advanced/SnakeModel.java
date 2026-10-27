package com.erronka.advanced;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

/**
 * SnakeModel: Snake 2D jokoaren datuen egoera eta posizioen kudeatzailea.
 * 25x25 gelaxkako sareta birtuala kudeatzen du.
 * Hariekin modu seguruan lan egiteko sinkronizatutako sarbideak eskaintzen ditu.
 */
public class SnakeModel {

    public static final int SARETA_TAMAINA = 25; // 25x25 gelaxka

    public enum Norabidea {
        GORA(0, -1),
        BEHERA(0, 1),
        EZKERRERA(-1, 0),
        ESKUINERA(1, 0);

        public final int dx;
        public final int dy;

        Norabidea(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public boolean daKontrakoa(Norabidea besteBat) {
            return (this.dx + besteBat.dx == 0) && (this.dy + besteBat.dy == 0);
        }
    }

    private final LinkedList<Point> sugea;
    private Point sagarra;
    private Norabidea unekoNorabidea;
    private Norabidea hurrengoNorabidea;
    private int puntuazioa;
    private int errekorra;
    private boolean amaituta;
    private boolean autoPlayAktibatuta;

    private final Object giltza = new Object();
    private final Random ausazkoa = new Random();

    public SnakeModel() {
        this.sugea = new LinkedList<>();
        this.errekorra = 0;
        berrezarriJokoa();
    }

    /**
     * Jokoa hasierako posiziora berrezartzen du.
     */
    public void berrezarriJokoa() {
        synchronized (giltza) {
            sugea.clear();
            int hasierakoX = SARETA_TAMAINA / 2;
            int hasierakoY = SARETA_TAMAINA / 2;

            // 3 segmentuko hasierako sugea
            sugea.add(new Point(hasierakoX, hasierakoY));
            sugea.add(new Point(hasierakoX - 1, hasierakoY));
            sugea.add(new Point(hasierakoX - 2, hasierakoY));

            this.unekoNorabidea = Norabidea.ESKUINERA;
            this.hurrengoNorabidea = Norabidea.ESKUINERA;
            this.puntuazioa = 0;
            this.amaituta = false;
            sortuSagarra();
        }
    }

    /**
     * Sagarra hutsik dagoen ausazko posizio batean kokatzen du.
     */
    public void sortuSagarra() {
        synchronized (giltza) {
            Point berria;
            do {
                int x = ausazkoa.nextInt(SARETA_TAMAINA);
                int y = ausazkoa.nextInt(SARETA_TAMAINA);
                berria = new Point(x, y);
            } while (sugeaDagoPosizioan(berria.x, berria.y));
            this.sagarra = berria;
        }
    }

    /**
     * Sugearen aurreratze-pauso bat simulatzen du.
     *
     * @return true sagar bat jan badu; false bestela.
     */
    public boolean aurreratu() {
        synchronized (giltza) {
            if (amaituta) {
                return false;
            }

            this.unekoNorabidea = this.hurrengoNorabidea;
            Point burua = sugea.getFirst();
            int berriaX = burua.x + unekoNorabidea.dx;
            int berriaY = burua.y + unekoNorabidea.dy;

            // 1. Hormen aurkako talka
            if (berriaX < 0 || berriaX >= SARETA_TAMAINA || berriaY < 0 || berriaY >= SARETA_TAMAINA) {
                amaituta = true;
                return false;
            }

            // 2. Gorputzaren aurkako talka (azken buztana salbu, aurrera doalako)
            for (int i = 0; i < sugea.size() - 1; i++) {
                Point p = sugea.get(i);
                if (p.x == berriaX && p.y == berriaY) {
                    amaituta = true;
                    return false;
                }
            }

            Point buruBerria = new Point(berriaX, berriaY);
            sugea.addFirst(buruBerria);

            // Sagarra jan den aztertu
            if (buruBerria.equals(sagarra)) {
                puntuazioa += 10;
                if (puntuazioa > errekorra) {
                    errekorra = puntuazioa;
                }
                sortuSagarra();
                return true;
            } else {
                // Sagarrik ez badu jan, buztana kendu luzera mantentzeko
                sugea.removeLast();
                return false;
            }
        }
    }

    public void ezarriNorabidea(Norabidea berria) {
        synchronized (giltza) {
            if (berria != null && !unekoNorabidea.daKontrakoa(berria)) {
                this.hurrengoNorabidea = berria;
            }
        }
    }

    public boolean sugeaDagoPosizioan(int x, int y) {
        for (Point p : sugea) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<Point> getSugearenKopia() {
        synchronized (giltza) {
            return new LinkedList<>(sugea);
        }
    }

    public Point getSagarra() {
        synchronized (giltza) {
            return new Point(sagarra);
        }
    }

    public Norabidea getUnekoNorabidea() {
        synchronized (giltza) {
            return unekoNorabidea;
        }
    }

    public int getPuntuazioa() {
        return puntuazioa;
    }

    public int getErrekorra() {
        return errekorra;
    }

    public boolean isAmaituta() {
        return amaituta;
    }

    public boolean isAutoPlayAktibatuta() {
        return autoPlayAktibatuta;
    }

    public void setAutoPlayAktibatuta(boolean aktibatuta) {
        this.autoPlayAktibatuta = aktibatuta;
    }

    public void txandakatuAutoPlay() {
        this.autoPlayAktibatuta = !this.autoPlayAktibatuta;
    }
}
