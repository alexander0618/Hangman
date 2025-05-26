import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        String prompt = in.readLine();
        System.out.print(prompt + " ");
        String username = scanner.nextLine();
        out.println(username);

        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);

            if (line.equals("Your guess:")) {
                System.out.print("> ");
                String input = scanner.nextLine();
                out.println(input);
            }

            if (line.toLowerCase().contains("do you want to continue")) {
                System.out.print("Continue? (yes/no): ");
                String response = scanner.nextLine();
                out.println(response);
            }

            if (line.startsWith("You left the game.")) {
                System.out.println("[INFO] Game ended. Press Enter to exit.");
                scanner.nextLine();
                break;
            }
        }

        socket.close();
    }
}
