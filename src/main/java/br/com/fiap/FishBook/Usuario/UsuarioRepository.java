package br.com.fiap.FishBook.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM TB_USUARIO A WHERE A.EMAIL = :email", nativeQuery = true)
    Usuario LoginUsuario(@Param("email") String email);
}
