package io.datajek.databaserelationships.ManytoManyUnidirectional;

import java.util.ArrayList;
import java.util.List;
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

	// IMPORTANT: In a unidirectional many-to-many relationship, we put the
	// relationship on the
	// referenced/child side.
	// So, in the Tournament class we have to put a link to the Category
	// class. Since a tournament can have more than one categories, we will create a
	// List of
	// categories
	// TONOTE: We will not use cascade type REMOVE as we do not want to delete
	// tournaments when we
	// delete a category. We will also not use cascade type PERSIST, because that
	// will cause an
	// error if we try to add a tournament with nested category values.
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "tournament_categories", joinColumns = @JoinColumn(name = "tournament_id"), // FK
																									// of
																									// the
																									// owning
																									// side
			inverseJoinColumns = @JoinColumn(name = "category_id") // FK of inverse side
	)
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

	// TONOTE: Many to many unidirectional relationship should be set up on the
	// referenced/child side.
	// set up many to many relationship
	public void addCategory(Category category) {
		playingCategories.add(category);
	}

	// remove category
	public void removeCategory(Category category) {
		if (playingCategories != null)
			playingCategories.remove(category);
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

	public List<Category> getPlayingCategories() {
		return playingCategories;
	}

	public void setPlayingCategories(List<Category> playingCategories) {
		this.playingCategories = playingCategories;
	}

	@Override
	public String toString() {
		return "Tournament [id=" + id + ", name=" + name + ", location=" + location
				+ ", registrations=" + registrations + ", playingCategories=" + playingCategories
				+ "]";
	}
}
