package commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DisconnectCommand implements Command {

	@Override
	public Integer doCommand(List<String> command) {
		PrintWriter out = null;
		
		if (!ConnectCommand.client.isClosed()) {
			try {
				out = new PrintWriter(ConnectCommand.client.getOutputStream());
				out.println("bye");
				out.flush();
				ConnectCommand.client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		out.close();
		}
		if (!OpenServerCommand.server.isClosed())
			try {
				OpenServerCommand.server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return 1;
	}

}