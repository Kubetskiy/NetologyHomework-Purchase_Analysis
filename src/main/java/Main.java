import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        var serverSocketRunner = new SocketServerRunner();
//        var dataHandler = DataHandler.getInstance();
//        DataHandler.getInstance();
        serverSocketRunner.run();
    }
}
