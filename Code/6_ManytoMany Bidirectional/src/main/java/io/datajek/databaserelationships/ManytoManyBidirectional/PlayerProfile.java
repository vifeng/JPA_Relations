package io.datajek.databaserelationships.ManytoManyBidirectional;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PlayerProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String twitter;

	// TONOTE: JSON Infinite Recursion
	// Solution 1: @JsonManagedReference and @JsonBackReference
	// To solve this issue, we can use the @JsonManagedReference and @JsonBackReference annotations
	// in the classes. As a result, only the owning side of the relationship is serialized and the
	// inverse side is not serialized.
	// The @JsonManagedReference annotation is used on the playerProfile field in the owning side
	// (Player class). On the inverse side (PlayerProfile class), the @JsonBackReference annotation
	// is used to the player field. These annotations solve the infinite recursion problem.
	// Solution 2: @JsonIdentityInfo
	// Another solution is to use the @JsonIdentityInfo annotation at class level. Both Player and
	// PlayerProfile classes are annotated with @JsonIdentityInfo to avoid infinite recursion while
	// converting POJOs to String. @JsonIdentityInfo(generator=
	// ObjectIdGenerators.PropertyGenerator.class, property="id"). The property attribute specifies
	// the property name of the target reference. Here, id field is used to break out of the
	// recursion. The first time id is encountered, it is replaced with the object and for
	// subsequent occurrences of id, the numerical value is used instead of replacing it with the
	// object.



	// TONOTE: mappedBy is an optional attribute of the @oneToOne annotation which specifies the
	// name of the field which owns the relationship. In our case, it is the playerProfile field in
	// the Player class. The mappedBy attribute is placed on the inverse side on the relationship
	// only. The owning side cannot have this attribute.
	@OneToOne(mappedBy = "playerProfile", cascade = CascadeType.ALL)
	// @JsonBackReference
	private Player player;

	public PlayerProfile() {}

	public PlayerProfile(String twitter) {
		super();
		this.twitter = twitter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "PlayerDetail [id=" + id + ", twitter=" + twitter + ", player=" + player + "]";
	}
}
