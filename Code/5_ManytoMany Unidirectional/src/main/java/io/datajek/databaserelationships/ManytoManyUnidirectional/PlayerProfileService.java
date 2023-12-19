package io.datajek.databaserelationships.ManytoManyUnidirectional;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerProfileService {

	@Autowired
	PlayerProfileRepository repo;

	public List<PlayerProfile> allPlayerProfiles() {
		return repo.findAll();
	}

	public PlayerProfile getPlayerProfile(int id) {
		return repo.findById(id).get();
	}

	public PlayerProfile addPlayerProfile(PlayerProfile profile) {
		profile.setId(0);
		// check if profile contains nested player
		if (profile.getPlayer() != null) {
			profile.getPlayer().setPlayerProfile(profile);
		}
		return repo.save(profile);
	}

	// TONOTE: important to avoid the player being deleted when we delete the player profile
	// The following code removes the link between the PlayerProfile and Player object by manually
	// setting the references to null before deleting from the database.
	public void deletePlayerProfile(int id) {
		PlayerProfile tempPlayerProfile = repo.findById(id).get();
		tempPlayerProfile.getPlayer().setPlayerProfile(null);
		tempPlayerProfile.setPlayer(null);
		repo.save(tempPlayerProfile);
		repo.delete(tempPlayerProfile);
	}
}

