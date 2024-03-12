package com.mymobile.model.entity;

import com.mymobile.model.enumerated.CategoryEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "models")
@Getter
@Setter
@Accessors(chain = true)
public class ModelEntity extends GenericEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer startYear;

    private Integer endYear;

    @ManyToOne
    private BrandEntity brand;

    @Override
    public String toString() {
        return "ModelEntity{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", brand=" + (brand != null ? brand.getName() : null) +
                '}';
    }

    public ModelEntity setId(Long id) {
        super.setId(id);

        return this;
    }
}
