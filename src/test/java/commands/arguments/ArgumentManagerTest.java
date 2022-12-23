package commands.arguments;

import aybici.parkourplugin.commands.arguments.*;
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

    @Test
    public void parseNumberArguments(){
        IntArgument intArgument = new IntArgument("-intArg=", 5);
        DoubleArgument doubleArgument = new DoubleArgument("-doubleArg=", 1.1);
        DominantIntArgument dominantIntArgument = new DominantIntArgument(true);
        ArgumentManager argumentManager = new ArgumentManager();
        argumentManager.addArgument(intArgument);
        argumentManager.addArgument(doubleArgument);
        argumentManager.addArgument(dominantIntArgument);
        String[] args = {"-intArg=10","12","-doubleArg=2.1"};
        boolean isArgsOk = argumentManager.parseAllArgs(args);
        Assertions.assertEquals(true, intArgument.isSpecified());
        Assertions.assertEquals(10, intArgument.getValue());
        Assertions.assertEquals(true, doubleArgument.isSpecified());
        Assertions.assertEquals(2.1, doubleArgument.getValue());
        Assertions.assertEquals(true, dominantIntArgument.isSpecified());
        Assertions.assertEquals(12, dominantIntArgument.getValue());
        Assertions.assertEquals(true, isArgsOk);

        intArgument = new IntArgument("-intArg=", 5);
        doubleArgument = new DoubleArgument("-doubleArg=", 1.1);
        dominantIntArgument = new DominantIntArgument(true);
        argumentManager = new ArgumentManager();
        argumentManager.addArgument(intArgument);
        argumentManager.addArgument(doubleArgument);
        argumentManager.addArgument(dominantIntArgument);
        String[] args2 = {"-intArg=10","-doubleArg=2.2","23", "23"};
        isArgsOk = argumentManager.parseAllArgs(args2);
        Assertions.assertEquals(true, intArgument.isSpecified());
        Assertions.assertEquals(10, intArgument.getValue());
        Assertions.assertEquals(true, doubleArgument.isSpecified());
        Assertions.assertEquals(2.2, doubleArgument.getValue());
        Assertions.assertEquals(true, dominantIntArgument.isSpecified());
        Assertions.assertEquals(23, dominantIntArgument.getValue());
        Assertions.assertEquals(false, isArgsOk);
    }
}
