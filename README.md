# 🕹️ Network Hangman Game

[![Java](https://img.shields.io/badge/language-Java-red.svg)](https://www.java.com/)
[![Platform](https://img.shields.io/badge/platform-console-blue.svg)]()
[![Status](https://img.shields.io/badge/status-working-green.svg)]()

> A multiplayer network implementation of the classic Hangman game using Java.

---

## 🔍 Overview

This project allows players to connect to a server and play a game of **Hangman**, featuring a visual representation of the hangman, individual scoring, and automatic player turn rotation.

---

## 🧩 Features

- ✅ Multiplayer over network (client-server)
- 📜 Random word selection from `words.txt`
- 🧠 Scoring system based on correct letter guesses
- 🗃️ Automatic saving of scores (`scores.txt`) and player info (`players.txt`)
- 🔁 Prompt to continue the game after each round

---

## 🗂️ File Structure

| File              | Description                                                           |
|-------------------|------------------------------------------------------------------------|
| `Host.java`       | Game server. Waits for players and controls game logic.               |
| `Client.java`     | Client connecting to 127.0.0.1 (localhost)                            |
| `ClientManual.java` | Client with manual IP input                                          |
| `words.txt`       | List of possible words for the game                                   |
| `players.txt`     | Auto-generated file with player names and IPs                         |
| `scores.txt`      | Auto-generated file with player scores                                |

---

## ⚙️ Requirements

- Java 8 or higher
- Console or IDE supporting Java
- Local or LAN network connection

---

## ▶️ How to Run

### 1. Compile the source files

```bash
javac Host.java Client.java ClientManual.java
```

### 2. Start the server

```bash
java Host
```

You will be prompted to enter the number of players.

### 3. Connect clients

- Use `Client.java` to connect to localhost
- Use `ClientManual.java` to connect to a different IP

---

## 💾 Sample Scores (`scores.txt`)

```
Alex (127.0.0.1): 2
Ionut (172.20.10.4): 1
```

---

## 📋 TODO

- [ ] Add GUI
- [ ] Add difficulty levels (easy, medium, hard)
- [ ] Support session persistence
- [ ] Improve client-server communication with threads

---

## 👥 Project Team

- Achitei Alexandru-Ionut  
- Andronache Ionut

---

## 📚 Useful Resources

- [Java Documentation](https://docs.oracle.com/en/java/)
- [Java Sockets Guide](https://www.baeldung.com/a-guide-to-java-sockets)
