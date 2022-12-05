package Model.DataStructures;

import Model.Values.IValue;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

public class ADTDictionary<k, v> implements IDictionary<k, v> {

    public final Hashtable<k, v> data = new Hashtable<>();

    @Override
    public int size() {
        synchronized (data) {
            return data.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (data) {
            return data.isEmpty();
        }
    }

    @Override
    public Enumeration<k> keys() {
        synchronized (data) {
            return data.keys();
        }
    }

    @Override
    public Enumeration<v> elements() {
        synchronized (data) {
            return data.elements();
        }
    }

    @Override
    public v get(k key) {
        synchronized (data) {
            return data.get(key);
        }
    }

    @Override
    public v put(k key, v value) {
        synchronized (data) {
            return data.put(key, value);
        }
    }

    @Override
    public v remove(k key) {
        synchronized (data) {
            return data.remove(key);
        }
    }

    @Override
    public boolean containsKey(k id) {
        synchronized (data) {
            return data.containsKey(id);
        }
    }

    @Override
    public IDictionary<k, v> copy() {
        IDictionary<k, v> copyDictionary = new ADTDictionary<>();
        for (k key : new HashSet<k>(Collections.list(this.keys())))
            copyDictionary.put(key, get(key));
        return copyDictionary;
    }


}
