public class CategoryRepository extends AbstractRepository<Category, Long> {
    public CategoryRepository() {
        super(Category.class, Long.class);
    }
}
