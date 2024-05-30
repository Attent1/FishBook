package br.com.fiap.FishBook.Comentario;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("comentarios")
@CacheConfig(cacheNames = "comentarios")
@Tag(name = "Comentários", description = "Endpoint relacionado com Comentários")
public class ComentarioController {

    @Autowired
    ComentarioRepository repository;

    @Autowired
    PagedResourcesAssembler<Comentario> pageAssembler;

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cria um novo comentário.", description = "Cria um novo comentário no sistema.")
    public Comentario create(@RequestBody @Valid Comentario usuario) {
        return repository.save(usuario);
    }

    @GetMapping
    @Cacheable
    @Operation(summary = "Lista todos os comentários.", description = "Retorna uma lista de todos os comentários no sistema.") 
    public PagedModel<EntityModel<Comentario>> readAll(@PageableDefault(size = 5, sort = "dtComentarioInclusao", direction = Direction.DESC) Pageable pageable) {
        Page<Comentario> page = repository.findAll(pageable);
        return pageAssembler.toModel(page, Comentario::toEntityModel);
    }

    @GetMapping("{id}")
    @Operation(summary = "Retorna um comentário por ID.", description = "Retorna um comentário pelo seu ID.")
    public ResponseEntity<Comentario> readItem(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ResponseStatus(OK)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualiza um comentário.", description = "Atualiza um comentário pelo seu ID.")
    public Comentario update(@PathVariable Long id, @RequestBody Comentario usuario) {
        verificarSeExisteComentario(id);
        usuario.setID_COMENTARIO(id);
        return repository.save(usuario);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Deleta um comentário.", description = "Remove um comentário pelo seu ID.")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        verificarSeExisteComentario(id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Comentario verificarSeExisteComentario(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Comentário não encontrado"));
    }

}
