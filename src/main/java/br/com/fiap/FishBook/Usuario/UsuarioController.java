package br.com.fiap.FishBook.Usuario;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("usuarios")
@CacheConfig(cacheNames = "usuarios")
@Tag(name = "Usuários", description = "Endpoint relacionado com Usuários")
public class UsuarioController {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    PagedResourcesAssembler<Usuario> pageAssembler;

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cria um novo usuário.", description = "Cria um novo usuário no sistema.")
    public Usuario create(@RequestBody @Valid Usuario usuario) {
        return repository.save(usuario);
    }

    @GetMapping
    @Cacheable
    @Operation(summary = "Lista todos os usuários.", description = "Retorna uma lista de todos os usuários no sistema.")
    public PagedModel<EntityModel<Usuario>> readAll(@PageableDefault(size = 5, sort = "NOME", direction = Direction.ASC) Pageable pageable) {
        Page<Usuario> page = repository.findAll(pageable);
        return pageAssembler.toModel(page, Usuario::toEntityModel);
    }

    @GetMapping("{id}")
    @Operation(summary = "Retorna um usuário por ID.", description = "Retorna um usuário pelo seu ID.")
    public ResponseEntity<Usuario> readItem(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ResponseStatus(OK)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualiza um usuário.", description = "Atualiza um usuário pelo seu ID.")
    public Usuario update(@PathVariable Long id, @RequestBody Usuario usuario) {
        verificarSeExisteUsuario(id);
        usuario.setID_USUARIO(id);
        return repository.save(usuario);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Deleta um usuário.", description = "Remove um usuário pelo seu ID.")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        verificarSeExisteUsuario(id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Usuario verificarSeExisteUsuario(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = repository.LoginUsuario(email);
        
        if (usuario != null) {
            if (usuario.getSENHA().equals(senha)) {                
                return ResponseEntity.ok("ID:" + usuario.getID_USUARIO());
                
            }
        }        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cod status: " + HttpStatus.BAD_REQUEST.value());
    }

}
