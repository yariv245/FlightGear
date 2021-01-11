package commands;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ConnectCommand implements Command {
    public static Socket client;

    @Override
    public Integer doCommand(List<String> command) {
        System.out.println("\nConnect server command start for IP: "+command.get(0)+" Port: "+command.get(1));
        new Thread(() -> connectServer(command.get(0), Integer.parseInt(command.get(1)))).start();
        return 2;
    }

    private void connectServer(String ip, int port) {
        boolean stop = false;
        while (!stop) {
            try {
                client = new Socket(ip, port);
				System.out.println("Connected to FlightSimulator server PORT: "+client.getPort());
                stop = true;
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
