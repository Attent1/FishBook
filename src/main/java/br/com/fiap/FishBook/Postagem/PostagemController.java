package br.com.fiap.FishBook.Postagem;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

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

import br.com.fiap.FishBook.Comentario.Comentario;
import br.com.fiap.FishBook.Comentario.ComentarioController;
import br.com.fiap.FishBook.Comentario.ComentarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("postagens")
@CacheConfig(cacheNames = "postagens")
@Tag(name = "Postagens", description = "Endpoint relacionado com Postagens")
public class PostagemController {

    @Autowired
    PostagemRepository repository;

    @Autowired
    ComentarioRepository repositoryComentario;

    @Autowired
    ComentarioController controllerComentario;

    @Autowired
    PagedResourcesAssembler<Postagem> pageAssembler;

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cria uma nova postagem.", description = "Cria um nova postagem no sistema.")
    public Postagem create(@RequestBody @Valid Postagem usuario) {
        return repository.save(usuario);
    }

    @GetMapping
    @Cacheable
    @Operation(summary = "Lista todas as postagems.", description = "Retorna uma lista de todas os postagems no sistema.") 
    public PagedModel<EntityModel<Postagem>> readAll(@PageableDefault(size = 500, sort = "dtPostagemInclusao", direction = Direction.DESC) Pageable pageable) {
        Page<Postagem> page = repository.findAll(pageable);
        return pageAssembler.toModel(page, Postagem::toEntityModel);
    }

    @GetMapping("{id}")
    @Operation(summary = "Retorna uma postagem por ID.", description = "Retorna uma postagem pelo seu ID.")
    public ResponseEntity<Postagem> readItem(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("postagensUsuario/{idUsuario}")
    @Operation(summary = "Retorna postagens de um usuário")
    public PagedModel<EntityModel<Postagem>> readIPostagensByUsuario(@PathVariable Long idUsuario, @PageableDefault(size = 500, sort = "dtPostagemInclusao", direction = Direction.DESC) Pageable pageable) {
        Page<Postagem> page = repository.findByUsuarioId(idUsuario, pageable);
        return pageAssembler.toModel(page, Postagem::toEntityModel);    
    }

    @PutMapping("{id}")
    @ResponseStatus(OK)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualiza uma postagem.", description = "Atualiza um postagem pelo seu ID.")
    public Postagem update(@PathVariable Long id, @RequestBody Postagem usuario) {
        verificarSeExistePostagem(id);
        usuario.setID_POSTAGEM(id);
        return repository.save(usuario);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Deleta uma postagem e seus comentários.", description = "Remove uma postagem pelo seu ID.")
    public ResponseEntity<Object> delete(@PathVariable Long id) {        
        verificarSeExistePostagem(id);
        List<Comentario> comentarios = repositoryComentario.findByPostagemId(id);
        for (int i = 0; i < comentarios.size(); i++) {
            controllerComentario.delete(comentarios.get(i).getID_COMENTARIO());
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Postagem verificarSeExistePostagem(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Postagem não encontrada"));
    }

}
