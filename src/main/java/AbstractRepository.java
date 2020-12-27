import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractRepository<T, ID> {
    private Class<T> tClass;
    private Class<ID> idClass;

    public List<T> findAll() {

    }

    public T findById(ID id) {

    }

    public T delete(T obj) {

    }

    public T save(T obj) {

    }

    public AbstractRepository() {
        this.tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
