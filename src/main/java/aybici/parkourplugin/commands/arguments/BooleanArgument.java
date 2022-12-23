package aybici.parkourplugin.commands.arguments;


public class BooleanArgument extends Argument{
    private String textNeededToChangeValue;
    private boolean defaultValue;
    private boolean value;
    public boolean getValue(){
        return value;
    }
    @Override
    public void parseArg(String text) {
        if(specified)
            return;
        if (text.equals(textNeededToChangeValue)){
            this.value = !defaultValue;
            this.specified = true;
        }
    }
    public BooleanArgument(String textNeededToChangeValue, boolean defaultValue){
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.textNeededToChangeValue = textNeededToChangeValue;
        this.specified = false;
    }
}
