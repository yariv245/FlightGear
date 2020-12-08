package commands;

import java.util.List;

import expressions.ShuntingYard;
import test.MyInterpreter;

public class DefineVarCommand implements Command {

	@Override
	public Integer doCommand(List<String> command) {
		String name;
		if (command.contains("bind")) { // If expression contain "bind" => " x = bind y"
			name = command.get(0); // Get the name of variable
			double value = MyInterpreter.getSymbolTable(command.get(3)).getValue(); // Get the value from symbol table
			MyInterpreter.putSymbolTable(name, value);
			new BindCommand().doCommand(command);
		} else {// If expression doesn't contain "bind" => "x=5+6" || x"
			String expression = String.join("", command); // turn the Arraylist to string for the Shunting Yard
			if (expression.contains("=")) { // If expression contains "=" => "x=5+6"
				double result = 0;
				String[] expArray = expression.split("="); // Split the command to ["x","5+6"]
				name = expArray[0];
				result = ShuntingYard.calc(expArray[1]);
				MyInterpreter.putSymbolTable(name, result);
			} else { // If expression doesn't contains "=" => "x"
				name = command.get(0);
				MyInterpreter.putSymbolTable(name);
			}
		}
		return 1;

	}

}
