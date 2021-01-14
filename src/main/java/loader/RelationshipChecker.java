package loader;

import java.lang.reflect.Field;

public abstract class RelationshipChecker<T> {
    protected Class<T> persistenceClass;
    protected ReferenceLoader<T> loader;

    protected abstract RelationshipChecker<T> getNextChecker();

    protected abstract void initializeLoader();

    protected abstract boolean isAppropriate(Field field);

    public ReferenceLoader<T> check(Field field) {
        if (isAppropriate(field)) {
            if (loader == null)
                initializeLoader();

            return loader;
        }

        if (getNextChecker() != null)
            return getNextChecker().check(field);
        return null;
    }

    public RelationshipChecker(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }
}
