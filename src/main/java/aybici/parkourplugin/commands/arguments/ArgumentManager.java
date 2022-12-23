package aybici.parkourplugin.commands.arguments;

import java.util.ArrayList;
import java.util.List;

public class ArgumentManager {
    private List<Argument> arguments;
    public ArgumentManager(List<Argument> arguments){
        this.arguments = arguments;
    }
    public ArgumentManager(){
        arguments = new ArrayList<>();
    }
    public void parseAllArgs(String[] stringArgs){
        for (String arg : stringArgs) {
            for (Argument argument : arguments) {
                argument.parseArg(arg);
            }
        }
    }
    public void addArgument(Argument argument){
        arguments.add(argument);
    }
}
