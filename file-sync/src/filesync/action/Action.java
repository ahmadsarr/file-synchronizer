package filesync.action;

import java.io.Serializable;

public interface Action extends Comparable, Serializable {


    @Override
    public int compareTo(Object o);

    @Override
    public String toString() ;
}


