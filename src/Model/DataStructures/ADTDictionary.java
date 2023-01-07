package Model.DataStructures;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

// wrapper class used by the file & symbol tables.
public class ADTDictionary<k, v> implements IDictionary<k, v> {

    Hashtable<k, v> data = new Hashtable<>();

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public Enumeration<k> keys() {
        return data.keys();
    }

    @Override
    public Enumeration<v> elements() {
        return data.elements();
    }

    @Override
    public v get(k key) {
        return data.get(key);
    }

    @Override
    public v put(k key, v value) {
        return data.put(key, value);
    }

    @Override
    public v remove(k key) {
        return data.remove(key);
    }

    @Override
    public boolean containsKey(k id) {
        return data.containsKey(id);
    }

    @Override
    public IDictionary<k, v> copy() {
        IDictionary<k, v> toReturn = new ADTDictionary<>();
        for (k key : data.keySet())
            toReturn.put(key, get(key));
        return toReturn;
    }

    @Override
    public Map<k, v> getContent() {
        return data;
    }


}
