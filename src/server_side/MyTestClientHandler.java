
package server_side;

import java.io.InputStream;
import java.io.OutputStream;

public class MyTestClientHandler implements ClientHandler {
    Solver solver;
    CacheManager cm;

    public MyTestClientHandler() {
    }

    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
    }
}
