package com.WxLtechDev.peopleDBweb.data;

import com.WxLtechDev.peopleDBweb.business.model.Person;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//@Component
public class PersonDataLoader implements ApplicationRunner {
    private PersonRepository personRepository;

    public PersonDataLoader(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        if (personRepository.count() == 0) {
            List<Person> people = List.of(
                    new Person(null,
                            "Perry",
                            "Pang",
                            LocalDate.of(1985, 6, 30),
                            "perry@163.com",
                            new BigDecimal("50000"),
                            ""),
                    new Person(null,
                            "Nicole",
                            "Wang",
                            LocalDate.of(2011, 1, 1),
                            "nicole@gmail.com",
                            new BigDecimal("60000"),
                            ""),
                    new Person(null,
                            "Wilson",
                            "Wang",
                            LocalDate.of(1955, 2, 2),
                            "wilson@126.com",
                            new BigDecimal("40000"),
                            ""),
                    new Person(null,
                            "Kerr",
                            "Renfrew",
                            LocalDate.of(1975, 3, 4),
                            "kerr@gmail.com",
                            new BigDecimal("70000"),
                            ""));
            personRepository.saveAll(people);
        }
    }
}
