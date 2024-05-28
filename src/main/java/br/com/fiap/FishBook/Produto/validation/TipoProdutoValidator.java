package br.com.fiap.FishBook.Produto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoProdutoValidator implements ConstraintValidator<TipoProduto, String>  {
    
    @Override
    public boolean isValid(String tipo, ConstraintValidatorContext arg1) {
        return tipo.equals("PEIXES") || tipo.equals("CRUSTACEOS") 
        || tipo.equals("MOLUSCOS") || tipo.equals("ALGAS");
    }
}
