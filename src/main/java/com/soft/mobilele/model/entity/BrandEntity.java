package com.soft.mobilele.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands")
@Getter
@Setter
@Accessors(chain = true)
public class BrandEntity extends GenericEntity {

    private String name; // Ford


    @OneToMany(
            mappedBy = "brand",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private List<ModelEntity> models = new ArrayList<>(); // Fiesta


    @Override
    public String toString() {
        return "BrandEntity{" +
                "name='" + name + '\'' +
                ", models=" + models +
                '}';
    }

    public BrandEntity setId(Long id){
        super.setId(id);

        return this;
    }
}

