package net.codejava;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import net.codejava.airport.Airport;
import net.codejava.airport.AirportID;
import net.codejava.airport.AirportRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AirportRepositoryTests {

	@Autowired private AirportRepository repo;
	
	@Test
	public void testSaveNew() {
		Airport airport = new Airport();
		airport.setCountryCode("VN");
		airport.setCityCode("HAN");
		airport.setName("Noi Bai International Airport");
		
		Airport savedAirport = repo.save(airport);
		
		assertThat(savedAirport).isNotNull();
		assertThat(savedAirport.getCountryCode()).isEqualTo("VN");
		assertThat(savedAirport.getCityCode()).isEqualTo("HAN");
	}
	
	@Test
	public void testListAll() {
		Iterable<Airport> airports = repo.findAll();
		
		assertThat(airports).isNotEmpty();
		airports.forEach(System.out::println);
	}	
	
	@Test
	public void testFindById() {
		AirportID id = new AirportID();
		id.setCityCode("HAN");
		id.setCountryCode("VN");
		
		Optional<Airport> result = repo.findById(id);
		assertThat(result).isPresent();
	}
}
