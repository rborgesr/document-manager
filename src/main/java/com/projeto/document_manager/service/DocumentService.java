package com.projeto.document_manager.service;

import com.projeto.document_manager.dto.DocumentRequestDTO;
import com.projeto.document_manager.entity.Document;
import com.projeto.document_manager.entity.Project;
import com.projeto.document_manager.repository.DocumentRepository;
import com.projeto.document_manager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    public DocumentService(DocumentRepository documentRepository, ProjectRepository projectRepository) {
        this.documentRepository = documentRepository;
        this.projectRepository = projectRepository;
    }

    public Document fromDTO(DocumentRequestDTO dto, Project project) {
        Document document = new Document();
        document.setGuid(UUID.randomUUID().toString());
        document.setCode(dto.getCode());
        document.setTitle(dto.getTitle());
        document.setRevision(dto.getRevision());
        document.setType(dto.getType());
        document.setStatus(dto.getStatus());
        document.setDiscipline(dto.getDiscipline());
        document.setProject(project);
        document.setCreatedAt(new Date());
        document.setDeleted(false);
        return document;
    }

    public Document create(Document document) {
        return documentRepository.save(document);
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    public List<Document> findByProject(Project project) {
    return documentRepository.findByProject(project);
}


    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document update(Document document) {
        if (document.getProject() != null && document.getProject().getId() != null) {
            Optional<Project> projectOpt = projectRepository.findById(document.getProject().getId());
            projectOpt.ifPresent(document::setProject);
        }
        return documentRepository.save(document);
    }

    public void delete(Long id) {
        documentRepository.deleteById(id);
    }
}
