package com.content.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "advices", schema = "public")
public class AdviseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, cascade= CascadeType.MERGE)
    @JoinColumn(name="professionid")
    private ProfessionEntity profession;

    @ManyToOne(optional=false, cascade= CascadeType.MERGE)
    @JoinColumn(name="companyid")
    private CompanyEntity company;

    @Column(name = "employeename")
    private String name;

    @Column(name = "employeelevel")
    private String level;

    @Column(name = "text")
    private String text;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdviseEntity)) return false;
        return id != null && id.equals(((AdviseEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}