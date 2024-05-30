package br.com.fiap.FishBook.Usuario;

import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import br.com.fiap.FishBook.Usuario.validation.Tipo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity(name = "TB_USUARIO")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    @SequenceGenerator(name = "USUARIO_SEQ", sequenceName = "TB_USUARIO_SEQ", allocationSize = 1)
    private Long ID_USUARIO;

    @NotBlank  @Size(min = 1,max = 100)
    private String NOME;
    
    @NotBlank @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email inválido")
    @Size(max = 50)
    @Column(unique = true)
    private String EMAIL;

    @NotBlank @Size(min=8, max=8, message = "tamanho deve ser 8")
    private String SENHA;

    @Tipo(message = "{usuario.tipo.invalido}")
    private String TIPO;

    @Pattern(regexp = "\\d+", message = "Número de telefone inválido") @Size(min = 8, max = 14)
    private String NUMERO_TELEFONE;

    private LocalDate DT_USUARIO_INCLUSAO = LocalDate.now();

    public EntityModel<Usuario> toEntityModel() {
        return EntityModel.of(
                this,
                linkTo(methodOn(UsuarioController.class).readItem(ID_USUARIO)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).delete(ID_USUARIO)).withRel("delete"),
                linkTo(methodOn(UsuarioController.class).readAll(null)).withRel("contents")
            );
    }  

}
