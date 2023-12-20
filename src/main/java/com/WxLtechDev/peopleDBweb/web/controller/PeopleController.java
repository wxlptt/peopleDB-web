package com.WxLtechDev.peopleDBweb.web.controller;

import com.WxLtechDev.peopleDBweb.business.model.Person;
import com.WxLtechDev.peopleDBweb.business.service.PersonService;
import com.WxLtechDev.peopleDBweb.data.FileStorageRepository;
import com.WxLtechDev.peopleDBweb.data.PersonRepository;
import com.WxLtechDev.peopleDBweb.exception.StorageException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {

    public static final String DISPOSITION = """
             attachment; filename="%s"
            """;
    private PersonRepository personRepository;
    private FileStorageRepository fileStorageRepository;
    private PersonService personService;

    public PeopleController(final PersonRepository personRepository,
                            final FileStorageRepository fileStorageRepository,
                            final PersonService personService) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.personService = personService;
    }

    @ModelAttribute("people")
    public Page<Person> getPeople(@PageableDefault(size = 5) Pageable page ){
        return personService.findAll(page);
    }

    // If we do not add an attribute name after @ModelAttribute
    // It takes the type(Person) as its name("person").
    @ModelAttribute("person")
    public Person getPerson(){
        Person person = new Person();
        person.setFirstName("First Name");
        person.setLastName("Last Name");
        person.setDob(LocalDate.of(2000,01,01));
        person.setSalary(new BigDecimal(10000.00));
        person.setEmail("wxlptt@gmail.com");
        return person;
//        return new Person();
    }

    @GetMapping
    public String showPeoplePage(Model model){
        return "people";
    }

    @GetMapping("/images/{resource}")
    public ResponseEntity<Resource> getResource(@PathVariable String resource){

        // To build a response with a status code of 200, ok.
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(DISPOSITION, resource))
                .body(fileStorageRepository.findByName(resource));

    }

    @ExceptionHandler(StorageException.class)
    public String handleException(){
        return null;
    }

    @PostMapping
    public String savePerson(Model model, @Valid Person person, Errors errors, @RequestParam("photoFileName") MultipartFile photoFile) throws IOException {
        log.info(person);
        log.info(photoFile.getOriginalFilename());
        log.info(photoFile.getSize());
        log.info("Errors" + errors);
        if (!errors.hasErrors()) {
            try {
                personService.save(person, photoFile.getInputStream());
                return "redirect:people";
            } catch (StorageException e) {
                model.addAttribute("errorMsg", "System is currently unable to accept photo file this time.");
                return "people";
            }
        }
        return "people";
    }

    /**
     * If the website does not click any checkbox, it will crash.
     * So here uses Optional as a parameter.
     */
    @PostMapping(params = "action=delete")
    public String deletePerson(@RequestParam Optional<List<Long>> selections){
        log.info(selections);
        if (selections.isPresent()){
            personService.deleteAllById(selections.get());
        }
//        selections.orElseGet( () -> List.of() ).stream().parallel()
//                .forEach(personRepository::deleteById);
        return "redirect:people";
    }

    @PostMapping(params = "action=edit")
    public String editPerson(@RequestParam Optional<List<Long>> selections, Model model){
        log.info(selections);
        if (selections.isPresent()){
            Optional<Person> person = personRepository.findById(selections.get().get(0));
            model.addAttribute("person", person);
        }
        return "people";
    }

    @PostMapping(params = "action=import")
    public String importCSV(@RequestParam() MultipartFile csvFile){
        log.info("File name : ".concat(csvFile.getOriginalFilename()));
        log.info("File size : " + csvFile.getSize());
        try {
            personService.importCSV(csvFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:people";
    }
}
