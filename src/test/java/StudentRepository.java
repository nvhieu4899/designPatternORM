public class StudentRepository extends AbstractRepository<Student, Long> {

    public StudentRepository() {
        super(Student.class, Long.class);
    }
}
