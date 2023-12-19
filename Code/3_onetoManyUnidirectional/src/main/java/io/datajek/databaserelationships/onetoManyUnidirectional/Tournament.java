package io.datajek.databaserelationships.onetoManyUnidirectional;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

/**
 * One tournament to Many registrations Unidirectional Relationship. Unidirectional one-to-many
 * relationship means that only one side maintains the relationship details. So given a Tournament
 * entity, we can find the Registrations but we cannot find the Tournament details from a
 * Registration entity.
 */
@Entity
public class Tournament {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String location;

	// TONOTE: In the absence of the @JoinColumn annotation, Hibernate creates a join table for the
	// one-to-many relationship containing the primary keys of both the tables.
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// this CascadeType.ALL and orphanRemoval = true deletes the orphaned record in the database
	// @OneToMany(cascade = CascadeType.ALL)
	// this also creates an orphaned record in the database
	// @OneToMany(cascade = CascadeType.REMOVE)
	// this CascadeType.REMOVE creates an orphaned record in the database
	// TONOTE: these options can be tested with the thunderclient collection.
	@JoinColumn(name = "tournament_id")
	private List<Registration> registrations = new ArrayList<>();

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

	// TONOTE: Tournament is the owning side of the relationship. So, we need to set the
	// registration's tournament reference to this tournament object.

	// set up one to many relationship
	public void addRegistration(Registration reg) {
		registrations.add(reg);
	}

	// TONOTE: removeRegistration breaks the association between a Tournament and a Registration
	// object. The Registration object is not deleted from the database thus creating an orphaned.
	// remove registration
	public void removeRegistration(Registration reg) {
		if (registrations != null)
			registrations.remove(reg);
	}

	@Override
	public String toString() {
		return "Tournament [id=" + id + ", name=" + name + ", location=" + location
				+ ", registrations=" + registrations + "]";
	}
}
