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
    private static PrintWriter outClient = null;
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

    // TODO : Not sure if VOID
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
        new Thread(() -> runClient(ip, port)).start();
    }

    public static void startServer(int port) {
        new Thread(() -> runServer(port)).start();
    }

    private static void runClient(String ip, int port) {
        while (!stop) {
            try {
                Socket interpreter = new Socket(ip, port);
                outClient = new PrintWriter(interpreter.getOutputStream());
                while (!stop) {
//                    out.println(simX + "," + simY + "," + simZ);
//                    outClient.flush();
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e1) {
//                    }
                }
                outClient.close();
                interpreter.close();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            }
        }
    }

    private static void runServer(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);
            Queue<String> Qlines = new ArrayDeque<>();
            String[] lines = new String[1];
            System.out.print("MyInterpreter server: Waiting for clients\n");
            while (!stop) {
                try {
                    System.out.print(".");
                    Socket client = server.accept();
                    System.out.println("MyInterpreter server: Client Connected");
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String line = null;
                    while (!(line = in.readLine()).equals("bye")) {
                        try {
//                            System.out.println("Interpreter server: " + line);
                            lines[0] = line;
                            MyInterpreter.interpret(lines);

//                            if (line.startsWith("set simX"))
//                                simX = Double.parseDouble(line.split(" ")[2]);
                        } catch (NumberFormatException e) {
                        }
                    }
                    in.close();
                    client.close();
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

    public static void sentToServer(String[] lines) {
        if (outClient == null)
            return;
        System.out.println("lines got");

        for (String line : lines) {
            outClient.println(line);
            outClient.flush();
        }
    }

    public static void sentToServer(String line) {
        if (outClient == null)
            return;
        outClient.println(line);
        outClient.flush();
    }

}
