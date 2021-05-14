package ru.alefilas.service;

import ru.alefilas.dto.permit.PermitDto;

import java.util.List;

public interface PermitService {

    PermitDto save(PermitDto dto);

    void delete(Long id);

    List<PermitDto> getAllPermits();

}
