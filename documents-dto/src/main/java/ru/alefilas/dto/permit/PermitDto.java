package ru.alefilas.dto.permit;

import lombok.Data;
import ru.alefilas.model.permit.PermitType;

@Data
public class PermitDto {

    private String username;
    private Long directoryId;
    private PermitType permitType;

}
