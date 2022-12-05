package Controller;

import Model.DataStructures.ADTStack;
import Model.DataStructures.IStack;
import Model.Exceptions.*;
import Model.State.PrgState;
import Model.Statements.IStatement;
import Model.Values.IValue;
import Model.Values.RefValue;
import Repository.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class Controller {

    private final IRepository repository;

    public ExecutorService executor;
    public boolean stepByStep;


    public Controller() {
        repository = new Repository("C:\\Users\\lexig\\IdeaProjects\\a3\\logFile");
        this.stepByStep = true;
    }

    public Controller(boolean stepByStep) {
        repository = new Repository("C:\\Users\\lexig\\IdeaProjects\\a3\\logFile");
        this.stepByStep = stepByStep;
    }

    public IRepository getRepository() {
        return repository;
    }

    public void addProgram(IStatement statement) {
        repository.addProgramState(new PrgState(statement));
    }

    // normal: gc removes those addresses not referred from symbol table and other
    // heap entries.

    // unsafe: gc removes all heap entries whose keys (addresses) do not occur in the
    // address fields of the RefValues from the symbol table

    Map<Integer, IValue> unsafeGarbageCollector(Set<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void oneStepForEachProgram(List<PrgState> programStateList) throws EvaluationException, ExecutionError {
        programStateList.forEach(prg -> {
            try {
                repository.logProgramStateExecution(prg);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        });
        List<Callable<PrgState>> callList = programStateList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());
        List<PrgState> newProgramsList = null;
        try {
            newProgramsList = executor.invokeAll(callList).stream()
                    .map(future ->
                    {
                        try {
                            return future.get();
                        } catch (ExecutionException | InterruptedException ex) {
                            System.out.println(ex.toString());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull).toList();

        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        programStateList.addAll(newProgramsList);

        /// We save again all the program states, including the old and the new ones to a file
        programStateList.forEach(p -> {
            try {
                this.repository.logProgramStateExecution(p);
            } catch (IOException e) {
                // ...
                throw new IllegalArgumentException("Unexpected error in logging"+p.toString()+"!");
            }
        });

        this.repository.setProgramStates(programStateList);
}

    List<PrgState> removeCompletedPrograms(List<PrgState> programStateList) {
        List<PrgState> foundStates = programStateList.stream()
                .filter(p -> !p.isCompleted())
                .collect(Collectors.toList());
        if (foundStates.isEmpty() && !programStateList.isEmpty())
            foundStates.add(programStateList.get(0));
        return foundStates;
    }

    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    // DEPRECATED BECAUSE OF MULTITHREADING
    public void executeOneStep(PrgState programState) throws EvaluationException, ExecutionError, StackException {
        IStack<IStatement> executionStack = programState.getExeStack(); // get the stack

        IStatement currentStatement = executionStack.pop(); // get first statement
        currentStatement.execute(programState); // execute the statement

        displayCurrentState(programState); // display state after execution
    }

    public void executeAllSteps() throws EvaluationException, ExecutionError, StackException {
        PrgState currentProgramState = repository.getCurrentProgramStates(); // get state

        displayCurrentState(currentProgramState); // display state
        try {
            repository.logProgramStateExecution(currentProgramState);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        while (!currentProgramState.getExeStack().isEmpty()) { // while we can still execute commands
            executeOneStep(currentProgramState);

            try {
                repository.logProgramStateExecution(currentProgramState);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            currentProgramState.getHeap().setContent(unsafeGarbageCollector(new HashSet<>(getAddrFromSymTable(Collections.list(currentProgramState.getSymTable().elements()))),
                    currentProgramState.getHeap().getContent()));
        }
    }

    public void stepByStepExecution() throws EvaluationException, ExecutionError, StackException {
        ADTStack<IStatement> currentStack = (ADTStack<IStatement>) this.repository.getCurrentProgramState().getExeStack();
        if (currentStack.isEmpty()) {
            throw new ExecutionError("");
        }

        IStatement currentInstruction = currentStack.pop();
        this.repository.addProgramState(currentInstruction.execute(this.repository.getCurrentProgramState()));

        System.out.println(this.repository.toString());
        if (this.stepByStep) {
            new Scanner(System.in).next();
            this.stepByStepExecution();
        }

    }



    public void displayCurrentState(PrgState programState) {
        System.out.println(programState.toString() + "\n");
    }
}