package Model.Statements;

import Model.Exceptions.EvaluationException;
import Model.Expressions.IExpression;
import Model.State.PrgState;
import Model.Types.BoolType;
import Model.Values.BoolValue;
import Model.Values.IValue;

public class WhileStatement implements IStatement {

    private final IExpression expression;
    private final IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws EvaluationException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (!value.getType().equals(new BoolType()))
            throw new EvaluationException(value + " is not of type BoolType!");
        BoolValue boolValue = (BoolValue) value;
        if (boolValue.getVal()) {
            state.getExeStack().push(this);
            state.getExeStack().push(statement);
        }
        return null;
    }

    @Override
    public String toString() {
        return "While(" + expression + "){\n" + statement + "}";
    }
}
