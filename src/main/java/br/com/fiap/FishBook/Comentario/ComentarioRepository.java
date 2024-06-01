package br.com.fiap.FishBook.Comentario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
    
    @Query("SELECT c FROM TB_COMENTARIO c WHERE c.usuario.ID_USUARIO = ?1")
    Page<Comentario> findByUsuarioId(Long idUsuario, Pageable pageable);
}
