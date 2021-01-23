package commands;

import java.util.List;

public class PrintCommand implements Command{
    @Override
    public Integer doCommand(List<String> command) {
        System.out.println("Print Command:" + command.toString());
        return 0;
    }
}