package com.mavs.relationshipservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipDto {

    private String personEmail;
    private String personRelationshipEmail;
}
