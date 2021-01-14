package Loader;

import annotation.OneToOne;

import java.lang.reflect.Field;

public class OneToOneChecker<T> extends RelationshipChecker<T> {
    public OneToOneChecker(Class<T> persistenceClass) {
        super(persistenceClass);
    }

    @Override
    protected RelationshipChecker getNextChecker() {
        return new OneToManyChecker(persistenceClass);
    }

    @Override
    protected void initializeLoader() {
        loader = new OneToOneLoader<>(persistenceClass);
    }

    @Override
    protected boolean isAppropriate(Field field) {
        return field.isAnnotationPresent(OneToOne.class);
    }
}
