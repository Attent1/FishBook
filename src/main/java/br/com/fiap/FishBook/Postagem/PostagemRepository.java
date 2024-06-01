package br.com.fiap.FishBook.Postagem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{

    @Query("SELECT p FROM TB_POSTAGEM p WHERE p.usuario.ID_USUARIO = :idUsuario")
    Page<Postagem> findByUsuarioId(Long idUsuario, Pageable pageable);
}
