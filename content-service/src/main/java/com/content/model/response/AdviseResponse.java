package com.content.model.response;


import com.content.model.entity.AdviseEntity;
import com.content.model.entity.ArticleEntity;
import com.content.model.entity.CompanyEntity;
import com.content.model.entity.ProfessionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdviseResponse {

    private Long id;
    private String profession;
    private String company;
    private String name;
    private String level;
    private String text;


    public AdviseResponse(AdviseEntity advise) {
        this.id = advise.getId();
        this.profession = advise.getProfession().getProfession();
        this.company = advise.getCompany().getCompany();
        this.name = advise.getName();
        this.level = advise.getLevel();
        this.text = advise.getText();
    }

    public List<AdviseResponse> getListAdviseResponces(List<AdviseEntity> adviseEntities) {
        List<AdviseResponse> adviseResponses = new ArrayList<>();
        for (AdviseEntity adviseEntity: adviseEntities) {
            adviseResponses.add(new AdviseResponse(adviseEntity));
        }
        return adviseResponses;
    }
}