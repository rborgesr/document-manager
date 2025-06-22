package com.projeto.document_manager.controller;

import com.projeto.document_manager.dto.DocumentResponseDTO;
import com.projeto.document_manager.dto.ProjectResponseDTO;
import com.projeto.document_manager.entity.Document;
import com.projeto.document_manager.entity.Project;
import com.projeto.document_manager.service.DocumentService;
import com.projeto.document_manager.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operações relacionadas a projetos")
public class ProjectController {

    private final ProjectService projectService;
    private final DocumentService documentService;

    public ProjectController(ProjectService projectService, DocumentService documentService) {
        this.projectService = projectService;
        this.documentService = documentService;
    }

    @Operation(summary = "Lista todos os projetos",
               description = "Retorna uma lista com todos os projetos cadastrados no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso"),
        @ApiResponse(responseCode = "204", description = "Nenhum projeto cadastrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno ao listar projetos")
    })
    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        if (projects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProjectResponseDTO> dtos = projects.stream()
                .map(ProjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Busca um projeto pelo ID",
               description = "Retorna o projeto correspondente ao ID informado, ou 404 se não existir.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Projeto encontrado e retornado"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno ao buscar projeto")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        Optional<Project> optionalProject = projectService.findById(id);
        return optionalProject
                .map(project -> ResponseEntity.ok(ProjectResponseDTO.fromEntity(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista documentos de um projeto",
               description = "Retorna todos os documentos vinculados a um projeto específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documentos retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno ao buscar documentos do projeto")
    })
    @GetMapping("/{id}/documents")
    public ResponseEntity<?> getDocumentsByProject(@PathVariable Long id) {
        Optional<Project> optionalProject = projectService.findById(id);
        if (optionalProject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Projeto com ID " + id + " não encontrado.");
        }

        List<Document> documents = documentService.findByProject(optionalProject.get());
        List<DocumentResponseDTO> dtos = documents.stream()
                .map(DocumentResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Cria um novo projeto",
               description = "Cria um projeto com as informações fornecidas via parâmetros.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou faltando"),
        @ApiResponse(responseCode = "409", description = "Projeto com dados conflitantes já existe"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<?> createProject(
        @RequestParam String name,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
        @RequestParam List<String> disciplines
    ) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do projeto é obrigatório.");
            }
            if (startDate == null || endDate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datas de início e fim são obrigatórias.");
            }
            if (startDate.after(endDate)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de início não pode ser depois da data de fim.");
            }

            Project project = new Project();
            project.setName(name);
            project.setStartDate(startDate);
            project.setEndDate(endDate);
            project.setDisciplines(disciplines);

            Project created = projectService.create(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponseDTO.fromEntity(created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar projeto: " + e.getMessage());
        }
    }

    @Operation(summary = "Atualiza um projeto existente pelo ID",
               description = "Atualiza os dados do projeto identificado pelo ID. Parâmetros opcionais.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflito de dados durante atualização"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
        @PathVariable Long id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
        @RequestParam(required = false) List<String> disciplines
    ) {
        try {
            Optional<Project> optionalProject = projectService.findById(id);
            if (optionalProject.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
            }

            Project project = optionalProject.get();

            if (name != null && name.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do projeto não pode ser vazio.");
            }
            if (startDate != null && endDate != null && startDate.after(endDate)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de início não pode ser depois da data de fim.");
            }

            if (name != null) {
                project.setName(name);
            }
            if (startDate != null) {
                project.setStartDate(startDate);
            }
            if (endDate != null) {
                project.setEndDate(endDate);
            }
            if (disciplines != null) {
                project.setDisciplines(disciplines);
            }

            Project updated = projectService.update(project);
            return ResponseEntity.ok(ProjectResponseDTO.fromEntity(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar projeto: " + e.getMessage());
        }
    }

    @Operation(summary = "Remove um projeto pelo ID",
               description = "Exclui o projeto identificado pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Projeto removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            Optional<Project> optionalProject = projectService.findById(id);
            if (optionalProject.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado.");
            }

            projectService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover projeto: " + e.getMessage());
        }
    }
}
