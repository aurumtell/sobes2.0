package com.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "roles", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "role_user")
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy="role", fetch= FetchType.EAGER)
    private Set<UserEntity> users;

    public RoleEntity(Long id, @NotNull String role) {
        this.id = id;
        this.role = role;
    }

}
