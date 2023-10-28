package com.soft.mobilele.model.entity;

import com.soft.mobilele.model.enumarated.EngineEnum;
import com.soft.mobilele.model.enumarated.TransmissionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Getter
@Setter
@Accessors(chain = true)
public class OfferEntity {

    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EngineEnum engine;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransmissionEnum transmission;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime modified;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private Integer mileage;

    private Integer year;

    // Additional

    private Boolean awd = false;

    private Boolean climatic = false;

    private Boolean parktronic = false;

    private Boolean navi = false;

    private Boolean leasing = false;

    private Boolean xenonLights = false;

    private Boolean isofixSystem = false;

    private Boolean antiBlockingSystem = false;

    private Boolean cabriolet = false;

    private Boolean bluetooth = false;

    @ManyToOne
    private ModelEntity model;

    @ManyToOne
    private UserEntity seller;

    @Override
    public String toString() {
        return "OfferEntity{" +
                "price=" + price +
                ", description='" + description + '\'' +
                ", engine=" + engine +
                ", transmission=" + transmission +
                ", imageUrl='" + imageUrl + '\'' +
                ", mileage=" + mileage +
                ", year=" + year +
                ", model=" + model +
                ", seller=" + seller +
                '}';
    }
}
