package commands;

import java.util.List;

import test.MyInterpreter;

public class BindCommand implements Command {

    @Override
    public Integer doCommand(List<String> command) { // "x = bind simX"
        MyInterpreter.getSymbolTable(command.get(0))
                .addObserver(MyInterpreter.getSymbolTable(command.get(3)));
        MyInterpreter.getSymbolTable(command.get(3))
                .addObserver(MyInterpreter.getSymbolTable(command.get(0)));
        System.out.println(command.get(0) + " bind to: " + command.get(3));

        return 3;
    }

}
