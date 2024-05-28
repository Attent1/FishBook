package br.com.fiap.FishBook.Postagem;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import java.time.LocalDate;
import br.com.fiap.FishBook.Usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity(name = "TB_POSTAGEM")
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID_POSTAGEM;

    @NotBlank
    @Size(min = 5, max = 15)
    private String TITULO;

    @NotBlank
    private String CONTEUDO;

    private LocalDate DT_POSTAGEM_INCLUSAO = LocalDate.now();

    @ManyToOne
    private Usuario usuario;

    public EntityModel<Postagem> toEntityModel() {
        return EntityModel.of(
                this,
                linkTo(methodOn(PostagemController.class).readItem(ID_POSTAGEM)).withSelfRel(),
                linkTo(methodOn(PostagemController.class).delete(ID_POSTAGEM)).withRel("delete"),
                linkTo(methodOn(PostagemController.class).readAll(null)).withRel("contents"));
    }

}
