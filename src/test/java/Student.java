import annotation.*;

import java.util.Collection;

@Entity
public class Student {
    @Key
    private Long id;
    @Column(name = "displayName")
    private String name;
    private Long age;
    @OneToMany
    private Collection<PhoneNumber> phoneNumbers;
    @OneToOne
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Collection<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Collection<PhoneNumber> students) {
        this.phoneNumbers = students;
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

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }
}
