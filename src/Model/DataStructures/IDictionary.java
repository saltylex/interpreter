package Model.DataStructures;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

public interface IDictionary<k, v> {

    int size();

    boolean isEmpty();

    Enumeration<k> keys();

    Enumeration<v> elements();

    v get(k key);

    v put(k key, v value);

    v remove(k key);

    boolean containsKey(k id);

    IDictionary<k, v> copy();

    Map<k, v> getContent();


}
