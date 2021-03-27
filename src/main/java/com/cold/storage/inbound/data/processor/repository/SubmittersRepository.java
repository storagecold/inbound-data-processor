@Repository
public interface SubmittersRepository extends MongoRepository<Submitters, String> {
	Submitters findBy_id(ObjectId _id);
	Submitters findBySubmittersCategoryCode(int submittersCategoryCode);
}
