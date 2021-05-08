package ru.alefilas.service.mapper;

import ru.alefilas.dto.EntityDto;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;

import java.time.LocalDate;

public class EntityMapper {

    public static EntityDto fromModelToDto(AbstractEntity entity) {
        EntityDto dto = new EntityDto();

        Long id = entity.getId();
        LocalDate date = entity.getCreationDate();
        String type;
        String title;
        Long directoryId = entity.getParentDirectory().getId();

        if (entity.isDocument()) {
            type = "document";
            title = ((Document) entity).getCurrentVersion().getTitle();
        } else {
            type = "directory";
            title = ((Directory) entity).getTitle();
        }

        dto.setId(id);
        dto.setCreationDate(date);
        dto.setType(type);
        dto.setTitle(title);
        dto.setDirectoryId(directoryId);

        return dto;
    }


}
