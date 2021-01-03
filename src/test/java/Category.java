import annotation.Column;
import annotation.Entity;

import java.sql.Date;

@Entity
public class Category {

    @Column(name="Id")
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Order")
    private Long order;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    public Category() {
    }

    public Category(Long id, String name, Long order, Date modifiedDate) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.modifiedDate = modifiedDate;
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
