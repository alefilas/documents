package ru.alefilas.model.document;

import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class AbstractEntity {

    protected Long id;
    protected LocalDate creationDate;
    protected Directory parentDirectory;

    public abstract boolean isDocument();

}
