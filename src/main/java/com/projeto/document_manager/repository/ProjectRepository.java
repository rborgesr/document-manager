package com.projeto.document_manager.repository;

import com.projeto.document_manager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Buscar projetos que contenham a disciplina na lista disciplines
    List<Project> findByDisciplinesContaining(String discipline);

    // Buscar projeto por nome 
    Optional<Project> findByName(String name);
}
