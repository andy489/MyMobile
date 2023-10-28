package com.soft.mobilele.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "activation_tokens")
@Getter
@Setter
@Accessors(chain = true)
public class UserActivationLinkEntity extends GenericEntity {


    @OneToOne
    private UserEntity user;

    private String activationToken;


}
