SUMMARY

- [JPA Relationship](#jpa-relationship)
  - [Running project](#running-project)
    - [Thunder / Postman](#thunder--postman)
    - [run a project](#run-a-project)
  - [One-to-One Unidirectional Relationship](#one-to-one-unidirectional-relationship)
    - [cascade option :](#cascade-option-)
    - [@JoinColumn](#joincolumn)
    - [Player is the owner, thus it is were do the mapping](#player-is-the-owner-thus-it-is-were-do-the-mapping)
  - [One-to-One Bidirectional Relationship](#one-to-one-bidirectional-relationship)
    - [Player](#player)
      - [optional = false](#optional--false)
    - [PlayerProfile we add the bidirectional mapping](#playerprofile-we-add-the-bidirectional-mapping)
      - [infinit loop](#infinit-loop)
  - [One-to-Many Unidirectional Relationship](#one-to-many-unidirectional-relationship)
    - [@JoinColumn](#joincolumn-1)
    - [CascadeType.ALL](#cascadetypeall)
    - [orphanRemoval=true](#orphanremovaltrue)
  - [One-to-Many Bidirectional Relationship](#one-to-many-bidirectional-relationship)
    - [the Many to one side : Registrations](#the-many-to-one-side--registrations)
      - [@ManyToOne](#manytoone)
        - [Cascade type](#cascade-type)
    - [The One to many side : player](#the-one-to-many-side--player)
      - [@OneToMany](#onetomany)
  - [Many-to-Many Unidirectional Relationship](#many-to-many-unidirectional-relationship)
    - [Owning/parent side : Categories](#owningparent-side--categories)
      - [@Column(unique=true)](#columnuniquetrue)
    - [Referenced/child side : Tournaments](#referencedchild-side--tournaments)
      - [@JoinTable](#jointable)
      - [Cascade type](#cascade-type-1)
  - [Many-to-Many Bidirectional Relationship](#many-to-many-bidirectional-relationship)
    - [Category side : chosen as the child side](#category-side--chosen-as-the-child-side)
      - [CascadeType](#cascadetype)
    - [Tournaments side : chosen as the owner/parent side](#tournaments-side--chosen-as-the-ownerparent-side)
      - [@JsonIgnoreProperties](#jsonignoreproperties)

# JPA Relationship

Please refer to the JPA lessons for details

## Running project

### Thunder / Postman

Herewith is a json file of the collection of all the request used to test the code.
It maybe imported in thunder (vscode extension) or postman.

### run a project

```sh
cd <project>
mvn spring-boot:run
```

## One-to-One Unidirectional Relationship

player 1 -> 1 PlayerProfile

owner -> child

#### cascade option :

If we delete a Player entity, the associated details should also be deleted.
The absence of the cascade property, results in the TransientPropertyValueException exception when Hibernate tries to save a Player object containing a nested PlayerProfile object.

#### @JoinColumn

We use the @JoinColumn annotation on the owning side.
The @JoinColumn annotation specifies the name of the foreign key column in the player table. We will call the column profile_id. In the player_profile table, the column that is being referenced is id.

### Player is the owner, thus it is were do the mapping

Player.java

```java
@Entity
public class Player {
  @Id
  @GeneratedValue(Strategy = GenerationType.IDENTITY)
  private int Id;

  private String name;

  @OneToOne(cascade= CascadeType.ALL)
  @JoinColumn(name="profile_id", referencedColumnName="id")
  private PlayerProfile playerProfile;

  // getter, setter and constructors

}
```

PlayerService.java

```java
// code
public Player assignProfile(int id, PlayerProfile profile) {
	Player player = repo.findById(id).get();
	player.setPlayerProfile(profile);
	return repo.save(player);
}

```

PlayerController.java

```java
@PutMapping("/{id}/profiles/{profile_id}")
public Player assignDetail(@PathVariable int id, @PathVariable int profile_id) {
	PlayerProfile profile = profileService.getPlayerProfile(profile_id);
	return service.assignProfile(id, profile);
}
```

## One-to-One Bidirectional Relationship

player 1 -> 1 PlayerProfile  
no owner

### Player

We keep the same mapping as in unidirectional : no change in PlayerService.java and PlayerController.java
And to avoid an infinit loop we add the @JsonIdentityInfo annotation to the Player class

#### optional = false

@OneToOne(cascade=CascadeType.ALL, optional = false)
The @OneToOne annotation has an optional attribute. By default the value is true meaning that the association can be null.
we set optional = false, to avoid a Player being added without a PlayerProfile. if so, it throws a DataIntegrityViolationException

Player.java

```java
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Player {
  @Id
  @GeneratedValue(Strategy = GenerationType.IDENTITY)
  private int Id;

  private String name;

  @OneToOne(cascade= CascadeType.ALL, optional = false)
  @JoinColumn(name="profile_id", referencedColumnName="id")
  private PlayerProfile playerProfile;

  // getter, setter and constructors

}
```

PlayerService.java

```java
// code
public Player assignProfile(int id, PlayerProfile profile) {
	Player player = repo.findById(id).get();
	player.setPlayerProfile(profile);
	return repo.save(player);
}

```

PlayerController.java

```java
@PutMapping("/{id}/profiles/{profile_id}")
public Player assignDetail(@PathVariable int id, @PathVariable int profile_id) {
	PlayerProfile profile = profileService.getPlayerProfile(profile_id);
	return service.assignProfile(id, profile);
}
```

### PlayerProfile we add the bidirectional mapping

PlayerProfile.java

```java
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PlayerProfile {
  @Id
  @GeneratedValue(GenertionType.IDENTITY)
  private int id;

  private String twitter;

  @OneToOne(mappedBy="playerProfile", cascade= CascadeType.ALL)
  private Player player;

}
```

PlayerProfileService.java
CascadeType.ALL means that if we delete a PlayerProfile object, the associated Player object will also be deleted so we need to break the association between the two objects before calling delete() on the PlayerProfile object.

```java
// code
public void deletePlayerProfile(int id) {
  PlayerProfile tempPlayerProfile = repo.findById(id).get();
  //set the playerProfile field of the Player object to null
  tempPlayerProfile.getPlayer().setPlayerProfile(null);
  //set the player field of the PlayerProfile object to null
  tempPlayerProfile.setPlayer(null);
  //save changes
  repo.save(tempPlayerProfile);
  //delete the PlayerProfile object
  repo.delete(tempPlayerProfile);
}

```

#### infinit loop

two solutions:

1. At class level for both entities: @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
   (The property attribute specifies the property name of the target reference. Here, id field is used to break out of the recursion. The first time id is encountered, it is replaced with the object and for subsequent occurrences of id, the numerical value is used instead of replacing it with the object.)
2. At field level
   1. for the owner entity (player) : @JsonManagedReference on the field (playerProfile)
   2. for the child entity (PlayerProfile): @JsonBackReference on the field (player)

## One-to-Many Unidirectional Relationship

Tournament 1 -> \* Registrations  
owner -> child

Unidirectional one-to-many relationship means that only one side maintains the relationship details. So given a Tournament entity, we can find the Registrations but we cannot find the Tournament details from a Registration entity.

Tournament.java

```java
public class Tournament {
  //...
  @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
  @JoinColumn(name="tournament_id")
  private List<Registration> registrations = new ArrayList<>();
  //generate getter and setter methods
  //update constructor & toString()
  public void addRegistration(Registration reg) {
    registrations.add(reg);
  }
  //remove registration
  // We can remove a registration from a tournament by breaking the association between the two. In such a case, the record in the registration table would become an orphan as it is no longer linked to any entry in the tournament table.
	public void removeRegistration(Registration reg) {
    	if (registrations != null)
    		registrations.remove(reg);
	}
}
```

TournamentController.java

```java
  @PutMapping("/{id}/registrations/{registration_id}")
  public Tournament addRegistration(@PathVariable int id, @PathVariable int registration_id) {
    Registration registration = registrationService.getRegistration(registration_id);
    return service.addRegistration(id, registration);
  }

  // this method illustrates how we can create an orphan by breaking the association between a tournament and a registration.
  @PutMapping("/{id}/remove_registrations/{registration_id}")
  public Tournament removeRegistration(@PathVariable int id, @PathVariable int registration_id) {
    Registration registration = registrationService.getRegistration(registration_id);
    return service.removeRegistration(id, registration);
  }

  @DeleteMapping("/{id}")
  public void deleteTournament(@PathVariable int id) {
    service.deleteTournament(id);
  }
```

TournamentService.java

```java
public Tournament addRegistration(int id, Registration registration) {
  Tournament tournament = repo.findById(id).get();
  tournament.addRegistration(registration);
  return repo.save(tournament);
}


public Tournament removeRegistration(int id, Registration registration) {
		Tournament tournament = repo.findById(id).get();
		tournament.removeRegistration(registration);
		return repo.save(tournament);
	}
```

#### @JoinColumn

In the absence of the @JoinColumn annotation, Hibernate creates a join table for the one-to-many relationship containing the primary keys of both the tables.

#### CascadeType.ALL

When a tournament is deleted we will delete the associated registrations as well. This can be achieved by choosing CascadeType.ALL.
Cascade type REMOVE only cascades the delete operation to child records which are linked to the parent, not the orphans.

#### orphanRemoval=true

@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
The orphanRemoval attribute triggers a remove operation for the Registration object no longer associated with the Tournament object thereby leaving the database in a consistent state.

## One-to-Many Bidirectional Relationship

Player 1 -> \* Registrations

Real life constraints to the model :

- The first one being that every Registration object must be associated with a Player object.
- Secondly, when we delete a Registration object, the associated Player object should not be deleted.

### the Many to one side : Registrations

Registration.java

```java
@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private int id;
    private Date registrationDate;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH,  CascadeType.REFRESH}
    @JoinColumn(name="player_id", referencedColumnName = "id")
    private Player player;
    //getters and setters
    //constructor
    //toString method
}
```

#### @ManyToOne

The inverse of one-to-many relationship is many-to-one, where many registrations map to one player. The many-to-one relationship is defined using the @ManyToOne annotation. The @JoinColumn annotation is used to specify the foreign key column in the registrations table.

##### Cascade type

@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE,
CascadeType.DETACH, CascadeType.REFRESH}
If a Registration object is deleted, the associated Player should not be deleted. This means that the delete operation should not be cascaded. Since we have fine grain control over the cascade types, we will list all of them except for REMOVE.

### The One to many side : player

Player.java

```java
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
    // code
  @OneToMany(mappedBy="player", cascade= CascadeType.ALL)
  private List<Registration> registrations = new ArrayList<>();
  // getters and setters, constructor, toString method

  //set up bidirectional relationship with Registration class
  public void registerPlayer(Registration reg) {
      //add registration to the list
      registrations.add(reg);
      //set the player field in the registration
      reg.setPlayer(this);
  }
  public void removeRegistration(Registration reg) {
    	if (registrations != null)
    		registrations.remove(reg);
    	//set the player field in the registration
    	reg.setPlayer(null);
    }
}

```

PlayerController.java

```java
@PutMapping("/{id}/registrations/{registration_id}")
public Player assignRegistration(@PathVariable int id, @PathVariable int registration_id) {
 Registration registration = registrationService.getRegistration(registration_id);
 return service.assignRegistration(id, registration);
}
```

PlayerService.java

```java
public Player assignRegistration(int id, Registration registration) {
  Player player = repo.findById(id).get();
  player.registerPlayer(registration);
  return repo.save(player);
}
```

#### @OneToMany

**mappedBy** attribute here (in the Player class) specify that this is the inverse side of the relationship.
We are using **cascade** type ALL here because we want a player’s registrations to be deleted when the player record is deleted.

## Many-to-Many Unidirectional Relationship

Tournaments \* -> \* Categories

**Owning side ?** For a many-to-many Unidirectional relationship, any side can become the parent/ owner. It depends on how the business rules are defined. Let’s say, we cannot have a tournament without a category attached to it but we can have a category that is not associated with any tournament. Given this scenario, a category can exist on its own but a tournament needs to have one or more categories associated with it. So, Category becomes the owning/parent side and Tournament becomes the referenced/child side.  
**In a unidirectional many-to-many relationship, we put the relationship on the child side. Here, in the Tournament class we have to put a link to the Category class.**

In databases, this relationship is modelled using a join table which has the primary keys of both tables in the relationship.

Two real life constraints :

1. The first one is that one playing category should appear only once in the category table (we don’t want multiple entries for the same category).
2. The second constraint is that when a tournament entry is deleted, the associated playing categories should not be deleted and vice versa.

### Owning/parent side : Categories

Category.java

```java
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(unique=true)
    private String name;
    //constructors
    //getters and setters
}

```

#### @Column(unique=true)

@Column(unique=true) ensures that the same category name is not entered more than once.
Since we do not want the same category name to appear more than once, we will impose the unique key constraint using the unique attribute of the @Column annotation.

### Referenced/child side : Tournaments

**In a unidirectional many-to-many relationship, we put the relationship on the child side.**

Tournament.java

```java
@Entity
public class Tournament {
    //...
    @ManyToMany(Cascade = CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH)
    @JoinTable(name="tournament_categories",
        joinColumns=@JoinColumn(name="tournament_id"),   //FK of the owning side
        inverseJoinColumns=@JoinColumn(name="category_id"))  //FK of inverse side
    private List<Category> playingCategories = new ArrayList<>();

    //...
    //set up many-to-many relationship
  public void addCategory(Category category) {
      playingCategories.add(category);
  }
  	//remove category
	public void removeCategory(Category category) {
		if (playingCategories != null)
			playingCategories.remove(category);
	}
}
```

TournamentController.java

```java
@PutMapping("/{id}/categories/{category_id}")
public Tournament addCategory(@PathVariable int id, @PathVariable int category_id) {
    Category category = categoryService.getCategory(category_id);
    return service.addCategory(id, category);
}
```

TournamentService.java

```java
public Tournament addCategory(int id, Category category) {
    Tournament tournament = repo.findById(id).get();
    tournament.addCategory(category);
    return repo.save(tournament);
}
```

#### @JoinTable

**joinColumns** attribute specifies the column(s) in the owner table that becomes a foreign key in the join table.  
**inverseJoinColumns** attribute specifies the foreign key column(s) from the inverse side.

#### Cascade type

We will not use cascade type REMOVE as we do not want to delete tournaments when we delete a category. We will also not use cascade type PERSIST, because that will cause an error if we try to add a tournament with nested category values.

## Many-to-Many Bidirectional Relationship

Tournaments \* <-> \* Categories

**Owner side ?** For a many-to-many Bidirectional relationship, we can choose any side to be the owner.
The relationship is configured in the owner side using the @JoinTable annotation. On the target side we use the mappedBy attribute to specify the name of the field that maps the relationship in the owning side. From the database design point of view, there is no owner of a many-to-many relationship. It would not make any difference to the table structure if we swap the @JoinTable and mappedBy.

**Careful** : In a many-to-many relationship, there is no owner when it comes to the table structure. This is different from a one-to-many relationship where the many side is always the owning side containing the key of the one side.

### Category side : chosen as the child side

Category.java

```java
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy= "playingCategories",
        cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
        fetch=FetchType.LAZY)
    @JsonIgnoreProperties("playingCategories")
    private List<Tournament> tournaments = new ArrayList<>();
    //...
}
```

#### CascadeType

We will also use the cascade property to cascade all operations except REMOVE because we do not want to delete all associated tournaments, if a category gets deleted.

### Tournaments side : chosen as the owner/parent side

unchanged except for the @JsonIgnoreProperties and the add and remove methods

Tournament.java

```java
@Entity
public class Tournament {
    //...
    @ManyToMany(Cascade = CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH)
    @JoinTable(name="tournament_categories",
        joinColumns=@JoinColumn(name="tournament_id"),   //FK of the owning side
        inverseJoinColumns=@JoinColumn(name="category_id"))  //FK of inverse side
    @JsonIgnoreProperties("tournaments")
    private List<Category> playingCategories = new ArrayList<>();

    //...
    //set up many-to-many relationship
  public void addCategory(Category category) {
      playingCategories.add(category);
      //set up bidirectional relationship
      category.getTournaments().add(this);
  }
  	//remove category
	public void removeCategory(Category category) {
		if (playingCategories != null)
			playingCategories.remove(category);
    //update bidirectional relationship
    category.getTournaments().remove(this);
	}
}
```

TournamentController.java

```java
@PutMapping("/{id}/categories/{category_id}")
public Tournament addCategory(@PathVariable int id, @PathVariable int category_id) {
    Category category = categoryService.getCategory(category_id);
    return service.addCategory(id, category);
}
```

TournamentService.java

```java
public Tournament addCategory(int id, Category category) {
    Tournament tournament = repo.findById(id).get();
    tournament.addCategory(category);
    return repo.save(tournament);
}
```

#### @JsonIgnoreProperties

JSON gets into infinite recursion when trying to de-serialize bidirectional relationships. We have seen two ways to solve this issue in the One-to-One Bidirectional Relationship lesson. Here, we will see yet another approach to avoid infinite recursion. We can use the property that we want to ignore with the @JsonIgnoreProperties. This annotation can be used at field level in both the Tournament and Category class.

This will throw a maxContentLength size of Infinity exceeded error.
