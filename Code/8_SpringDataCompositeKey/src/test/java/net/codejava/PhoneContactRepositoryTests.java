package net.codejava;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import net.codejava.phone.PhoneContact;
import net.codejava.phone.PhoneContactRepository;
import net.codejava.phone.PhoneID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class PhoneContactRepositoryTests {

	@Autowired private PhoneContactRepository repo;
	
	@Test
	public void testSaveNew() {
		PhoneID id = new PhoneID(1, "12027933129");
		PhoneContact newContact = new PhoneContact();
		newContact.setId(id);
		newContact.setFirstName("John");
		newContact.setLastName("Kellyson");
		
		PhoneContact savedContact = repo.save(newContact);
		
		assertThat(savedContact).isNotNull();
		assertThat(savedContact.getId().getAreaCode()).isEqualTo(1);
		assertThat(savedContact.getId().getNumber()).isEqualTo("12027933129");
	}
	
	@Test
	public void testListAll() {
		List<PhoneContact> contacts = repo.findAll();
		
		assertThat(contacts).isNotEmpty();
		contacts.forEach(System.out::println);
	}
	
	@Test
	public void testFindById() {
		PhoneID id = new PhoneID(1, "12027933129");
		Optional<PhoneContact> result = repo.findById(id);
		
		assertThat(result).isPresent();
	}
}
