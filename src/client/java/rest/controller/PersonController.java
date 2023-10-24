package rest.controller;

import rest.model.Person;
import rest.util.PersonSortingOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PersonController {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    private final List<Person> persons = new ArrayList<>();

    public void addPerson(Person person, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an http post request to the server
        webClient.post()
                .uri("persons")
                .bodyValue(person)
                .retrieve()
                .bodyToMono(Person.class)
                .onErrorStop()
                .subscribe(newPerson -> {
                    persons.add(newPerson);
                    personsConsumer.accept(persons);
                });
    }

    public void updatePerson(Person person, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an http put request to the server
        webClient.put()
                .uri("persons/" + person.getId())
                .bodyValue(person)
                .retrieve()
                .bodyToMono(Person.class)
                .onErrorStop()
                .subscribe(newPerson -> {
                    persons.replaceAll(oldPerson -> oldPerson.getId().equals(newPerson.getId()) ? newPerson : oldPerson);
                    personsConsumer.accept(persons);
                });
    }

    public void deletePerson(Person person, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an http delete request to the server
        webClient.delete()
                .uri("persons/" + person.getId())
                .retrieve()
                .toBodilessEntity()
                .onErrorStop()
                .subscribe(v -> {
                    persons.remove(person);
                    personsConsumer.accept(persons);
                });

    }

    public void getAllPersons(PersonSortingOptions sortingOptions, Consumer<List<Person>> personsConsumer) {
        // TODO Part 2: Make an https get request to the server
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("persons")
                        .queryParam("sortField", sortingOptions.getSortField())
                        .queryParam("sortingOrder", sortingOptions.getSortingOrder())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Person>>() {
                })
                .onErrorStop()
                .subscribe(newPersons -> {
                    persons.clear();
                    persons.addAll(newPersons);
                    personsConsumer.accept(persons);
                });
    }
}
