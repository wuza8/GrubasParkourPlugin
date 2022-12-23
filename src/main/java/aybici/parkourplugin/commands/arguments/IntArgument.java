package aybici.parkourplugin.commands.arguments;

import static aybici.parkourplugin.utils.NumberParser.tryParseInt;

public class IntArgument extends Argument{
    private String textPrefixNeededToSetValue;
    private int value;
    @Override
    public void parseArg(String text) {
        if(specified)
            return;
        if(text.startsWith(textPrefixNeededToSetValue)){
            Integer number = tryParseInt(text.substring(textPrefixNeededToSetValue.length()));
            if (number != null) {
                value = number;
                specified = true;
            }
        }
    }
    public IntArgument(String textPrefixNeededToSetValue, int defaultValue){
        this.textPrefixNeededToSetValue = textPrefixNeededToSetValue;
        this.value = defaultValue;
        this.specified = false;
    }
    public IntArgument(String textPrefixNeededToSetValue){
        this(textPrefixNeededToSetValue, 0);
    }
    public int getValue(){
        return value;
    }
}
