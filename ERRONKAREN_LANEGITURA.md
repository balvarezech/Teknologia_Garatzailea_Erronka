# Erronka: Java + IA Minijokoen Garapena
## Lan-Plangintza eta Gida Operatiboa (9 Lan Ordu — 3 Ikasle)

---

### 1. Sarrera eta Estrategia Orokorra

Erronka honen helburua **Java hutsean eta Swing erabiliz 3 minijoko dituen software-multzo oso bat garatzea da, Adimen Artifizialaren laguntzarekin**. 

Ebaluazio-errubrikan **10/10 puntuak** lortzeko, 3 ikasleek talde bakar batean lan egingo dute modu paraleloan, MVC (Eredua-Bista-Kontrolatzailea) arkitektura errespetatuz, akatsen aurrean sendoa den kodea bermatuz (`try-catch`), eta AIari egindako eskaera eta iterazio guztiak `prompt.txt` fitxategian dokumentatuz.

---

### 2. Jokoen Aukeraketa Estrategikoa eta AI Osagaiak

Rúbrikaren eskakizunak betetzeko eta zuzeneko aurkezpenean ikusgarritasun handia lortzeko proposatutako jokoak:

| Maila | Joko Proposatua | Egitura Teknikoa (Swing / POO) | Osagai Adimenduna / AI (Nahitaezkoa) |
| :--- | :--- | :--- | :--- |
| **1. Oinarrizkoa** | **Hiru Lerroan (Tic-Tac-Toe)** | 3x3 matrizea, botoien gertaerak (`ActionListener`), garaipen/berdinketa estatuak. | **Minimax Algoritmoa**: Zailtasun hautatzailea (Erraza = ausazkoa; Zaila = Minimax garaiezina) eta jokalariarentzako aholku adimendunak. |
| **2. Tartekoa** | **Konektatu 4 (Connect 4)** | 7x6 matrize dinamikoa, grabitate bidezko fitxen jaitsiera, lerrokatzeen egiaztapena (horizontala, bertikala, diagonala). | **Bot Heuristikoa (Ebaluazio-funtzioa)**: Taularen posizioak baloratzen ditu (2 eta 3ko lerroak lehenetsiz), erabiltzailearen garaipenak blokeatzen ditu eta erdialdea kontrolatzen du. |
| **3. Aurreratua** | **Snake 2D (Sugea)** | Denbora errealeko joko-begizta (`Thread` / Game Loop), FPS kontrola, `paintComponent` eta buffer bikoitza. | **Auto-Play Modua (A\* / Bide-bilaketa)**: Botoi bat sakatzean, AIak bere kabuz jokatzen du, hormak eta bere buztana saihestuz sagarrarainoko bide laburrena aurkituz. |

> [!TIP]
> **Abiarazle Nagusia (`GameLauncher`)**: Hiru jokoak biltzen dituen leiho nagusi bakar bat sortuko da Swing bidez. Bertatik joko bakoitza ireki ahal izango da, proiektuari produktu komertzial eta bateratu baten itxura emanez.

---

### 3. Taldearen Lan-Banaketa (3 Ikasleen Rolak)

Denbora optimizatzeko, lanaren zati handiena modu paraleloan egingo da 2. ordutik aurrera:

*   **1. Ikaslea (Integrazio-burua & 1. Ariketa - Oinarrizkoa):**
    *   Proiektuaren hasierako arkitektura eta `GameLauncher` leiho nagusia prestatzea.
    *   **1. Ariketa (Hiru Lerroan + Minimax)** garatzea.
    *   Bukaerako integrazioa, bateragarritasuna eta salbuespenen kudeaketa orokorra gainbegiratzea.
*   **2. Ikaslea (Matrizeen Espezialista & 2. Ariketa - Tartekoa):**
    *   **2. Ariketa (Konektatu 4 + Bot Heuristikoa)** garatzea.
    *   7x6 matrizearen logika, garaipen-baldintzak eta botaren puntuazio-algoritmoa kodetzea.
    *   Swing interfaze grafikoa eta fitxen erorketa diseinatzea.
