package exemplo.sqlite;

import java.io.Serializable;

/**
 * Created by rodrigo on 03/11/2015.
 */
public class Contatos implements Serializable {

    private long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String foto;

    public Contatos(String nome, String endereco, String telefone, String email, String foto) {


        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.foto = foto;
    }


    public Contatos() {
    }


    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    @Override
    public String toString() {   // observar se vai aparecer so o nome quando chamar o toString.
        return nome;
    }
}

