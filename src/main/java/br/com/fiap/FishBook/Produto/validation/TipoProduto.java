package br.com.fiap.FishBook.Produto.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = TipoProdutoValidator.class)

public @interface TipoProduto {
    String message() default "{produto.tipo.invalido}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
