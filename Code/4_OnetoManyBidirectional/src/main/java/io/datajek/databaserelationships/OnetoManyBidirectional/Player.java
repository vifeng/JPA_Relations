package io.datajek.databaserelationships.OnetoManyBidirectional;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private PlayerProfile playerProfile;

	// TONOTE: Since the many side (Registration) is the owning side of a bidirectional
	// relationship, we will use the mappedBy attribute here (in the Player class) to specify that
	// this is the inverse side of the relationship.
	// TONOTE: We are using cascade type ALL here because we
	// want a player’s registrations to be deleted when the player record is deleted.
	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
	private List<Registration> registrations = new ArrayList<>();

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

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	// TONOTE: a method to the Player class that sets the bidirectional relationship. In this
	// method, we will add a Registration object to the Player and also update the Registration to
	// reflect that it belongs to this Player.
	// set up bi-directional relationship with Registration class
	public void registerPlayer(Registration reg) {
		// add registration to the list
		registrations.add(reg);
		// set the player field in the registration
		reg.setPlayer(this);
	}

	public void removeRegistration(Registration reg) {
		if (registrations != null)
			registrations.remove(reg);
		// set the player field in the registration
		reg.setPlayer(null);
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", playerProfile=" + playerProfile
				+ ", registrations=" + registrations + "]";
	}

}
