package br.com.fiap.FishBook.Produto;

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
@RequestMapping("produtos")
@CacheConfig(cacheNames = "produtos")
@Tag(name = "Produtos", description = "Endpoint relacionado com Produtos")
public class ProdutoController {

    @Autowired
    ProdutoRepository repository;

    @Autowired
    PagedResourcesAssembler<Produto> pageAssembler;

    @PostMapping
    @ResponseStatus(CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cria um novo produto.", description = "Cria um novo produto no sistema.")
    public Produto create(@RequestBody @Valid Produto usuario) {
        return repository.save(usuario);
    }

    @GetMapping
    @Cacheable
    @Operation(summary = "Lista todos os produtos.", description = "Retorna uma lista de todos os produtos no sistema.")
    public PagedModel<EntityModel<Produto>> readAll(@PageableDefault(size = 5, sort = "PRECO", direction = Direction.ASC) Pageable pageable) {
        Page<Produto> page = repository.findAll(pageable);
        return pageAssembler.toModel(page, Produto::toEntityModel);
    }

    @GetMapping("{id}")
    @Operation(summary = "Retorna um produto por ID.", description = "Retorna um produto pelo seu ID.")
    public ResponseEntity<Produto> readItem(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    @ResponseStatus(OK)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualiza um produto.", description = "Atualiza um produto pelo seu ID.")
    public Produto update(@PathVariable Long id, @RequestBody Produto usuario) {
        verificarSeExisteProduto(id);
        usuario.setID_PRODUTO(id);
        return repository.save(usuario);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Deleta um produto.", description = "Remove um produto pelo seu ID.")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        verificarSeExisteProduto(id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Produto verificarSeExisteProduto(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }

}
