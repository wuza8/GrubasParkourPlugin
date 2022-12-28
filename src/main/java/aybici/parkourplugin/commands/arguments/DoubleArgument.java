package aybici.parkourplugin.commands.arguments;

import static aybici.parkourplugin.utils.ValueParser.tryParseDouble;

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
        this.isObligatory = false;
    }
    public DoubleArgument(String textPrefixNeededToSetValue, boolean isObligatory){
        this.textPrefixNeededToSetValue = textPrefixNeededToSetValue;
        this.value = 0;
        this.specified = false;
        this.isObligatory = isObligatory;
    }
    public double getValue(){
        return value;
    }
}
