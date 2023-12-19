package io.datajek.databaserelationships.ManytoManyUnidirectional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// TONOTE:
	// IMPORTANT: The many side of a many-to-one bidirectional relationship is ALWAYS the
	// owning side of the relationship.Here, there is a many-to-one relationship between the
	// Registration and Player classes where many registrations can map to one player.
	// TONOTE: If a Registration object is deleted, the associated Player should not be deleted.
	// This means that the delete operation should not be cascaded. Since we have fine grain control
	// over the cascade types, we will list all of them except for REMOVE.
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH}) // , fetch=FetchType.LAZY
	@JoinColumn(name = "player_id", referencedColumnName = "id")
	private Player player;

	public Registration() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "Registration [id=" + id + ", player=" + player + "]";
	}
}
