import annotation.Column;
import annotation.Entity;

import java.sql.Date;

@Entity
public class SinhVien {
    @Column(name = "id")
    private String mssv;
    @Column(name = "name")
    private String hoVaTen;

    @Column(name = "dob")
    private Date ngaySinh;

    public SinhVien(String mssv, String hoVaTen, Date ngaySinh) {
        this.mssv = mssv;
        this.hoVaTen = hoVaTen;
        this.ngaySinh = ngaySinh;
    }

    public SinhVien() {
    }
}
