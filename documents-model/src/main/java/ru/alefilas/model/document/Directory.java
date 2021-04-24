package ru.alefilas.model.document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Directory extends AbstractEntity {

    private String title;


    @Override
    public boolean isDocument() {
        return false;
    }
}
