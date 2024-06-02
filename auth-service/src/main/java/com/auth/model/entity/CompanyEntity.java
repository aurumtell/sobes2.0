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
@Table(name = "companies", schema = "public")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "company")
    private String company;

    @ManyToMany(mappedBy = "companies", fetch= FetchType.EAGER)
    @JsonIgnore
    @JsonBackReference
    private Set<UserEntity> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity companyEntity = (CompanyEntity) o;
//        System.out.println("equals");
//        System.out.println(user.getLogin());
//        System.out.println(this.getLogin());
//        System.out.println(user);
//        System.out.println(this);
        return Objects.equals(company, companyEntity.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company);
    }
}