package br.com.fiap.FishBook.Comentario;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
    
    @Query("SELECT c FROM TB_COMENTARIO c WHERE c.usuario.ID_USUARIO = ?1")
    Page<Comentario> findByUsuarioId(Long idUsuario, Pageable pageable);

    @Query("SELECT p FROM TB_COMENTARIO p WHERE p.postagem.ID_POSTAGEM = ?1")
    Page<Comentario> findByPostagemId(Long idPostagem, Pageable pageable);

    @Query("SELECT p FROM TB_COMENTARIO p WHERE p.postagem.ID_POSTAGEM = ?1")
    List<Comentario> findByPostagemId(Long idPostagem);
}
