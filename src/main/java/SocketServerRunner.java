import java.io.*;
import java.net.ServerSocket;

public class SocketServerRunner implements Runnable {
    @Override
    public void run() {
        try (var serverSocket = new ServerSocket(8989)) {
            while (true) {
                try (
                        var socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                    String response;
                    // Принимаем запрос
                    var request = in.readLine(); // {"title": "булка", "date": "2022.02.08", "sum": 200}
                    // Отправляем на обработку
                    DataHandler.getInstance().addSale(request);
                    // Запрашиваем результат и отправляем его клиенту
                    response = DataHandler.getInstance().generateAnalysisResults();
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
