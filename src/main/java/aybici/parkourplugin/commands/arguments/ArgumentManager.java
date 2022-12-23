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
    public boolean parseAllArgs(String[] stringArgs){
        int specifiedArgs = 0;
        for (String arg : stringArgs) {
            for (Argument argument : arguments) {
                if(!argument.isSpecified()) {
                    argument.parseArg(arg);
                    if(argument.isSpecified())
                        specifiedArgs ++;
                }
            }
        }
        for(Argument argument : arguments)
            if(argument.isObligatory && !argument.isSpecified())
                return false;
        return (specifiedArgs == stringArgs.length);
    }
    public void addArgument(Argument argument){
        arguments.add(argument);
    }
}
