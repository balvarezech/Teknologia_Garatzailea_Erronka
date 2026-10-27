package com.erronka.advanced;

import javax.swing.SwingUtilities;

/**
 * GameLoop: Snake 2D jokoaren denbora errealeko joko-begizta multihiloa (Thread / Runnable).
 * Swing-eko Event Dispatch Thread (EDT) haritik erabat bananduta exekutatzen da,
 * interfaze grafikoa inoiz ez blokeatzeko eta errendimendu maximoa lortzeko.
 */
public class GameLoop implements Runnable {

    private final SnakeModel eredua;
    private final Runnable birmarraztuCallback;
    private final Runnable jokoAmaieraCallback;

    private volatile boolean exekutatzen;
    private volatile boolean pausatuta;
    private Thread begiztaHaria;

    private static final int HASIERAKO_ABIADURA_MS = 110;
    private static final int ABIADURA_MAXIMOA_MS = 55;

    public GameLoop(SnakeModel eredua, Runnable birmarraztuCallback, Runnable jokoAmaieraCallback) {
        this.eredua = eredua;
        this.birmarraztuCallback = birmarraztuCallback;
        this.jokoAmaieraCallback = jokoAmaieraCallback;
        this.exekutatzen = false;
        this.pausatuta = false;
    }

    /**
     * Bukaerako haria sortu eta abiarazten du.
     */
    public synchronized void abiarazi() {
        if (!exekutatzen) {
            exekutatzen = true;
            pausatuta = false;
            begiztaHaria = new Thread(this, "Snake-GameLoop-Haria");
            begiztaHaria.setDaemon(true); // Leihoa ixtean haria automatikoki ixteko
            begiztaHaria.start();
        }
    }

    /**
     * Jokoa pausatu edo berriz martxan jartzen du.
     */
    public synchronized void txandakatuPausa() {
        this.pausatuta = !this.pausatuta;
    }

    public boolean isPausatuta() {
        return pausatuta;
    }

    /**
     * Begizta segurtasunez gelditzen du.
     */
    public synchronized void gelditu() {
        exekutatzen = false;
        if (begiztaHaria != null) {
            begiztaHaria.interrupt();
        }
    }

    @Override
    public void run() {
        while (exekutatzen) {
            try {
                if (!pausatuta && !eredua.isAmaituta()) {

                    // 1. Auto-Play aktibatuta badago, IAren bide optimoa aplikatu
                    if (eredua.isAutoPlayAktibatuta()) {
                        SnakeModel.Norabidea aiNorabidea = SnakeAI.kalkulatuHurrengoNorabidea(eredua);
                        eredua.ezarriNorabidea(aiNorabidea);
                    }

                    // 2. Pauso bat aurreratu
                    eredua.aurreratu();

                    // 3. Bista birrenderizatu EDT hariaren bidez
                    if (birmarraztuCallback != null) {
                        SwingUtilities.invokeLater(birmarraztuCallback);
                    }

                    // 4. Jokoa amaitu bada, jakinarazi
                    if (eredua.isAmaituta() && jokoAmaieraCallback != null) {
                        SwingUtilities.invokeLater(jokoAmaieraCallback);
                    }
                }

                // 5. Abiaduraren kalkulua puntuazioaren arabera
                int unekoAbiadura = Math.max(
                    ABIADURA_MAXIMOA_MS,
                    HASIERAKO_ABIADURA_MS - (eredua.getPuntuazioa() / 30) * 4
                );

                Thread.sleep(unekoAbiadura);

            } catch (InterruptedException ex) {
                // Haria gelditzeko seinalea jaso dugu
                break;
            } catch (Exception ex) {
                System.err.println("Errorea GameLoop barruan: " + ex.getMessage());
            }
        }
    }
}
