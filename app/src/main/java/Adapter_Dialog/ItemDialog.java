package Adapter_Dialog;

/**
 * Created by irom on 07/11/2015.
 */
public class ItemDialog  {

    private String nome;
    private int imagem;

    public ItemDialog(String nome, int imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public int getImagem() {
        return imagem;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
