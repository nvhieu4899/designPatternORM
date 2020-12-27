package manipulation;

import org.example.map.Column;
import org.example.map.Table;

@Table
public class Category {
    @Column(fieldName = "Id")
    private Long id;

    @Column(fieldName = "Name")
    private String name;

    @Column(fieldName = "Order")
   private Long order;

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
