package ru.alefilas.model.document;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Directory extends AbstractEntity {

    private String title;
    private List<AbstractEntity> files;

    public void addFile(AbstractEntity file) {
        files.add(file);
    }

    @Override
    public boolean isDocument() {
        return false;
    }
}
