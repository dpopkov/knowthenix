package io.dpopkov.knowthenix.services.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IdChangeSetDto {
    private List<Long> add = new ArrayList<>();
    private List<Long> remove = new ArrayList<>();
}
