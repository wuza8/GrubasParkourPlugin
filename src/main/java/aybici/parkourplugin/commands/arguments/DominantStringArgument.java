package aybici.parkourplugin.commands.arguments;

public class DominantStringArgument extends StringArgument{

    public DominantStringArgument(boolean isObligatory){
        super(null,isObligatory);
    }
    @Override
    public void parseArg(String text){
        if(specified)
            return;
        value = text;
        specified = true;
    }
}
