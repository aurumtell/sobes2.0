package com.auth.model.request;

import com.auth.model.entity.CompanyEntity;
import com.auth.model.entity.ProfessionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private String experience;
    private Set<String> professions;
    private Set<String> companies;

}


