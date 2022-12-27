package aybici.parkourplugin.commands.arguments;

import static aybici.parkourplugin.utils.NumberParser.tryParseInt;

public class DominantIntArgument extends IntArgument{
    @Override
    public void parseArg(String text) {
        if(specified)
            return;
        Integer number = tryParseInt(text);
        if (number != null) {
            value = number;
            specified = true;
        }
    }
    public DominantIntArgument(int defaultValue){
        super(null, defaultValue);
    }
    public DominantIntArgument(boolean isObligatory){
        super(null, isObligatory);
    }
}
