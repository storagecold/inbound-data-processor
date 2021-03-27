@Repository
public interface CustomerProfileRepo {

    SubmitterDefinition getCustomerProfile(String fileId, String submitterId);

    SubmitterFullDetail getSubmitterFullDetail(String fileId, String submitterId);
}
