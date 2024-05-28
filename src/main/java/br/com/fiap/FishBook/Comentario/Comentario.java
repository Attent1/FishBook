package br.com.fiap.FishBook.Comentario;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import org.springframework.hateoas.EntityModel;
import br.com.fiap.FishBook.Postagem.Postagem;
import br.com.fiap.FishBook.Usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity(name = "TB_COMENTARIO")
public class Comentario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID_COMENTARIO;

    @NotBlank
    private String CONTEUDO;

    private LocalDate INCLUSAO = LocalDate.now();

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Postagem postagem;

    public EntityModel<Comentario> toEntityModel() {
        return EntityModel.of(
                this,
                linkTo(methodOn(ComentarioController.class).readItem(ID_COMENTARIO)).withSelfRel(),
                linkTo(methodOn(ComentarioController.class).delete(ID_COMENTARIO)).withRel("delete"),
                linkTo(methodOn(ComentarioController.class).readAll(null)).withRel("contents"));
    }
}
