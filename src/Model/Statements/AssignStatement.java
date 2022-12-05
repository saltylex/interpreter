package Model.Statements;

import Model.DataStructures.IDictionary;
import Model.DataStructures.IHeap;
import Model.Exceptions.EvaluationException;
import Model.Exceptions.ExecutionError;
import Model.Expressions.IExpression;
import Model.State.PrgState;
import Model.Types.IType;
import Model.Values.IValue;

// v = 5
public class AssignStatement implements IStatement {
    public String id; // v
    public IExpression expression; // 5

    public AssignStatement(String id, IExpression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionError, EvaluationException {
        IDictionary<String, IValue> symbolTable = state.getSymTable(); // vars
        IHeap hp = state.getHeap();
        IType type = symbolTable.get(id).getType(); // check type of declared variable
        IValue value = expression.eval(symbolTable, hp); // gets the value

        if (!value.getType().equals(type)) {
            throw new ExecutionError("Mismatch between variable type and assigned expression!");
        }
        if (!symbolTable.containsKey(id)) {
            throw new ExecutionError(String.format("Variable %s was not declared in this scope!", id));
        }

        symbolTable.put(id, value); // assign

        return null;
    }

    @Override
    public String toString() {
        return String.format("%s=%s", id, expression);
    }

}
