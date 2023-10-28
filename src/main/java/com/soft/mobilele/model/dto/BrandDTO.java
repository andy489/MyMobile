package com.soft.mobilele.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class BrandDTO {

    private String name;

    private List<ModelDTO> models = new ArrayList<>();

    public BrandDTO addModel(ModelDTO modelDto) {
        if (this.models == null) {
            this.models = new ArrayList<>();
        }

        this.models.add(modelDto);

        return this;
    }

}

