package aybici.parkourplugin.commands.arguments;

import aybici.parkourplugin.commands.arguments.ArgValue;

public abstract class Argument {
    protected boolean specified;
    protected ArgValue value;
    public abstract void parseArg(String text);
    public boolean isSpecified(){
        return specified;
    }
}
