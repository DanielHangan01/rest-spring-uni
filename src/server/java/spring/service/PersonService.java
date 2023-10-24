package spring.service;

import rest.model.Person;
import rest.util.PersonSortingOptions;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService {
  	// do not change this
    private final List<Person> persons;

    public PersonService() {
        this.persons = new ArrayList<>();
    }

    public Person savePerson(Person person) {
        var optionalPerson = persons.stream().filter(existingPerson -> existingPerson.getId().equals(person.getId())).findFirst();
        if (optionalPerson.isEmpty()) {
            person.setId(UUID.randomUUID());
            persons.add(person);
            return person;
        } else {
            var existingPerson = optionalPerson.get();
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setBirthday(person.getBirthday());
            return existingPerson;
        }
    }

    public void deletePerson(UUID personId) {
        this.persons.removeIf(person -> person.getId().equals(personId));
    }

    public List<Person> getAllPersons(PersonSortingOptions sortingOptions) {
        // TODO Part 3: Add sorting here
        if (sortingOptions == null) {
            return new ArrayList<>(this.persons);
        }
        List<Person> sortedList = new ArrayList<>(this.persons);
        sortedList.sort((p1, p2) -> {
            Person person1;
            Person person2;
            if (sortingOptions.getSortingOrder() == PersonSortingOptions.SortingOrder.ASCENDING) {
                person1 = p1;
                person2 = p2;
            } else {
                person1 = p2;
                person2 = p1;
            }
            return switch(sortingOptions.getSortField()) {
                case ID -> person1.getId().compareTo(person2.getId());
                case FIRST_NAME -> person1.getFirstName().compareTo(person2.getFirstName());
                case LAST_NAME -> person1.getLastName().compareTo(person2.getLastName());
                case BIRTHDAY -> person1.getBirthday().compareTo(person2.getBirthday());
            };
        });
        return sortedList;
    }
}
