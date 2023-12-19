package io.datajek.databaserelationships.onetooneBidirectionnel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	// TONOTE: If the value of the optional attribute is set to false, then we will get an error
	// when a Player object is added without an associated PlayerProfile object throwing a
	// DataIntegrityViolationException error.
	// TONOTE: WORTH SEEING LESSON for cascadeType with bidirectional relationship.
	@OneToOne(cascade = CascadeType.ALL) // , optional = false)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	// @JsonManagedReference
	private PlayerProfile playerProfile;

	public Player() {}

	public Player(String name) {
		super();
		this.name = name;
	}

	public Player(String name, PlayerProfile profile) {
		super();
		this.name = name;
		this.playerProfile = profile;
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

	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	public void setPlayerProfile(PlayerProfile playerProfile) {
		this.playerProfile = playerProfile;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", playerProfile=" + playerProfile + "]";
	}

}
