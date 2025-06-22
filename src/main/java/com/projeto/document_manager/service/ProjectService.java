package com.projeto.document_manager.service;

import com.projeto.document_manager.entity.Project;
import com.projeto.document_manager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project update(Project project) {
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    // Novo método para buscar projetos por disciplina na lista
    public List<Project> findByDiscipline(String discipline) {
        return projectRepository.findByDisciplinesContaining(discipline);
    }
}
