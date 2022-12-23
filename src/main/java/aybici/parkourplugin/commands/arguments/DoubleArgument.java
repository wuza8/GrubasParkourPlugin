package aybici.parkourplugin.commands.arguments;

import static aybici.parkourplugin.utils.NumberParser.tryParseDouble;

public class DoubleArgument extends Argument{
    private String textPrefixNeededToSetValue;
    private double value;
    @Override
    public void parseArg(String text) {
        if(specified)
            return;
        if(text.startsWith(textPrefixNeededToSetValue)){
            Double number = tryParseDouble(text.substring(textPrefixNeededToSetValue.length()));
            if (number != null) {
                value = number;
                specified = true;
            }
        }
    }
    public DoubleArgument(String textPrefixNeededToSetValue, double defaultValue){
        this.textPrefixNeededToSetValue = textPrefixNeededToSetValue;
        this.value = defaultValue;
        this.specified = false;
    }
    public DoubleArgument(String textPrefixNeededToSetValue){
        this(textPrefixNeededToSetValue, 0);
    }
    public double getValue(){
        return value;
    }
}
