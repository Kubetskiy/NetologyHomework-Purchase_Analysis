import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class SocketServerRunner implements Runnable {
    @Override
    public void run() {
        try (var serverSocket = new ServerSocket(8989);
             var socket = serverSocket.accept();
             var outputStream = new DataOutputStream(socket.getOutputStream());
             var inputStream = new DataInputStream(socket.getInputStream())) {
            String response;
            while (true) {
                // Принимаем запрос
                var request = inputStream.readUTF(); // {"title": "булка", "date": "2022.02.08", "sum": 200}
                // Отправляем на обработку
                DataHandler.getInstance().addSale(request);
                // Запрашиваем результат и отправляем его клиенту
                response = DataHandler.getInstance().generateAnalysisResults();
                outputStream.writeUTF(response);
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
