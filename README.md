# Teknologia Garatzailea — Java + IA Minijokoen Multzoa

> **Irakasgaia:** Teknologia Berritzaileak  
> **Modalitatea:** Talde Bakarra (3 Ikasle)  
> **Teknologiak:** Java (JDK 21+), Swing GUI, Multi-threading, Minimax, Heuristics, Pathfinding (A*/BFS).

---

## 📌 Proiektuaren Deskribapena / Descripción del Proyecto

Proiektu honek Java hutsean eta Swing bidez garatutako 3 minijoko biltzen ditu, Adimen Artifizialeko algoritmoekin hornituta eta abiarazle grafiko nagusi (*Launcher Hub*) baten bidez integratuta:

1. **1. Ariketa (Oinarrizkoa / Nivel Básico):**
   * **Hiru Lerroan (Tic-Tac-Toe):** Minimax algoritmoa sakonera-zigorrarekin (+10 - depth), 3 zailtasun-maila (Erraza, Ertaina, Garaiezina) eta jokalariarentzako aholku-sistema adimenduna (*Smart Hint*).
2. **2. Ariketa (Tartekoa / Nivel Intermedio):**
   * **Konektatu 4 (Connect 4):** 7x6 matrize dinamikoa grabitatearekin, 4 noranzkoetako garaipen-detekzioa eta bot heuristiko adimenduna (blokeoak, erdialdeko lehentasuna eta tranpak saihestea).
3. **3. Ariketa (Aurreratua / Nivel Avanzado):**
   * **Snake 2D (Sugea):** Denbora errealeko joko-begizta multihiloa (`Thread` / Game Loop), FPS kontrola, flicker-free Swing renderizatua eta **Auto-Play modua** (A* / BFS bidezko bide-bilaketa adimentsua eta Flood Fill biziraupen-heuristika).

---

## 📂 Biltegiaren Egitura / Estructura del Repositorio

```text
├── .gitignore
├── README.md
├── ERRONKAREN_LANEGITURA.md    # 9 orduetako plan operatiboa eta errubrika
├── prompt.txt                  # AIari bidalitako prompt guztiak (Ingelesez) eta ebaluazioak (Euskaraz)
├── Presentacion.pdf            # 10 minutuko aurkezpenerako laguntza-fitxategia
└── Proyecto_Java/              # Java iturburu-kode osoa (MVC patroian)
    └── src/
        └── com/erronka/
            ├── Main.java       # Abiarazle nagusia (Launcher Hub)
            ├── common/         # Estilo grafikoak eta utilitateak
            ├── basic/          # 1. Ariketa (Tic-Tac-Toe + Minimax)
            ├── intermediate/   # 2. Ariketa (Connect 4 + Heuristics)
            └── advanced/       # 3. Ariketa (Snake 2D + Threads + Auto-Play)
```

---

## 🚀 Nola Exekutatu / Cómo Ejecutar

Proiektua exekutatzeko Java JDK 21 edo berriagoa behar da instalatuta:

```bash
# 1. Proiektuaren iturburu-kodera joan
cd Proyecto_Java/src

# 2. Klase guztiak konpilatu
javac -encoding UTF-8 com/erronka/*.java com/erronka/*/*.java

# 3. Abiarazle nagusia exekutatu
java com.erronka.Main
```
