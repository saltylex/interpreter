package Model.Statements;

import Model.Exceptions.EvaluationException;
import Model.Exceptions.ExecutionError;
import Model.State.PrgState;

public interface IStatement {
    PrgState execute(PrgState state) throws ExecutionError, EvaluationException;       //which is the execution method for a statement
}
