package ru.alefilas.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutputDirectoryDto extends AbstractEntityDto {

    private String title;

}
