package br.com.fiap.FishBook.Produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    
    @Query("SELECT p FROM TB_PRODUTO p WHERE p.usuario.ID_USUARIO = :idUsuario")
    Page<Produto> findByUsuarioId(Long idUsuario, Pageable pageable);
}
