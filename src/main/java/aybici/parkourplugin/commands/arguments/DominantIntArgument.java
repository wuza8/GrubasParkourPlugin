package aybici.parkourplugin.commands.arguments;

import static aybici.parkourplugin.utils.NumberParser.tryParseInt;

public class DominantIntArgument extends Argument{
    private int value;
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
        this.value = defaultValue;
        this.specified = false;
    }
    public DominantIntArgument(){
        this(0);
    }
    public int getValue(){
        return value;
    }
}
