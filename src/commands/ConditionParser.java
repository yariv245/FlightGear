package commands;

import java.util.List;

import test.MyInterpreter;

public class ConditionParser implements Command {

    @Override
    public Integer doCommand(List<String> command) {
        String[] condition = command.get(0).split(" "); // "while x < 5 {"
        command.remove(0);
        String predicate = condition[2]; // <
        int result = 0;
        double left = MyInterpreter.getSymbolTable(condition[1]).getValue(); // x
        double right = Double.parseDouble(condition[3]); // 5
//		while (stop) {
        switch (predicate) {
            case "<":
                while (left < right) {
                    for (String line : command) {
                        String newLine = line.replace("\t", "");
                        result = MyInterpreter.parser(MyInterpreter.lexer(newLine));
                        left = MyInterpreter.getSymbolTable(condition[1]).getValue(); // update
                    }
                }
                break;
//		case "<=":
//			break;
//		case ">":
//			break;
//		case ">=":
//			break;
//		case "==":
//			break;
//		case "!=":
//			break;
        }
//		}
        return result;
    }
}
