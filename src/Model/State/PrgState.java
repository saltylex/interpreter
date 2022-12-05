package Model.State;


import Model.DataStructures.*;
import Model.Exceptions.EvaluationException;
import Model.Exceptions.ExecutionError;
import Model.Exceptions.StackException;
import Model.Statements.IStatement;
import Model.Values.IValue;
import Model.Values.StringValue;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;


public class PrgState {
    public IStack<IStatement> exeStack;
    public IDictionary<String, IValue> symTable;
    public IList<String> out;

    public IDictionary<StringValue, BufferedReader> fileTable;
    public IHeap heap;

    private static final TreeSet<Integer> ids = new TreeSet<>();
    public int id;

    public PrgState(IStatement originalProgram) {
        this.exeStack = new ADTStack<>();
        this.symTable = new ADTDictionary<>();
        this.out = new ADTList<>();
        this.fileTable = new ADTDictionary<>();
        this.heap = new ADTHeap();
        exeStack.push(originalProgram);
        this.id = newId();
    }



    // ADDED FOR MULTITHREADING AND MANAGING THE IDs OF THE STATE
    private static Integer newId() {
        Random random = new Random();
        Integer id;
        synchronized (ids) {
            do {
                id = random.nextInt();
            } while (ids.contains(id));
            ids.add(id);
        }
        return id;
    }


    public PrgState(IStack<IStatement> executionStack, IDictionary<String, IValue> symTable, IList<String> out, IDictionary<StringValue, BufferedReader> fileTable, IHeap heap) {
        this.exeStack = executionStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
    }

    public IStack<IStatement> getExeStack() {
        return exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public IList<String> getOut() {
        return out;
    }

    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public IHeap getHeap() {
        return heap;
    }

    public PrgState oneStep() throws StackException, ExecutionError, EvaluationException {

        IStatement top = exeStack.pop();
        return top.execute(this);

    }

    public boolean isCompleted() {
        return exeStack.isEmpty();
    }

    // ADDED FOR MULTITHREADING
    public void executeOneStep() throws EvaluationException, ExecutionError, StackException {
        if (exeStack.isEmpty())
            throw new ExecutionError("Exe stack is empty!");
        IStatement currentStatement = exeStack.pop(); // get first statement
        currentStatement.execute(this); // execute the statement

        System.out.println(this.toString() + "\n");// display state after execution
    }

    public String executionStackToString() {
        StringBuilder executionStackStringBuilder = new StringBuilder();

        for (IStatement statement : exeStack) {
            executionStackStringBuilder.append(statement.toString()).append("|");
        }

        return executionStackStringBuilder.toString();
    }

    public String symTableToString() {
        StringBuilder symbolTableStringBuilder = new StringBuilder();

        for (String key : Collections.list(symTable.keys())) { // enumeration to list
            symbolTableStringBuilder.append(String.format("%s -> %s\n", key, symTable.get(key).toString()));
        }

        return symbolTableStringBuilder.toString();
    }

    public String outToString() {
        StringBuilder outStringBuilder = new StringBuilder();

        for (String object : out) {
            outStringBuilder.append(object).append("\n");
        }

        return outStringBuilder.toString();
    }

    public String fileTableToString() {
        StringBuilder fileTableStringBuilder = new StringBuilder();

        for (StringValue key : Collections.list(fileTable.keys())) { // enumeration to list
            fileTableStringBuilder.append(String.format("%s -> %s\n", key.toString(), fileTable.get(key).toString()));
        }

        return fileTableStringBuilder.toString();
    }

    public String heapToString() {
        StringBuilder heapStringBuilder = new StringBuilder();

        for (Integer position : heap.getContent().keySet()) {
            heapStringBuilder.append(position + " -> " + heap.getContent().get(position).toString() + "\n");
        }

        return heapStringBuilder.toString();

    }

    @Override
    public String toString() {
        return String.format("ID: %d\n\n~ EXECUTION STACK ~\n%s\n~ SYMBOL TABLE ~\n%s\n~ OUTPUT ~\n%s\n~ FILE TABLE ~\n%s\n~ HEAP ~\n%s\n", id, executionStackToString(), symTableToString(), outToString(), fileTableToString(), heapToString());
    }

}
