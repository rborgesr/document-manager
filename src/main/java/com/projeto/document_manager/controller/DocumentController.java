package com.projeto.document_manager.controller;

import com.projeto.document_manager.dto.DocumentRequestDTO;
import com.projeto.document_manager.dto.DocumentResponseDTO;
import com.projeto.document_manager.entity.Document;
import com.projeto.document_manager.entity.Project;
import com.projeto.document_manager.service.DocumentService;
import com.projeto.document_manager.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Operações relacionadas a documentos")
public class DocumentController {

    private final DocumentService documentService;
    private final ProjectService projectService;

    public DocumentController(DocumentService documentService, ProjectService projectService) {
        this.documentService = documentService;
        this.projectService = projectService;
    }

    @Operation(summary = "Lista todos os documentos", description = "Retorna uma lista com todos os documentos cadastrados.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documentos retornados com sucesso"),
        @ApiResponse(responseCode = "204", description = "Nenhum documento cadastrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments() {
        List<Document> documents = documentService.findAll();
        if (documents.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        List<DocumentResponseDTO> dtos = documents.stream()
                .map(DocumentResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos); // 200
    }

    @Operation(summary = "Busca documento por ID", description = "Retorna os dados do documento correspondente ao ID informado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento encontrado"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@PathVariable Long id) {
        try {
            Optional<Document> optionalDoc = documentService.findById(id);
            if (optionalDoc.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado.");
            }
            return ResponseEntity.ok(DocumentResponseDTO.fromEntity(optionalDoc.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar documento: " + e.getMessage());
        }
    }

    @Operation(summary = "Cria um novo documento", description = "Cadastra um novo documento vinculado a um projeto via JSON.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
        @ApiResponse(responseCode = "409", description = "Documento em conflito (ex: código duplicado)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<?> createDocument(@RequestBody DocumentRequestDTO dto) {
        try {
            Optional<Project> optProject = projectService.findById(dto.getProjectId());
            if (optProject.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Projeto com ID " + dto.getProjectId() + " não encontrado.");
            }

            Document document = documentService.fromDTO(dto, optProject.get());
            Document created = documentService.create(document);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(DocumentResponseDTO.fromEntity(created)); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao criar documento: " + e.getMessage()); // 400
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + e.getMessage()); // 500
        }
    }

    @Operation(summary = "Atualiza um documento", description = "Atualiza os dados de um documento existente via parâmetros.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Documento ou projeto não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocumentByQueryParams(
            @PathVariable Long id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer revision,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String discipline,
            @RequestParam(required = false) Long projectId
    ) {
        try {
            Optional<Document> optionalDoc = documentService.findById(id);
            if (optionalDoc.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado.");
            }

            Document document = optionalDoc.get();

            if (code != null) document.setCode(code);
            if (title != null) document.setTitle(title);
            if (revision != null) document.setRevision(revision);
            if (type != null) document.setType(type);
            if (status != null) document.setStatus(status);
            if (discipline != null) document.setDiscipline(discipline);

            if (projectId != null) {
                Optional<Project> optProject = projectService.findById(projectId);
                if (optProject.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Projeto com ID " + projectId + " não encontrado.");
                }
                document.setProject(optProject.get());
            }

            Document updated = documentService.update(document);
            return ResponseEntity.ok(DocumentResponseDTO.fromEntity(updated)); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar: " + e.getMessage());
        }
    }

    @Operation(summary = "Remove um documento", description = "Exclui permanentemente um documento com base no ID informado.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Documento removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        try {
            Optional<Document> optionalDoc = documentService.findById(id);
            if (optionalDoc.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado.");
            }
            documentService.delete(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir documento: " + e.getMessage());
        }
    }
}
