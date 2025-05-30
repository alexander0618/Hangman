# Jocul Spânzurătoarea în Rețea

Acest proiect este o implementare a jocului clasic **Spânzurătoarea (Hangman)**, care permite desfășurarea unei sesiuni de joc între mai mulți jucători conectați prin rețea. Un jucător acționează ca **gazdă (Host)**, iar ceilalți se conectează în calitate de **clienți** pentru a participa la joc.

## Funcționalități

- Joc multiplayer în rețea, cu alegerea cuvintelor dintr-o listă predefinită (`words.txt`).
- Afișarea progresului grafic al spânzurătorii.
- Scoruri salvate pentru fiecare jucător în fișierul `scores.txt`.
- Jucătorii sunt înregistrați cu IP-ul în `players.txt`.
- Suport pentru reluarea jocului cu aceiași jucători.

## Fișiere Importante

- `Host.java` – codul serverului (gazdă joc).
- `Client.java` – aplicația client cu IP implicit `127.0.0.1`.
- `ClientManual.java` – aplicația client unde IP-ul serverului este introdus manual.
- `words.txt` – lista de cuvinte ce pot fi alese aleator pentru joc.
- `players.txt` – fișier generat automat cu numele și IP-urile jucătorilor.
- `scores.txt` – fișier generat automat cu scorurile jucătorilor după fiecare rundă.

## Cerințe

- JDK 8+ instalat
- Consolă sau IDE compatibilă cu Java

## Cum se rulează

### 1. Pornirea serverului

```bash
javac Host.java
java Host
```

Se va cere numărul de jucători. Apoi, aplicația va aștepta conexiuni.

### 2. Conectarea jucătorilor

#### Opțiunea 1 – folosind `Client.java` (IP implicit 127.0.0.1)

```bash
javac Client.java
java Client
```

#### Opțiunea 2 – folosind `ClientManual.java` (pentru IP diferit)

```bash
javac ClientManual.java
java ClientManual
```

Introdu IP-ul serverului atunci când este cerut.

### 3. Desfășurarea jocului

- Fiecare jucător ghicește litere pe rând.
- Se acordă puncte pentru literele corect ghicite.
- Jocul continuă până când cuvântul este complet sau spânzurătoarea este complet desenată.
- La final, se afișează scorurile și se oferă opțiunea de a continua jocul.

## Exemplu de scoruri (`scores.txt`)

```
Alex (127.0.0.1): 2
Ionut (172.20.10.4): 1
```

## Echipa proiectului

- Achitei Alexandru-Ionut
- Andronache Ionut]

