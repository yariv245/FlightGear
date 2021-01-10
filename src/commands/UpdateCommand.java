package commands;

import java.util.List;

import expressions.ShuntingYard;
import test.MyInterpreter;

public class UpdateCommand implements Command {

    @Override
    public Integer doCommand(List<String> command) {
        double result = 0;
        if (command.contains("bind")) { // "x = bind Simx"
            MyInterpreter.putSymbolTable(command.get(0));
            new BindCommand().doCommand(command);
        } else {
            String stringExp = String.join("", command); // turn the Arraylist to string for the Shunting Yard
            String[] arrayExp = stringExp.split("=");
            result = ShuntingYard.calc(arrayExp[1]); // Calculate the expression
            MyInterpreter.putSymbolTable(arrayExp[0], result);
        }
        return (int) result;
    }

}
