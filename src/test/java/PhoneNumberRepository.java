public class PhoneNumberRepository extends AbstractRepository<PhoneNumber, Long>{
    public PhoneNumberRepository() {
        super(PhoneNumber.class, Long.class);
    }

}
