package Model.Statements;

import Model.Exceptions.EvaluationException;
import Model.Expressions.IExpression;
import Model.State.PrgState;

public class PrintStatement implements IStatement {
    private final IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws EvaluationException {
        state.getOut().add(expression.eval(state.getSymTable(), state.getHeap()).toString());
        return null;
    }

    @Override
    public String toString() {
        return String.format("print(%s)", expression.toString());
    }
}
