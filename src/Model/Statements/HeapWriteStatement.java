package Model.Statements;

import Model.DataStructures.IDictionary;
import Model.DataStructures.IHeap;
import Model.Exceptions.EvaluationException;
import Model.Exceptions.ExecutionError;
import Model.Expressions.IExpression;
import Model.State.PrgState;
import Model.Values.IValue;
import Model.Values.RefValue;

public class HeapWriteStatement implements IStatement {

    private final String varName;
    private final IExpression expression;


    public HeapWriteStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionError, EvaluationException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (!symTable.containsKey(varName))
            throw new EvaluationException(varName + " is not present in the symbol table!");
        IValue varValue = symTable.get(varName);
        if (!(varValue instanceof RefValue))
            throw new EvaluationException(varValue + " is not of type RefType!");
        RefValue referenceValue = (RefValue) varValue;
        IValue evaluated = expression.eval(symTable, heap);
        if (!evaluated.getType().equals(referenceValue.getLocationType()))
            throw new EvaluationException(evaluated + " is not of type " + referenceValue.getLocationType());
        heap.update(referenceValue.getAddr(), evaluated);
        return null;
    }


    @Override
    public String toString() {
        return "WriteHeap{" + varName + ", " + expression + "}";
    }
}
