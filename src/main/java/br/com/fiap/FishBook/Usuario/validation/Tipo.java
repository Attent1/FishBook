package br.com.fiap.FishBook.Usuario.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = TipoValidator.class)

public @interface Tipo {
    String message() default "{usuario.tipo.invalido}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
