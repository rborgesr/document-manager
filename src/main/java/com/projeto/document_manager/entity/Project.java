package com.projeto.document_manager.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date startDate;
    private Date endDate;

    // Alterado para lista de disciplinas (Strings)
    @ElementCollection
    @CollectionTable(name = "project_disciplines", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "discipline")
    private List<String> disciplines;

    // Getters e Setters
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
