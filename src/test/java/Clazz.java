import annotation.Column;
import annotation.Entity;
import annotation.OneToMany;

import java.util.Collection;

@Entity
public class Clazz {
    private Long id;
    @Column(name = "displayName")
    private String name;
    private Long idStudent;

    @OneToMany
    private Collection<Student> student;


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

    public Long getIdStudent() {
        return idStudent;
    }

    public Collection<Student> getStudent() {
        return student;
    }

    public void setStudent(Collection<Student> student) {
        this.student = student;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }
}
