package com.mymobile.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class BrandDto {

    private String name;

    private List<ModelDto> models = new ArrayList<>();

    public BrandDto addModel(ModelDto modelDto) {
        if (this.models == null) {
            this.models = new ArrayList<>();
        }

        this.models.add(modelDto);

        return this;
    }

}

