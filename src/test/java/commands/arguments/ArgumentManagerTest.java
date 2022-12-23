package commands.arguments;

import aybici.parkourplugin.commands.arguments.Argument;
import aybici.parkourplugin.commands.arguments.ArgumentManager;
import aybici.parkourplugin.commands.arguments.BooleanArgument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentManagerTest {
    @Test
    public void parseArgsTest(){
        List<Argument> argumentList = new ArrayList<>();
        BooleanArgument arg1 = new BooleanArgument("-setArg1", false);
        BooleanArgument arg2 = new BooleanArgument("-setArg2", true);

        argumentList.add(arg1);
        argumentList.add(arg2);
        ArgumentManager argumentManager = new ArgumentManager(argumentList);
        String[] args = {"asd","asf","dfg","ghjghj"};
        argumentManager.parseAllArgs(args);
        Assertions.assertEquals(false, arg1.isSpecified());
        Assertions.assertEquals(false, arg2.isSpecified());
        Assertions.assertEquals(false, arg1.getValue());
        Assertions.assertEquals(true, arg2.getValue());

        List<Argument> argumentList2 = new ArrayList<>();
        BooleanArgument arg3 = new BooleanArgument("-setArg3", false);
        BooleanArgument arg4 = new BooleanArgument("-setArg4", true);

        argumentList2.add(arg3);
        argumentList2.add(arg4);

        ArgumentManager argumentManager2 = new ArgumentManager(argumentList2);
        String[] args2 = {"-setArg3","asf","-setArg4"};
        argumentManager2.parseAllArgs(args2);
        Assertions.assertEquals(true, arg3.isSpecified());
        Assertions.assertEquals(true, arg4.isSpecified());
        Assertions.assertEquals(true, arg3.getValue());
        Assertions.assertEquals(false, arg4.getValue());

    }
    @Test
    public void addArgTest(){
        BooleanArgument arg1 = new BooleanArgument("-setArg1", false);
        ArgumentManager argumentManager = new ArgumentManager();
        argumentManager.addArgument(arg1);
        String[] args = {"asd","-setArg1"};
        argumentManager.parseAllArgs(args);
        Assertions.assertEquals(true, arg1.isSpecified());
        Assertions.assertEquals(true, arg1.getValue());
    }
}
