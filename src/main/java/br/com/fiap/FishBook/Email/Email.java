package br.com.fiap.FishBook.Email;

import lombok.Data;

@Data
public class Email {

    private String destinatario;

    private String assunto;

    private String conteudo;
}
