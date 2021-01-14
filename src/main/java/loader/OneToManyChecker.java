package loader;

import annotation.OneToMany;

import java.lang.reflect.Field;

public class OneToManyChecker<T> extends RelationshipChecker<T>{
    public OneToManyChecker(Class<T> persistenceClass) {
        super(persistenceClass);
    }

    @Override
    protected RelationshipChecker getNextChecker() {
        return new ManyToOneChecker(persistenceClass);
    }

    @Override
    protected void initializeLoader() {
        loader = new OneToManyLoader<>(persistenceClass);
    }

    @Override
    protected boolean isAppropriate(Field field) {
        return field.isAnnotationPresent(OneToMany.class);
    }
}
