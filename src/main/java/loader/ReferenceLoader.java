package loader;

import java.util.List;

public interface ReferenceLoader<T> {


    void load(List<T> entities, String propertyName);

}
