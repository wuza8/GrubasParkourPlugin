package aybici.parkourplugin.commands.arguments;

public abstract class Argument {
    protected boolean isObligatory;
    protected boolean specified;
    public abstract void parseArg(String text);
    public boolean isSpecified(){
        return specified;
    }
}
