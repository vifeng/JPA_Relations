package io.datajek.databaserelationships.ManytoManyBidirectional;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

// IMPORTANT:For a many-to-many Bidirectional relationship,we can choose any side to be the
// owner.The relationship is configured in the owner side using the @JoinTable annotation. On the
// target side we use the mappedBy attribute to specify the name of the field that maps the
// relationship in the owning side. From the database design point of view, there is no owner of a
// many-to-many relationship. It would not make any difference to the table structure if we swap the
// @JoinTable and mappedBy.

/**
 * many-to-many Bidirectional
 * 
 * Constraints : 1- The first one is that one playing category should appear only once in the
 * category table (we don’t want multiple entries for the same category). 2- The second constraint
 * is that when a tournament entry is deleted, the associated playing categories should not be
 * deleted and vice versa.
 */
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// TONOTE: Since we do not want the same category name to appear more than once, we will impose
	// the unique key constraint using the unique attribute of the @Column annotation.
	// @Column(unique=true) ensures that the same category name is not entered more than once.
	@Column(unique = true)
	private String name;

	// TONOTE: We will also use the cascade property to cascade all operations except REMOVE because
	// we do not want to delete all associated tournaments, if a category gets deleted.
	// TONOTE: @JsonIgnoreProperties("playingCategories") is used to avoid the infinite recursion
	@ManyToMany(mappedBy = "playingCategories",
			cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
					CascadeType.REFRESH},
			fetch = FetchType.LAZY)
	@JsonIgnoreProperties("playingCategories")
	private List<Tournament> tournaments = new ArrayList<>();


	public Category() {}

	public Category(int id, String name) {
		setId(id);
		setName(name);
	}

	public Category(String name) {
		setName(name);
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

	// set up many to many relationship - child side
	public List<Tournament> getTournaments() {
		return tournaments;
	}

	public void setTournaments(List<Tournament> tournaments) {
		this.tournaments = tournaments;
	}
	// end of many to many relationship

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}
}
