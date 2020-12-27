import annotation.Column;
import annotation.Entity;

@Entity
public class Category {

    @Column(name="Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Order")
    private Long order;

    public Category() {
    }

    public Category(Long id, String name, Long order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", order=" + order +
                '}';
    }
}
