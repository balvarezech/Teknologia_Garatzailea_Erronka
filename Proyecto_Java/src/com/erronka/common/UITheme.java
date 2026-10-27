package com.erronka.common;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * UITheme: Aplikazio osoko estilo bisualak, koloreak eta tipografiak
 * zentralizatzen dituen laguntza-klasea.
 * Euskarazko iruzkinekin eta interfaze ilun moderno batekin diseinatua.
 */
public final class UITheme {

    // Eraikitzaile pribatua klase estatikoa delako
    private UITheme() {}

    // Koloreen paleta nagusia (Dark Slate & Neon Accent)
    public static final Color ATZEALDE_NAGUSIA = new Color(15, 23, 42);      // #0F172A (Arbel iluna)
    public static final Color TXARTEL_ATZEALDEA = new Color(30, 41, 59);    // #1E293B (Arbel ertaina)
    public static final Color PANEL_ATZEALDEA = new Color(51, 65, 85);      // #334155 (Arbel argia)
    public static final Color TESTU_ZURIA = new Color(248, 250, 252);       // #F8FAFC
    public static final Color TESTU_MUTUA = new Color(148, 163, 184);       // #94A3B8

    // Nabarmendutako koloreak (Joko bakoitzerako bereizgarriak)
    public static final Color ZIAN_DISTIRA = new Color(6, 182, 212);        // #06B6D4 (1. Jokoa - Hiru Lerroan)
    public static final Color AMBAR_HORIA = new Color(245, 158, 11);        // #F59E0B (2. Jokoa - Konektatu 4)
    public static final Color ESMERALDA_BERDEA = new Color(16, 185, 129);   // #10B981 (3. Jokoa - Snake 2D)
    public static final Color GORRI_BIZIA = new Color(239, 68, 68);         // #EF4444 (Arriskua / Garaipena)
    public static final Color MORE_ARGIA = new Color(168, 85, 247);         // #A855F7 (Modu adimenduna / IA)

    // Letra-tipo estandarrak
    public static final Font LETRA_TITULUA = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font LETRA_AZPITITULUA = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font LETRA_ARRUNTA = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font LETRA_BOTOIA = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font LETRA_JOKOA_IKURRA = new Font("Segoe UI", Font.BOLD, 46);

    /**
     * Botoi bati estilo moderno eta laua aplikatzen dio, hover efektuekin.
     *
     * @param botoia Estilizatu nahi den JButton-a.
     * @param atzekoKolorea Botoiaren oinarrizko kolorea.
     */
    public static void estilizatuBotoia(JButton botoia, Color atzekoKolorea) {
        botoia.setBackground(atzekoKolorea);
        botoia.setForeground(TESTU_ZURIA);
        botoia.setFont(LETRA_BOTOIA);
        botoia.setFocusPainted(false);
        botoia.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        botoia.setOpaque(true);

        // Saguaren gaineko efektu grafikoa (Hover)
        botoia.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botoia.setBackground(atzekoKolorea.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botoia.setBackground(atzekoKolorea);
            }
        });
    }

    /**
     * Ertz biribildu garbi bat sortzen du txarteletarako.
     */
    public static Border sortuTxartelErtza(Color ertzekoKolorea) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ertzekoKolorea, 1),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        );
    }
}
