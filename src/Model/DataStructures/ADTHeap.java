package Model.DataStructures;

import Model.Exceptions.EvaluationException;
import Model.Exceptions.ExecutionError;
import Model.Values.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ADTHeap implements IHeap {
    private final Map<Integer, IValue> map;
    private Integer freeValue;

    public Integer newValue() { // to generate a random free space in heap memory
        Random rand = new Random();
        freeValue = rand.nextInt();

        if (freeValue == 0 || map.containsKey(freeValue))
            freeValue = rand.nextInt();

        return freeValue;
    }

    public ADTHeap(Map<Integer, IValue> map) {
        this.map = map;
        freeValue = newValue();
    }

    public ADTHeap() {
        map = new HashMap<>();
        freeValue = newValue();
    }

    @Override
    public Integer getFreeValue() {
        synchronized (this) { // threads cannot modify heap at the same time
            return freeValue;
        }
    }

    @Override
    public Map<Integer, IValue> getContent() {
        synchronized (this) {
            return map;
        }
    }

    @Override
    public void setContent(Map<Integer, IValue> newMap) {
        synchronized (this) {
            map.clear();
            for (Integer i : newMap.keySet()) {
                map.put(i, newMap.get(i));
            }
        }
    }

    @Override
    public Integer add(IValue value) {
        synchronized (this) {
            map.put(freeValue, value);
            Integer freeAddressWhere = freeValue;
            freeValue = newValue();
            return freeAddressWhere;
        }
    }

    @Override
    public void update(Integer position, IValue value) throws ExecutionError {
        synchronized (this) {
            if (!map.containsKey(position))
                throw new ExecutionError(position + " is not a valid position in the heap!");
            map.put(position, value);
        }
    }

    @Override
    public IValue get(Integer position) throws EvaluationException {
        synchronized (this) {
            if (!map.containsKey(position))
                throw new EvaluationException(position + " is not a valid position in the heap!");
            return map.get(position);
        }
    }
}
