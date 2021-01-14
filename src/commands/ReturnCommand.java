package commands;

import java.util.List;

import expressions.ShuntingYard;
import servers.MyInterpreter;

public class ReturnCommand implements Command {

    public ReturnCommand() {
    }

    @Override
    public Integer doCommand(List<String> command) {
        StringBuilder sb = new StringBuilder();
        for (String s : command) // turn the array list to string for the ShuntingYard
        {
            sb.append(s);
        }
        if (MyInterpreter.getSymbolTable(command.get(0)) == null) // If on the SymbolTable contain "y"/"x"
            return (int) (ShuntingYard.calc(sb.toString())); // Calculate the expression
        else
            return (int) MyInterpreter.getSymbolTable(command.get(0)).getValue(); // Return the element from
        // SymbolTable
    }

}
