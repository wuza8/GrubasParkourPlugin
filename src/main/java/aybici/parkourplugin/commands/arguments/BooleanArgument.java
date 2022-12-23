package aybici.parkourplugin.commands.arguments;


public class BooleanArgument extends Argument{
    private String textNeededToChangeValue;
    private boolean defaultValue;
    public boolean getValue(){
        return ((BooleanValue) this.value).booleanValue;
    }
    @Override
    public void parseArg(String text) {
        if(specified)
            return;
        if (text.equals(textNeededToChangeValue)){
            this.value = new BooleanValue(!defaultValue);
            this.specified = true;
        }
    }
    public BooleanArgument(String textNeededToChangeValue, boolean defaultValue){
        this.defaultValue = defaultValue;
        this.value = new BooleanValue(defaultValue);
        this.textNeededToChangeValue = textNeededToChangeValue;
        this.specified = false;
    }
}
