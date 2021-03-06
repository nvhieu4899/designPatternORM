import annotation.Entity;
import annotation.ManyToOne;

@Entity
public class PhoneNumber {

    private Long idStudent;
    private String number;

    @ManyToOne(keyPropertyName = "idStudent")
    private SinhV student;

    public long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(long idStudent) {
        this.idStudent = idStudent;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SinhV getStudent() {
        return student;
    }

    public void setStudent(SinhV sinhV) {
        this.student = sinhV;
    }
}
