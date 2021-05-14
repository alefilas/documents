package ru.alefilas.model.document;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "entity")
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "creation_date", nullable = false, updatable = false)
    protected LocalDate creationDate;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH},
    fetch = FetchType.EAGER)
    @JoinColumn(name = "directory_id")
    protected Directory parentDirectory;

    public abstract boolean isDocument();

}
