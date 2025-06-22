package com.projeto.document_manager.dto;

import com.projeto.document_manager.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

@Schema(description = "")
public class ProjectResponseDTO {

    private Long id;

    private String name;

    private Date startDate;

    private Date endDate;

    private List<String> disciplines;

    public ProjectResponseDTO() {}

    public ProjectResponseDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.disciplines = project.getDisciplines();
    }

    public static ProjectResponseDTO fromEntity(Project project) {
        return new ProjectResponseDTO(project);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public List<String> getDisciplines() { return disciplines; }
    public void setDisciplines(List<String> disciplines) { this.disciplines = disciplines; }
}
