package aybici.parkourplugin.commands.arguments;

public class StringArgument extends Argument{
    private String textPrefixNeededToSetValue;
    protected String value;
    @Override
    public void parseArg(String text) {
        if(specified)
            return;
        if(text.startsWith(textPrefixNeededToSetValue)){
            String textValue = text.substring(textPrefixNeededToSetValue.length());
            value = textValue;
            specified = true;
        }
    }
    public StringArgument(String textPrefixNeededToSetValue, boolean isObligatory){
        this.textPrefixNeededToSetValue = textPrefixNeededToSetValue;
        this.isObligatory = isObligatory;
        this.specified = false;
    }
    public String getValue(){
        return value;
    }
}
