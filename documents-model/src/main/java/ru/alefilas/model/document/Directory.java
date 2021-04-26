package ru.alefilas.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Directory extends AbstractEntity {

    private String title;


    @Override
    @JsonIgnore
    public boolean isDocument() {
        return false;
    }
}
