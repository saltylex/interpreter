package Model.Statements;

import Model.DataStructures.IDictionary;
import Model.Exceptions.EvaluationException;
import Model.Exceptions.ExecutionError;
import Model.Expressions.IExpression;
import Model.State.PrgState;
import Model.Types.StringType;
import Model.Values.IValue;
import Model.Values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFileStatement implements IStatement {
    private final IExpression expression;

    public CloseReadFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionError, EvaluationException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (!value.getType().equals(new StringType()))
            throw new EvaluationException(expression + " does not evaluate to StringValue!");
        StringValue fileName = (StringValue) value;
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        if (!fileTable.containsKey(fileName))
            throw new EvaluationException(value + " is not present in the symTable!");
        BufferedReader br = fileTable.get(fileName);
        try {
            br.close();
        } catch (IOException e) {
            throw new EvaluationException("Unexpected error in closing" + value + "!");
        }
        fileTable.remove(fileName);
        return null;
    }

    @Override
    public String toString() {
        return String.format("CloseReadFile{%s}", expression);
    }
}
