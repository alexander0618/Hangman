import java.io.*;
import java.net.*;
import java.util.*;

public class ClientManual {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter server IP: ");
        String serverIP = scanner.nextLine();
        final int PORT = 12345;

        Socket socket = new Socket(serverIP, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

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
