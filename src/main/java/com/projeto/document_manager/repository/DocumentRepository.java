package com.projeto.document_manager.repository;

import com.projeto.document_manager.entity.Document;
import com.projeto.document_manager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Buscar documentos pelo objeto Project (entidade)
    List<Document> findByProject(Project project);

  /*
    // Buscar documentos por disciplina (campo String discipline em Document)
    List<Document> findByDiscipline(String discipline);

    List<Document> findByType(Integer type);

    List<Document> findByStatus(Integer status);

    List<Document> findByRevision(Integer revision);*/
}
