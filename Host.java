import java.io.*;
import java.net.*;
import java.util.*;

public class Host {
    private static final int PORT = 12345;
    private static List<String> words;
    private static List<Player> players = new ArrayList<>();
    private static Map<String, Integer> scores = new HashMap<>();
    private static Map<String, Integer> correctGuesses = new HashMap<>();
    private static Map<String, Integer> wrongGuessesCount = new HashMap<>();
    private static String currentWord;
    private static char[] display;
    private static int wrongGuesses = 0;
    private static final String[] HANGMAN = {
        "",
        "\n\n\n\n\n\n____",
        "\n |\n |\n |\n |\n_|___",
        "____\n |\n |\n |\n |\n_|___",
        "____\n |  O\n |\n |\n |\n_|___",
        "____\n |  O\n |  |\n |\n |\n_|___",
        "____\n |  O\n | /|\\\n |\n |\n_|___",
        "____\n |  O\n | /|\\\n | / \\\n |\n_|___"
    };

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        loadWords();
        clearFile("players.txt");
        clearFile("scores.txt");

        System.out.print("[HOST] Enter number of players: ");
        int numPlayers = Integer.parseInt(scanner.nextLine());
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("[HOST] Waiting for players...");

        while (players.size() < numPlayers) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter your username:");
            String username = in.readLine();
            Player player = new Player(username, socket, in, out);
            players.add(player);
            scores.put(username, 0);
            correctGuesses.put(username, 0);
            wrongGuessesCount.put(username, 0);
            savePlayer(username, socket.getInetAddress().getHostAddress());

            System.out.println("[HOST] Player connected: " + username + " (" + socket.getInetAddress().getHostAddress() + ")");
        }

        System.out.println("[HOST] A new game has started.");
        startGame();
        serverSocket.close();
    }

    private static void loadWords() throws IOException {
        words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        }
    }

    private static void clearFile(String filename) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.print("");
        }
    }

    private static void saveScore(String username, int score) throws IOException {
        try (FileWriter fw = new FileWriter("scores.txt", true)) {
            fw.write(username + ": " + score + "\n");
        }
    }

    private static void savePlayer(String username, String ip) throws IOException {
        try (FileWriter fw = new FileWriter("players.txt", true)) {
            fw.write(username + "," + ip + "\n");
        }
    }

    private static void startGame() throws IOException {
        currentWord = words.get(new Random().nextInt(words.size()));
        System.out.println("[HOST] A new game has started");
        System.out.println("[HOST] Word to guess: " + currentWord);
        display = new char[currentWord.length()];
        Arrays.fill(display, '_');

        int currentPlayerIndex = 0;
        Set<Character> guessed = new HashSet<>();

        while (wrongGuesses < HANGMAN.length && new String(display).contains("_")) {
            Player current = players.get(currentPlayerIndex);
            broadcast(" " + HANGMAN[wrongGuesses]);
            broadcast("Word: " + new String(display));
            broadcast("It is " + current.username + "'s turn (" + current.getIP() + ")");

            current.out.println("Your guess:");
            String input = current.in.readLine();
            if (input == null || input.length() == 0) continue;
            char guess = input.toLowerCase().charAt(0);

            if (guessed.contains(guess)) {
                current.out.println("Letter already guessed. Skipping turn.");
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                continue;
            }

            guessed.add(guess);
            boolean correct = false;
            int points = 0;
            for (int i = 0; i < currentWord.length(); i++) {
                if (currentWord.charAt(i) == guess && display[i] == '_') {
                    display[i] = guess;
                    correct = true;
                    points++;
                }
            }
            if (correct) {
                scores.put(current.username, scores.get(current.username) + points);
                correctGuesses.put(current.username, correctGuesses.get(current.username) + points);
                broadcast(current.username + " guessed correctly: '" + guess + "'");
            } else {
                wrongGuesses++;
                wrongGuessesCount.put(current.username, wrongGuessesCount.get(current.username) + 1);
                broadcast(current.username + " guessed wrong: '" + guess + "'");
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }

        int index = Math.min(wrongGuesses, HANGMAN.length - 1);
        broadcast(HANGMAN[index]);
        System.out.println("[HOST] Final word was: " + currentWord);
        broadcast("Final word: " + currentWord);
        broadcast("Final scores:");
        clearFile("scores.txt");
        for (Player p : players) {
            int score = scores.get(p.username);
            broadcast(p.username + ": " + score + " points");
            String fullStats = p.username + ": " + score + " points, " + correctGuesses.get(p.username) + " correct, " + wrongGuessesCount.get(p.username) + " wrong guesses";
            System.out.println("[HOST] " + fullStats);
            saveScore(p.username + " (" + p.getIP() + ")", score);
            System.out.println("[HOST] " + summary);
            saveScore(p.username + " (" + p.getIP() + ")", score);
        }

        askForContinuation();
    }

    private static void askForContinuation() throws IOException {
        Map<Player, String> responses = new HashMap<>();
        for (Player p : players) {
            p.out.println("Do you want to continue?");
        }

        for (Player p : players) {
            try {
                String response = p.in.readLine();
                responses.put(p, response);
            } catch (IOException e) {
                responses.put(p, "no");
            }
        }

        List<Player> remainingPlayers = new ArrayList<>();
        for (Map.Entry<Player, String> entry : responses.entrySet()) {
            Player p = entry.getKey();
            String response = entry.getValue();

            if (response != null && response.trim().equalsIgnoreCase("yes")) {
                remainingPlayers.add(p);
            } else {
                p.out.println("You left the game.");
                p.socket.close();
            }
        }

        players = remainingPlayers;
        if (players.isEmpty()) {
            System.out.println("[HOST] All players have left. Shutting down.");
        } else {
            wrongGuesses = 0;
            startGame();
        }
    }

    private static void broadcast(String message) {
        for (Player p : players) {
            p.out.println(message);
        }
    }

    static class Player {
        String username;
        Socket socket;
        BufferedReader in;
        PrintWriter out;

        Player(String username, Socket socket, BufferedReader in, PrintWriter out) {
            this.username = username;
            this.socket = socket;
            this.in = in;
            this.out = out;
        }

        String getIP() {
            return socket.getInetAddress().getHostAddress();
        }
    }
}
