import loader.LoadManager;
import org.junit.Test;

import java.util.List;

public class TestNavigationProperty {

    @Test
    public void testReference() {
        StudentRepository repository = new StudentRepository();
        List<SinhV> sinhVS = repository.findAll();
        assert sinhVS != null;
        assert sinhVS.size() > 0;
        System.out.println(System.lineSeparator() + "Test Select all result:");
        for (SinhV c : sinhVS) {
            System.out.println(c);

        }

        (new LoadManager<>(SinhV.class)).load(sinhVS, "phoneNumbers");
        assert sinhVS.get(0).getPhoneNumbers() != null;

        for (SinhV sinhV : sinhVS) {
            System.out.println("Name: " + sinhV.getName());
            if (sinhV.getPhoneNumbers() != null) {
                for (PhoneNumber phoneNumber : sinhV.getPhoneNumbers()) {
                    System.out.println("- " + phoneNumber.getNumber());
                }
            }
        }
    }

    @Test
    public void TestOneToOne() {
        StudentRepository repository = new StudentRepository();
        List<SinhV> sinhVS = repository.findAll();
        assert sinhVS != null;
        assert sinhVS.size() > 0;
        System.out.println(System.lineSeparator() + "Test Select all result:");
        for (SinhV c : sinhVS) {
            System.out.println(c);

        }

        (new LoadManager<SinhV>(SinhV.class)).load(
                sinhVS, "account");
        assert sinhVS.get(0).getAccount() != null;

        for (SinhV sinhV : sinhVS) {
            System.out.print("Name: " + sinhV.getName() + "| Account: ");
            if (sinhV.getAccount() != null)
                System.out.println(sinhV.getAccount().getUserName());
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

        (new LoadManager<>(PhoneNumber.class)).load(
                numbers, "student");
        assert numbers.get(0).getStudent() != null;

        for (PhoneNumber number : numbers) {
            System.out.print("Name: " + number.getNumber() + "| Account: ");
            if (number.getStudent() != null)
                System.out.println(number.getStudent().getName());
        }
    }
}
