import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Scanner;


public class SocketRunner {
    public static void main(String[] args) throws IOException {

        var inetAddress = Inet4Address.getByName("localhost");

//        while (true) {
            System.out.println(" ");
            try (var socket = new Socket(inetAddress, 8989);
                 var outputStream = new DataOutputStream(socket.getOutputStream());
                 var inputStream = new DataInputStream(socket.getInputStream());
                 var scanner = new Scanner(System.in)) {
                var request = scanner.nextLine();
                outputStream.writeUTF(request);
                System.out.println("From server: " + inputStream.readUTF());
            }
//        }
    }
}
