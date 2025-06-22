package com.projeto.document_manager.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guid;
    private String code;
    private String title;
    private Integer revision;
    private Integer type;
    private Integer status;
    private Date createdAt;
    private Boolean deleted;
    private Date deletedAt;

    // Relacionamento ManyToOne com Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // Nova propriedade para disciplina do documento
    private String discipline;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }

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

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public Date getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public String getDiscipline() { return discipline; }
    public void setDiscipline(String discipline) { this.discipline = discipline; }
}
