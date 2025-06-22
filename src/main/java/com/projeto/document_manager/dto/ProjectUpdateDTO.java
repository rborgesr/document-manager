package com.projeto.document_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

@Schema(description = "DTO para criação ou atualização de projeto")
public class ProjectUpdateDTO {

    @Schema(description = "Nome do projeto", example = "Projeto X", required = true)
    private String name;

    @Schema(description = "Data de início do projeto", example = "2025-06-01")
    private Date startDate;

    @Schema(description = "Data de término do projeto", example = "2025-07-31")
    private Date endDate;

    @Schema(description = "Lista de disciplinas do projeto", example = "[\"Elétrica\", \"Mecânica\"]")
    private List<String> disciplines;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public List<String> getDisciplines() { return disciplines; }
    public void setDisciplines(List<String> disciplines) { this.disciplines = disciplines; }
}