*   **3. Ikaslea (Denbora Erreala & 3. Ariketa - Aurreratua):**
    *   **3. Ariketa (Snake 2D + Hariak / Threads)** garatzea.
    *   Game Loop begizta kontrolatzea (Swing-eko interfazearen hariarekin talkarik ez egiteko).
    *   AI bidezko nabigazio automatikoa (Auto-Play) inplementatzea.
*   **Zeregin Partekatuak:**
    *   Kide bakoitzak bere prompt-ak eta AIaren iterazioak `prompt.txt` fitxategian gehituko ditu uneoro.
    *   Hiruren artean `Presentacion.pdf` prestatzea eta zuzeneko demoa banatzea.

---

### 4. Denboraren Banaketa Zehatza (9 Lan Ordu)

```
[1. Ordua]  Arkitektura + GameLauncher + prompt.txt egituraketa
    │
[2-3. Orduak] 1. Ariketa: Hiru Lerroan + Minimax + Probak
    │
[4-5. Orduak] 2. Ariketa: Konektatu 4 + Bot Heuristikoa
    │
[6-7. Orduak] 3. Ariketa: Snake 2D + Hariak (Thread) + AI Nabigazioa
    │
[8. Ordua]  Integrazio Orokorra + Try-Catch / Crash-Free Egiaztapena
    │
[9. Ordua]  Aurkezpena (PDF) + Demoren Entsegua + Zip Fitxategia
```

*   **1. Ordua: Planifikazioa eta Azpiegitura**
    *   Paketeen egitura sortzea (`com.erronka...`).
    *   `prompt.txt` erregistroaren txantiloia finkatzea.
    *   `GameLauncher` leiho nagusia programatzea (3 jokoak abiarazteko botoiekin).
*   **2 - 3. Orduak: 1. Ariketa (Oinarrizkoa)**
    *   Hiru Lerroan jokoaren Eredua, Bista eta Kontrolatzailea garatzea.
    *   Minimax algoritmoa txertatzea eta AIarekin iteratzea.
    *   Gertaerak eta berrezarpen-botoiak probatzea.
*   **4 - 5. Orduak: 2. Ariketa (Tartekoa)**
    *   Konektatu 4 jokoaren taula eta grabitate-logika ezartzea.
    *   Bot heuristikoa garatzea (mugimendu adimentsuak kalkulatzeko).
    *   Zutabe beteetan klik egitean gerta daitezkeen akatsak kontrolatzea.
*   **6 - 7. Orduak: 3. Ariketa (Aurreratua)**
    *   Snake jokoaren haria (`Runnable` / `Thread`) eta joko-begizta sortzea.
    *   Gezien bidezko kontrola (`KeyAdapter`) eta talka-detekzioa ezartzea.
    *   Bide-bilaketaren osagai adimenduna (Auto-Play) gehitzea.
*   **8. Ordua: Integrazioa eta Sendotasuna (Crash-Free)**
    *   Hiru jokoak `GameLauncher`-era konektatzea eta elkarrekin exekutatzea.
    *   Erabiltzailearen ezohiko sarrerak `try-catch` bidez babestea (aplikazioa inoiz ez ixteko/apurtzeko).
    *   Terminaletik garbi konpilatzen dela egiaztatzea (`javac` bidez).
*   **9. Ordua: Entregagarriak eta Aurkezpena**
    *   Aurkezpenerako diapositibak prestatzea (PDF formatuan, 4-6 orri).
    *   10 minutuko zuzeneko demoaren txandak entseatzea.
    *   Azken fitxategia sortzea: `Reto_Java_TaldeBakarra.zip` (edo `Reto_Java_GrupoUnico.zip`).

---

### 5. Kodearen Arkitektura eta Karpeta-Egitura

Ebaluazioan POO eta Garbitasunaren atalean (1.5 puntu) emaitzarik onena lortzeko egitura:

