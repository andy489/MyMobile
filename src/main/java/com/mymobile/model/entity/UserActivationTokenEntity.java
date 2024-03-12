package com.mymobile.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Table(name = "activation_tokens")
@Getter
@Setter
@Accessors(chain = true)
public class UserActivationTokenEntity extends GenericEntity {


    @ManyToOne
    private UserEntity user;

    private String activationToken;

    private Instant created;

}
