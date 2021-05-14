package ru.alefilas.dto.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutputDirectoryDto extends AbstractEntityDto {

    private String title;

}
