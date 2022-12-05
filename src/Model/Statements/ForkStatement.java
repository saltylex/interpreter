package Model.Statements;

import Model.DataStructures.ADTStack;
import Model.DataStructures.IStack;
import Model.Exceptions.EvaluationException;
import Model.Exceptions.ExecutionError;
import Model.State.PrgState;

public class ForkStatement implements IStatement {
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "Fork{\n"+statement+"\n}";
    }
    @Override
    public PrgState execute(PrgState state) throws ExecutionError, EvaluationException {
        IStack<IStatement> newExeStack = new ADTStack<>();
        newExeStack.push(statement);
        return new PrgState(newExeStack, state.getSymTable().copy(),
                state.getOut(), state.getFileTable(), state.getHeap());
    }
}
