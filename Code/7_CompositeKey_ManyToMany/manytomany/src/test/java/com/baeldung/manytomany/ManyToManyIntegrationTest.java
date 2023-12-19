
package com.baeldung.manytomany;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.baeldung.manytomany.baldungwholeCode.Course;
import com.baeldung.manytomany.baldungwholeCode.CourseRating;
import com.baeldung.manytomany.baldungwholeCode.CourseRatingKey;
import com.baeldung.manytomany.baldungwholeCode.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest(classes = ManyToManyTestConfiguration.class)
@ContextConfiguration(classes = ManyToManyTestConfiguration.class)
@DirtiesContext
@Transactional
public class ManyToManyIntegrationTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void contextStarted() {}

    @Test
    public void whenCourseRatingPersisted_thenCorrect() {
        Student student = new com.baeldung.manytomany.baldungwholeCode.Student(101L);
        entityManager.persist(student);

        Course course = new Course(201L);
        entityManager.persist(course);

        CourseRating courseRating = new CourseRating();
        courseRating.setId(new CourseRatingKey());
        courseRating.setStudent(student);
        courseRating.setCourse(course);
        courseRating.setRating(100);
        entityManager.persist(courseRating);

        CourseRating persistedCourseRating =
                entityManager.find(CourseRating.class, new CourseRatingKey(101L, 201L));

        assertThat(persistedCourseRating, notNullValue());
        assertThat(persistedCourseRating.getStudent().getId(), is(101L));
        assertThat(persistedCourseRating.getCourse().getId(), is(201L));
    }
}
