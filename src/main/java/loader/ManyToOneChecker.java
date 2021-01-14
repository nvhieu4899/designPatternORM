package loader;

import annotation.ManyToOne;

import java.lang.reflect.Field;

public class ManyToOneChecker<T> extends RelationshipChecker<T> {
    public ManyToOneChecker(Class<T> persistenceClass) {
        super(persistenceClass);
    }

    @Override
    protected RelationshipChecker getNextChecker() {
        return null;
    }

    @Override
    protected void initializeLoader() {
        loader = new ManyToOneLoader<>(persistenceClass);
    }

    @Override
    protected boolean isAppropriate(Field field) {
        return field.isAnnotationPresent(ManyToOne.class);
    }
}
