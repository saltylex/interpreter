package View;

import Controller.Controller;
import Model.DataStructures.ADTStack;
import Model.Exceptions.EvaluationException;
import Model.Expressions.ArithmeticExpression;
import Model.Expressions.HeapReadExpression;
import Model.Expressions.ValueExpression;
import Model.Expressions.VariableExpression;
import Model.Statements.*;
import Model.Types.BoolType;
import Model.Types.IntType;
import Model.Types.RefType;
import Model.Types.StringType;
import Model.Values.BoolValue;
import Model.Values.IntValue;
import Model.Values.RefValue;
import Model.Values.StringValue;
import View.TextMenu;
import View.Commands.ExitCommand;
import View.Commands.RunCommand;

import java.io.IOException;

public class NewView {

    public static void main(String[] args) throws IOException {
        TextMenu menu = new TextMenu();
        Controller c1, c2, c3, c4, c5;
        IStatement s1 = new CompoundStatement(new VarDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        IStatement s2 = new CompoundStatement(new VarDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VarDeclarationStatement("b", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ArithmeticExpression(new ValueExpression(new IntValue(2)), new
                                ArithmeticExpression(new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), '*'), '+')),
                                new CompoundStatement(new AssignStatement("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new
                                        IntValue(1)), '+')), new PrintStatement(new VariableExpression("b"))))));

        IStatement s3 = new CompoundStatement(new VarDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VarDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));

        IStatement s4 = new CompoundStatement(
                new VarDeclarationStatement("varf", new StringType()),
                new CompoundStatement(
                        new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompoundStatement(
                                new OpenReadFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(
                                        new VarDeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(
                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseReadFileStatement(new VariableExpression("varf"))))))))));

        IStatement s5 = new CompoundStatement(new VarDeclarationStatement("v", new RefType(new IntType())),
        new CompoundStatement(new HeapNewStatement("v", new ValueExpression(new IntValue(20))),
        new CompoundStatement(new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
        new CompoundStatement(new HeapNewStatement("v", new ValueExpression(new IntValue(30))),
                new PrintStatement(new HeapReadExpression(new VariableExpression("v")))))));
        c1 = new Controller(false);
        c2 = new Controller(false);
        c3 = new Controller(false);
        c4 = new Controller(false);
        c5 = new Controller(false);

        c1.addProgram(s1);
        c2.addProgram(s2);
        c3.addProgram(s3);
        c4.addProgram(s4);
        c5.addProgram(s5);

        RunCommand r1, r2, r3, r4, r5;

        r1 = new RunCommand("1", "int v; v=2; Print(v)", c1);
        r2 = new RunCommand("2", "int a; int b; a=2+3*5; b=a+1; Print(b)", c2);
        r3 = new RunCommand("3", "bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)", c3);
        r4 = new RunCommand("4", "string varf; varf=\"test.in\"; openReadFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeReadFile(varf)", c4);
        r5 = new RunCommand("5", "Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a))) -- error", c5);
        menu.addCommand(r1);
        menu.addCommand(r2);
        menu.addCommand(r3);
        menu.addCommand(r4);
        menu.addCommand(r5);
        menu.addCommand(new ExitCommand("6", "Exit"));

        menu.show();
    }

}