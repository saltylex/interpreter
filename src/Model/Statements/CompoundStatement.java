package Model.Statements;

import Model.DataStructures.IStack;
import Model.State.PrgState;

// plenty of them
public class CompoundStatement implements IStatement {
    IStatement first;
    IStatement second;

    @Override
    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }

    public CompoundStatement(IStatement first, IStatement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public PrgState execute(PrgState state) {
        IStack<IStatement> stack = state.getExeStack();
        stack.push(second);
        stack.push(first);
        return null;
    }
}
