package br.com.fiap.FishBook.Produto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import org.springframework.hateoas.EntityModel;
import br.com.fiap.FishBook.Produto.validation.TipoProduto;
import br.com.fiap.FishBook.Usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity(name = "TB_PRODUTO")
public class Produto {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUTO_SEQ")
    @SequenceGenerator(name = "PRODUTO_SEQ", sequenceName = "TB_PRODUTO_SEQ", allocationSize = 1)
    private Long ID_PRODUTO;

    @TipoProduto(message = "{produto.tipo.invalido}")
    private String TIPO;

    @NotBlank  @Size(min = 3,max = 100)
    private String DESCRICAO;    

    @Positive
    private BigDecimal PRECO;

    @Positive
    private int DISPONIBILIDADE;
    
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    public EntityModel<Produto> toEntityModel() {
        return EntityModel.of(
                this,
                linkTo(methodOn(ProdutoController.class).readItem(ID_PRODUTO)).withSelfRel(),
                linkTo(methodOn(ProdutoController.class).delete(ID_PRODUTO)).withRel("delete"),
                linkTo(methodOn(ProdutoController.class).readAll(null)).withRel("contents")
            );
    }  

}
