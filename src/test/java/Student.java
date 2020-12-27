import annotation.Column;
import annotation.Entity;
import annotation.Id;

import java.sql.Date;

@Entity(table = "sinhvien")
public class Student {
    @Id
    @Column(name = "id")
    private String mssv;
    @Column(name = "name")
    private String hoVaTen;

    @Column(name = "dob")
    private Date ngaySinh;

    public Student(String mssv, String hoVaTen, Date ngaySinh) {
        this.mssv = mssv;
        this.hoVaTen = hoVaTen;
        this.ngaySinh = ngaySinh;
    }

    public Student() {
    }

    public String getMssv() {
        return mssv;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }
}
