package ru.alefilas.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "directory")
public class Directory extends AbstractEntity {

    @Column(name = "title", nullable = false)
    private String title;


    @Override
    @JsonIgnore
    public boolean isDocument() {
        return false;
    }
}
