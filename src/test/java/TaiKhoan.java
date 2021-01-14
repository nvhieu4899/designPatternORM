import annotation.Entity;
import annotation.Id;
import annotation.OneToOne;

@Entity(table = "Account")
public class TaiKhoan {
    @Id
    private  Long idStudent;
    private String userName;
    @OneToOne
    private SinhV sinhV;

    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SinhV getStudent() {
        return sinhV;
    }

    public void setStudent(SinhV sinhV) {
        this.sinhV = sinhV;
    }
}
