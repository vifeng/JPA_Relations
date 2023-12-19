package io.datajek.databaserelationships.ManytoManyUnidirectional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// IMPORTANT: For a many-to-many relationship, any side can become the parent/owner. It depends on
// how the business rules are defined. Let’s say, we cannot have a tournament without a category
// attached to it but we can have a category that is not associated with any tournament. Given this
// scenario, a category can exist on its own but a tournament needs to have one or more categories
// associated with it. So, Category becomes the owning/parent side and Tournament becomes the
// referenced/child side.
// tournaments 0,* -> 1,* categories
// IMPORTANT: In a unidirectional many-to-many relationship, we put the relationship on the
// referenced/child side. For a unidirectional relationship, the Category class does not need any
// information about the tournaments. But the Tournament class needs to know about the categories
// So, in the Tournament class we have to put a link to the Category class. Since a tournament
// can have more than one categories, we will create a List of categories.

/**
 * many-to-many unidirectional
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

	public Category() {}

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

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}
}
