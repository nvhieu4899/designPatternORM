import Loader.ManyToOneLoader;
import Loader.OneToManyLoader;
import Loader.OneToOneLoader;
import annotation.ManyToOne;
import org.junit.Test;

import java.util.List;

public class TestNavigationProperty {

    @Test
    public void testReference() {
        StudentRepository repository = new StudentRepository();
        List<Student> students = repository.findAll();
        assert students != null;
        assert students.size() > 0;
        System.out.println(System.lineSeparator() + "Test Select all result:");
        for (Student c : students) {
            System.out.println(c);

        }

//ReferenceLoader.load(students.get(0), "phoneNumbers");
        (new OneToManyLoader<Student>(Student.class)).load(students, "phoneNumbers");
        assert students.get(0).getPhoneNumbers() != null;

        for (Student student : students) {
            System.out.println("Name: " + student.getName());
            if (student.getPhoneNumbers() != null) {
                for (PhoneNumber phoneNumber : student.getPhoneNumbers()) {
                    System.out.println("- " + phoneNumber.getNumber());
                }
            }
        }
//        for (PhoneNumber st : students.get(0).getPhoneNumbers()) {
//            System.out.println(st.getNumber());
//        }
//        assert clazz!=null;
//
//        System.out.println(clazz.getId());
//        System.out.println(clazz.getIdStudent());
//        System.out.println(clazz.getName());
    }

    @Test
    public void TestOneToOne() {
        StudentRepository repository = new StudentRepository();
        List<Student> students = repository.findAll();
        assert students != null;
        assert students.size() > 0;
        System.out.println(System.lineSeparator() + "Test Select all result:");
        for (Student c : students) {
            System.out.println(c);

        }

        (new OneToOneLoader(Student.class)).load(
                students, "account");
        assert students.get(0).getAccount() != null;

        for (Student student : students) {
            System.out.print("Name: " + student.getName() + "| Account: ");
            if (student.getAccount() != null)
                System.out.println(student.getAccount().getUserName());
        }
    }

    @Test
    public void TestManyToOne() {
        PhoneNumberRepository repository = new PhoneNumberRepository();
        List<PhoneNumber> numbers = repository.findAll();
        assert numbers != null;
        assert numbers.size() > 0;
        System.out.println(System.lineSeparator() + "Test Select all result:");
        for (PhoneNumber c : numbers) {
            System.out.println(c);

        }

        (new ManyToOneLoader<>(PhoneNumber.class)).load(
                numbers, "student");
        assert numbers.get(0).getStudent() != null;

        for (PhoneNumber number : numbers) {
            System.out.print("Name: " + number.getNumber() + "| Account: ");
            if (number.getStudent() != null)
                System.out.println(number.getStudent().getName());
        }
    }
}
