package loader;

import java.lang.reflect.Field;
import java.util.List;

public class LoadManager<T> {
    Class<T> persistenceClass;
    RelationshipChecker<T> defaultChecker;

    public LoadManager(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
        defaultChecker = new OneToOneChecker<>(persistenceClass);
    }

    public LoadManager<T> load(List<T> entities, String propertyName) {
        try {
            Field propertyField = persistenceClass.getDeclaredField(propertyName);
            ReferenceLoader<T> loader = defaultChecker.check(propertyField);

            if (loader != null)
                loader.load(entities, propertyName);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return this;
    }
}
