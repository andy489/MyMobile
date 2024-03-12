package com.mymobile.model.entity;

import com.mymobile.model.enumerated.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@Accessors(chain = true)
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRoleEnum;

    @Override
    public String toString() {
        return "UserRoleEntity{" +
                "id=" + id +
                ", userRoleEnum=" + userRoleEnum +
                '}';
    }
}
