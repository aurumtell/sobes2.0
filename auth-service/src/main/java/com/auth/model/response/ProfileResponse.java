package com.auth.model.response;

import com.auth.model.entity.CompanyEntity;
import com.auth.model.entity.ProfessionEntity;
import com.auth.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private Long id;
    private String email;
    private String username;
    private String level;
    private List<String> professions;
    private List<String> companies;
    private String role;

//    public static ProfileResponse toProfile(UserEntity user, ) {
//        ModelMapper modelMapper = new ModelMapper();
//        return modelMapper.map(user, ProfileResponse.class);
//    }

    public ProfileResponse(UserEntity user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        if (user.getExperience() == null) {
            this.level = null;
        } else {
            this.level = user.getExperience().getExperience();
        }
        this.professions = user.getProfessions().stream().map(ProfessionEntity::getProfession).collect(Collectors.toList());
        this.companies = user.getCompanies().stream().map(CompanyEntity::getCompany).collect(Collectors.toList());
        this.role = user.getRole().getRole();
    }

    public static List<ProfileResponse> toProfileList(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(ProfileResponse::new)
                .collect(Collectors.toList());
    }
}