package ru.alefilas.model.permit;

import lombok.Data;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.user.User;

import javax.persistence.*;

@Entity
@Table(name = "permit")
@Data
public class Permit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "permit_type")
    private PermitType permitType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "directory_id")
    private Directory directory;
}
