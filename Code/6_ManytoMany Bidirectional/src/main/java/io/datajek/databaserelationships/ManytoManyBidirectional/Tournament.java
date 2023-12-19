package io.datajek.databaserelationships.ManytoManyBidirectional;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.datajek.databaserelationships.OnetoManyBidirectional.Registration;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Tournament {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String location;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "tournament_id")
	private List<Registration> registrations = new ArrayList<>();



	// TONOTE: We will not use cascade type REMOVE as we do not want to delete tournaments when we
	// delete a category. We will also not use cascade type PERSIST, because that will cause an
	// error if we try to add a tournament with nested category values.
	// @JsonIgnoreProperties("tournaments") avoid infinite loop
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "tournament_categories", joinColumns = @JoinColumn(name = "tournament_id"), // FK
																									// of
																									// the
																									// owning
																									// side
			inverseJoinColumns = @JoinColumn(name = "category_id") // FK of inverse side
	)
	@JsonIgnoreProperties("tournaments")
	private List<Category> playingCategories = new ArrayList<>();

	public Tournament() {}

	public Tournament(String name, String location) {
		super();
		this.name = name;
		this.location = location;
	}

	public Tournament(String name, String location, List<Registration> registrations) {
		super();
		this.name = name;
		this.location = location;
		this.registrations = registrations;
	}

	// set up one to many relationship
	public void addRegistration(Registration reg) {
		registrations.add(reg);
	}

	// remove registration
	public void removeRegistration(Registration reg) {
		if (registrations != null)
			registrations.remove(reg);
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}


	// set up many to many relationship owner side where we manage the relationship
	public void addCategory(Category category) {
		playingCategories.add(category);
		// TONOTE: set up bidirectional relationship
		category.getTournaments().add(this);
	}

	// remove category
	public void removeCategory(Category category) {
		if (playingCategories != null)
			playingCategories.remove(category);
		// TONOTE: update bidirectional relationship
		category.getTournaments().remove(this);
	}

	public List<Category> getPlayingCategories() {
		return playingCategories;
	}

	public void setPlayingCategories(List<Category> playingCategories) {
		this.playingCategories = playingCategories;
	}

	// end of many to many relationship


	@Override
	public String toString() {
		return "Tournament [id=" + id + ", name=" + name + ", location=" + location
				+ ", registrations=" + registrations + ", playingCategories=" + playingCategories
				+ "]";
	}
}
