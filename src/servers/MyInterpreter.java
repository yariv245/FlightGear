package servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

import commands.*;

public class MyInterpreter {
    private static volatile boolean stop;
    private static PrintWriter outClientFlightGear;
    private static PrintWriter outClientGui;
    private static boolean isRun = false;
    static final Map<String, Command> commandsMap = createCommandMap();

    private static Map<String, Command> createCommandMap() {
        Map<String, Command> result = new HashMap<String, Command>();
        result.put("return", new ReturnCommand());
        result.put("var", new DefineVarCommand());
        result.put("update", new UpdateCommand());
        result.put("openDataServer", new OpenServerCommand());
        result.put("connect", new ConnectCommand());
        result.put("disconnect", new DisconnectCommand());

        return Collections.unmodifiableMap(result);
    }

    static HashMap<String, Var> symbolTable = createSymbolTable();

    private static HashMap<String, Var> createSymbolTable() {
        HashMap<String, Var> map = new HashMap<String, Var>();
        map.put("simAileron", new Var(0, "/controls/flight/aileron"));
        map.put("simElevator", new Var(0, "/controls/flight/elevator"));
        map.put("simRudder", new Var(0, "/controls/flight/rudder"));
        map.put("simThrottle", new Var(0, "/controls/engines/current-engine/throttle"));
        map.put("simAirplaneX", new Var(0, "client_airplaneX"));
        map.put("simAirplaneY", new Var(0, "client_airplaneY"));
        map.put("simHeading", new Var(0, "client_heading"));

        return map;
    }

    public static int interpret(String[] lines) {
        int reuslt = 0;
        ArrayList<String> loopLines = new ArrayList<String>();
        Boolean insertLine = false;
        for (String line : lines) {//TODO:consider to remove this for and change the argument to String line
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
            if (line.contains("while")) {
                insertLine = true;
            } else if (line.contains("}")) {
                insertLine = false;
                new ConditionParser().doCommand(loopLines);
            }

            if (insertLine)
                loopLines.add(line);
            else {
                reuslt = parser(lexer(line));
            }
        }
        return reuslt;
    }

    public static ArrayList<String> lexer(String line) {
        ArrayList<String> words = new ArrayList<String>();
        for (String word : line.split(" "))
            words.add(word);
        return words;
    }

    public static int parser(ArrayList<String> command) {
        int result = 0;
        Command c = commandsMap.get(command.get(0));
        if (c != null) {
            result = c.doCommand(command.subList(1, command.size()));
        } else if (MyInterpreter.symbolTable.containsKey(command.get(0).split("=")[0])) {
            c = commandsMap.get("update");
            result = c.doCommand(command);
        }
        return result;
    }

    public static void putSymbolTable(String key, double value) {
        if (symbolTable.containsKey(key))
            symbolTable.get(key).setValue(value);
        else
            symbolTable.put(key, new Var(value));
    }

    public static void putSymbolTable(String key) {
        symbolTable.put(key, new Var(0));
    }

    public static Var getSymbolTable(String key) {
        return symbolTable.get(key);
    }

    public static void startClient(String ip, int port) {
        new Thread(() -> runClientFlightGear(ip, port)).start(); //Send data to Flightgear port:5401
        new Thread(() -> runClientGui(ip, port + 3)).start(); //Send data to Gui port:5404
    }

    public static void startServer(int port) {
        new Thread(() -> runServerFlightGear(port)).start(); // receive data from Flightgear port:5400
        new Thread(() -> runServerGui(port + 3)).start(); // receive data from GUI port:5403
    }

    private static void runClientFlightGear(String ip, int port) {
        while (!stop) {
            try {
                Socket interpreter = new Socket(ip, port);
                outClientFlightGear = new PrintWriter(interpreter.getOutputStream());
                while (!stop) {

                }
                outClientFlightGear.close();
                interpreter.close();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    private static void runClientGui(String ip, int port) {
        while (!stop) {
            try {
                Socket interpreter = new Socket(ip, port);
                outClientGui = new PrintWriter(interpreter.getOutputStream());
                while (!stop) {
                }
                outClientFlightGear.close();
                interpreter.close();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    private static void runServerGui(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            String[] lines = new String[1];
            System.out.print("Interpreter server Gui: Waiting for clients\n");
            while (!stop) {
                try {
                    System.out.print(".");
                    Socket socket = server.accept();
                    System.out.println("Interpreter server Gui: Client Connected");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;

                    while (!(line = in.readLine()).equals("bye")) {
                        lines[0] = line;
                        MyInterpreter.interpret(lines); // interpret msg from GUI
                        System.out.println("Gui sent message");
                    }

                    in.close();
                    socket.close();
                    System.out.println("MyInterpreter server: Client DisConnected");
                } catch (SocketTimeoutException e) {
                }
            }
            server.close();
        } catch (IOException e) {
        }
    }

    private static void runServerFlightGear(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            String[] initial = {"var airplaneX = bind simAirplaneX","var airplaneY = bind simAirplaneY"};
            System.out.print("Interpreter server flightgear: Waiting for clients\n");
            MyInterpreter.interpret(initial);
            while (!stop) {
                try {
                    System.out.print(".");
                    Socket socket = server.accept();
                    System.out.println("Interpreter server flightgear: Client Connected");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    while (!(line = in.readLine()).equals("bye")) {
                        String[] data = line.split(",");
                        String[] dataToInterpret = {"airplaneY = " + data[data.length - 2],"airplaneX = " + data[data.length - 1]};
                        MyInterpreter.interpret(dataToInterpret);
//                        System.out.println("Server Sent message: " + data[data.length - 2]+"," + data[data.length - 1]);
                    }
                    in.close();
                    socket.close();
                    System.out.println("MyInterpreter server: Client DisConnected");
                } catch (SocketTimeoutException e) {
                }
            }
            server.close();
        } catch (IOException e) {
        }
    }

    public void close() {
        stop = true;
    }

    public static void sendToClientFlightGear(String line) {
        if (outClientFlightGear == null)
            return;
        outClientFlightGear.println(line);
        outClientFlightGear.flush();
    }

    public static void sendToClientGui(String line) {
        if (outClientGui == null)
            return;
        outClientGui.println(line);
        System.out.println(line);
        outClientGui.flush();
    }

}
