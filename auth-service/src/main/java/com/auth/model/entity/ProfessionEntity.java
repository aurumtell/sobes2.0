package com.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "professions", schema = "public")
public class ProfessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "profession")
    private String profession;

    @ManyToMany(mappedBy = "professions", fetch= FetchType.EAGER)
    @JsonBackReference
    @JsonIgnore
    private Set<UserEntity> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfessionEntity professionEntity = (ProfessionEntity) o;
//        System.out.println("equals");
//        System.out.println(user.getLogin());
//        System.out.println(this.getLogin());
//        System.out.println(user);
//        System.out.println(this);
        return Objects.equals(profession, professionEntity.profession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profession);
    }
}