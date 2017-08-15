/**
 * Declarations in this file apply to all classes in the 
 * nz.ac.auckland.concert.domain package.
 * 
 * The GenericGenerator will apply to any @Entity class that includes an 
 * identity field annotated with @ID and @GeneratedValue. The effect of 
 * GenericGenerator is to cause the database to generate a new primary key
 * value immediately after calling persist() on an EntityManager. E.g.
 * 
 * EntityManager mgr;
 * Concert concert;
 * em.getTransaction().begin()
 * em.persist(concert);
 * // At this point, concert's primary key value has been generated and can be 
 * // accessed:
 * assertNotNull(concert.getId());
 * em.getTransaction().commit();
 * 
 * Without use of the GenericGenerator, concert's primary key value wouldn't 
 * be generated by the database until the commit() call - and calling getId() 
 * immediately after persist() would return null. Hence, by using the 
 * GenericGenerator, the primary key value is available immediately after the 
 * persist() call.  
 * 
 * 
 * The XmlJavaTypeAdapter applies to all fields of type javax.LocalDateTime in
 * classes in package nz.ac.auckland.concert.domain. Whenever fields of this
 * type need to be marshalled, they will be handled by the Adapter class
 * nz.ac.auckland.concert.jaxb.LocalDateTimeAdapter to convert the 
 * LocalDateTime object to an XML string. During unmarshalling, the converter
 * will convert the XML string value back to a LocalDateTime object. The 
 * converter is necessary because JAXB predates the Java 8 date/time classes
 * and doesn't know how to marshall/unmarshall them.
 */

@org.hibernate.annotations.GenericGenerator(
    name = "ID_GENERATOR",
    strategy = "enhanced-sequence"
)


 @XmlJavaTypeAdapter(type=LocalDateTime.class,
        value=LocalDateTimeAdapter.class)

package nz.ac.auckland.concert.domain;
 
import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import nz.ac.auckland.concert.jaxb.LocalDateTimeAdapter;
