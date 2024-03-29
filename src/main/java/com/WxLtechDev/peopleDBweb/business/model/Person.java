package com.WxLtechDev.peopleDBweb.business.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    @Id
    @GeneratedValue(generator = "optimized-sequence")
    @SequenceGenerator(name = "optimized-sequence", sequenceName = "your_entity_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "your_entity_seq")
//    @SequenceGenerator(name = "your_entity_seq", sequenceName = "your_entity_seq", allocationSize = 1)
    private Long       id;

    @NotEmpty(message = "First name can not be empty!")
    private String     firstName;

    @NotEmpty(message = "Last name can not be empty!")
    private String     lastName;

    @Past(message = "Date of birth must be in the past!")
    @NotNull(message = "Date of birth must be specified")
    private LocalDate  dob;

    @Email(message = "Email must be valid!")
    @NotEmpty(message = "Email must be not empty")
    private String     email;

    @DecimalMin(value="1000.00", message = "Salary must be greater than 1000.00")
    @NotNull(message = "Salary must be not null")
    private BigDecimal salary;

    private String photoFileName;

    public static Person parse(final String csvLine) {
        String[] fields = csvLine.split(",\\s*");
        LocalDate dob = LocalDate.parse(fields[10], DateTimeFormatter.ofPattern("M/d/yyyy"));
        return new Person(null, fields[2], fields[4], dob, fields[6], new BigDecimal(fields[25]), null);
    }
}
