package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import commands.Command;
import commands.ConditionParser;
import commands.ConnectCommand;
import commands.DefineVarCommand;
import commands.DisconnectCommand;
import commands.OpenServerCommand;
import commands.ReturnCommand;
import commands.UpdateCommand;

public class MyInterpreter {
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
        map.put("simX", new Var(0, "simX"));
        map.put("simY", new Var(0, "simY"));
        map.put("simZ", new Var(0, "simZ"));
        return map;
    }

    public static int interpret(String[] lines) {
        symbolTable = createSymbolTable();
        int reuslt = 0;
        ArrayList<String> loopLines = new ArrayList<String>();
        Boolean insertLine = false;
        for (String line : lines) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (line.contains("while")) {
                insertLine = true;
            } else if (line.contains("}")) {
                insertLine = false;
                new ConditionParser().doCommand(loopLines);
            }
            if (insertLine)
                loopLines.add(line);
            else
                reuslt = parser(lexer(line));
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
}
