package Model.Expressions;

import Model.DataStructures.IDictionary;
import Model.DataStructures.IHeap;
import Model.Exceptions.EvaluationException;
import Model.Values.IValue;
import Model.Values.RefValue;

public class HeapReadExpression implements IExpression {
    @Override
    public IValue eval(IDictionary<String, IValue> tbl, IHeap hp) throws EvaluationException {
        IValue evaluated = expression.eval(tbl, hp);
        if (!(evaluated instanceof RefValue))
            throw new EvaluationException(evaluated + " is not of type RefType!");
        RefValue referenceValue = (RefValue) evaluated;
        return hp.get(referenceValue.getAddr());
    }

    private final IExpression expression;

    public HeapReadExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "ReadHeap{"+expression+"}";
    }
}
