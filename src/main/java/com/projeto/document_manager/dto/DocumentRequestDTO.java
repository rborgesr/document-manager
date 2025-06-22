package com.projeto.document_manager.dto;

public class DocumentRequestDTO {

    private String code;
    private String title;
    private Integer revision;
    private Integer type;
    private Integer status;
    private String discipline;
    private Long projectId;  // ALTERADO: agora usa projectId

    // Getters e Setters

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

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}
