package com.WxLtechDev.peopleDBweb.business.service;

import com.WxLtechDev.peopleDBweb.business.model.Person;
import com.WxLtechDev.peopleDBweb.data.FileStorageRepository;
import com.WxLtechDev.peopleDBweb.data.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipInputStream;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final FileStorageRepository fileStorageRepository;

    public PersonService(final PersonRepository personRepository, final FileStorageRepository fileStorageRepository) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    // Delegate Method
    public Person save(final Person person, final InputStream photoStream) {
        fileStorageRepository.save(person.getPhotoFileName(), photoStream);
        Person savedPerson = personRepository.save(person);
        return savedPerson;
    }

    // Delegate Method
    public Optional<Person> findById(final Long aLong) {
        return personRepository.findById(aLong);
    }

    // Delegate Method from PersonRepository
    public Page<Person> findAll(final Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    // Delegate Method
    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    // Delegate Method
    public void deleteAllById(final Iterable<Long> ids) {
//        Iterable<Person> peopleToDelete = personRepository.findAllById(ids);
//        Stream<Person> personStream = StreamSupport.stream(peopleToDelete.spliterator(), false);
//        List<String> fileNames = personStream.map(Person::getPhotoFileName)
//                .collect(Collectors.toList());

        // Use sql to extract the data.
        List<String> fileNames = personRepository.findFileNamesByIds(ids);
        personRepository.deleteAllById(ids);
        fileStorageRepository.deleteAllByName(fileNames);
    }

    public void importCSV(final InputStream csvFileStream) {
        // Read a zip file - Binary format stream
        ZipInputStream zipInputStream = new ZipInputStream(csvFileStream);
        try {
            zipInputStream.getNextEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Convert the binary format stream to character format stream
        InputStreamReader inputStreamReader = new InputStreamReader(zipInputStream);
        // Map the character format stream to line data stream.
        BufferedReader bufferedInputStream = new BufferedReader(inputStreamReader);
        bufferedInputStream.lines()
                .skip(1)
                .limit(25)
                .map(Person::parse)
                .forEach(personRepository::save);
    }
}
