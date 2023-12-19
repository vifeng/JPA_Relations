package io.datajek.databaserelationships.onetooneUnidirectionnel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	// TONOTE: It is a unidirectional relationship because we have the reference of the
	// PlayerProfile entity
	// in the Player entity but we don’t have any reference of the Player entity in the
	// PlayerProfile entity. We can retrive the PlayerProfile object using the Player object but not
	// the other way round.
	// TONOTE: The absence of the cascade property, results in the TransientPropertyValueException
	// exception when Hibernate tries to save a Player object containing a nested PlayerProfile
	// object.
	@OneToOne(cascade = CascadeType.ALL) // , optional = false)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private PlayerProfile playerProfile;

	public Player() {

	}

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

	// TONOTE: In the Unidirectional One-to-One relationship, the Player entity is the owner of the
	// relationship. We can access the PlayerProfile object from the Player object but not the other
	// way round.
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
