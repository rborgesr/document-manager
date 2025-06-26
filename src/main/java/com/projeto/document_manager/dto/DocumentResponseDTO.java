package com.projeto.document_manager.dto;

import com.projeto.document_manager.entity.Document;

public class DocumentResponseDTO {
    private Long id;
    private String code;
    private String title;
    private Integer revision;
    private Integer type;
    private Integer status;
    private String discipline;
    private String projectName;

    public DocumentResponseDTO() {}

    public DocumentResponseDTO(Document document) {
        this.id = document.getId();
        this.code = document.getCode();
        this.title = document.getTitle();
        this.revision = document.getRevision();
        this.type = document.getType();
        this.status = document.getStatus();
        this.discipline = document.getDiscipline();
        this.projectName = document.getProject() != null ? document.getProject().getName() : null;
    }

    public static DocumentResponseDTO fromEntity(Document document) {
        return new DocumentResponseDTO(document);
    }

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getRevision() { return revision; }
    public void setRevision(Integer revision) { this.revision = revision; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getDiscipline() { return discipline; }
    public void setDiscipline(String discipline) { this.discipline = discipline; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }


}


