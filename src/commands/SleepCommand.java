package commands;

import java.util.List;

public class SleepCommand implements Command{
    @Override
    public Integer doCommand(List<String> command) {
        try {
            Thread.sleep(Integer.parseInt(command.get(0)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}