```text
Proyecto_Java/
└── src/
    └── com/
        └── erronka/
            ├── Main.java                 <-- Sarrera-puntua eta Abiarazle Orokorra (Swing)
            ├── common/                   <-- Estilo grafikoak, koloreak eta utilitateak
            ├── oinarrizkoa/              <-- 1. Ariketa (Hiru Lerroan)
            │   ├── TicTacToeModel.java   <-- Datuen egoera eta arauak
            │   ├── TicTacToeView.java    <-- Swing interfazea (Botoiak, markagailua)
            │   ├── TicTacToeController.java
            │   └── MinimaxAI.java        <-- Minimax kalkulua eta aholkuak
            ├── tartekoa/                 <-- 2. Ariketa (Konektatu 4)
            │   ├── Connect4Model.java    <-- 7x6 matrizea eta egiaztapenak
            │   ├── Connect4View.java     <-- Taularen marrazketa
            │   ├── Connect4Controller.java
            │   └── HeuristicBot.java     <-- Ponderazio bidezko adimen artifiziala
            └── aurreratua/               <-- 3. Ariketa (Snake 2D)
                ├── SnakeModel.java       <-- Sugearen gorputza eta sagarraren posizioa
                ├── SnakePanel.java       <-- paintComponent bidezko marrazketa dinamikoa
                ├── GameLoop.java         <-- Haria (Thread) eta abiadura-kontrola
                └── PathfindingAI.java    <-- Nabigazio automatiko adimentsua
```

---

### 6. `prompt.txt` Fitxategiaren Protokoloa (2.0 Puntu)

Rúbrikak balio handia ematen dio eskaeren zehaztasunari eta AIarekin izandako **hobekuntza-iterazioei**. Fitxategiak eredu hau jarraitu behar du:

```markdown
================================================================================
ARIKETA: [1. Oinarrizkoa / 2. Tartekoa / 3. Aurreratua]
DATA ETA ORDUA: 2026-10-27 --:--
IKASLE ARDURADUNA: [Ikaslearen Izena]
================================================================================

1. HASIERAKO PROMPT-A (AIari bidalitako agindua):
"Jardun Java Senior garatzaile gisa. Sortu Hiru Lerroan jokorako MinimaxAI klasea Swing MVC 
ereduan txertatzeko. Algoritmoak taularen egoera jaso behar du (char[3][3]) eta mugimendu 
hoberena bueltatu..."

2. AI-AREN ERANTZUNAREN EBALUAZIOA (Zer egin du ondo eta zer falta da):
"Kodeak oinarrizko algoritmoa ondo sortu du, baina garaipen azkarrak eta berantiarrak 
berdin baloratzen ditu sakonera kontuan hartu gabe..."

3. ITERAZIO / REFINEMENT PROMPT-A (Hobekuntza eskaera):
"Aldatu minimax funtzioa sakoneraren araberako zigorra gehitzeko (10 - depth)..."

4. EMAITZA ETA EGOKITZAPENA:
"MinimaxAI.java fitxategian txertatu da eta errore gabe konpilatzen du."
```

---

### 7. Aurkezpenaren Plana (10 Minutu)

Zuzeneko demoa arrakastatsua izan dadin, denbora zehatz banatuko da:

*   **00:00 - 01:00 (1. Ikaslea):** Proiektuaren sarrera, aukeratutako arkitektura (MVC), `GameLauncher` erakutsi.
*   **01:00 - 03:30 (1. Ikaslea):** **1. Ariketaren Demoa**: Gizakia vs Minimax Bota, zailtasun-mailak eta kodearen gakoak.
*   **03:30 - 06:00 (2. Ikaslea):** **2. Ariketaren Demoa**: Konektatu 4 partida, matrizearen kudeaketa eta heuristika nola pentsatzen duen.
*   **06:00 - 08:30 (3. Ikaslea):** **3. Ariketaren Demoa**: Snake 2D denbora errealean, Auto-Play moduaren aktibazioa eta harien (`Thread`) azalpena.
*   **08:30 - 10:00 (Talde Osoa):** Prompt Engineering-aren ondorioak, ikasitako irakaspenak eta galderak erantzutea.

---

### 8. Bidali Beharreko Fitxategiak eta Egiaztapen-Zerrenda

Guztia bukatzean, sortu **`Reto_Java_TaldeBakarra.zip`** fitxategia eduki honekin:

- [ ] `prompt.txt`: 3 ariketen prompt eta iterazio tekniko guztiekin.
- [ ] `Proyecto_Java/`: Iturburu-kode osoa (`src/` karpeta barne), hutsik gabe konpilatzen duena.
- [ ] `Presentacion.pdf`: 4-6 diapositiba dituen aurkezpen-fitxategia.
