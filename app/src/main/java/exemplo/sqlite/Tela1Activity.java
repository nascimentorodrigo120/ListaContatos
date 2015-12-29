package exemplo.sqlite;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class Tela1Activity extends AppCompatActivity {

    public AlertDialog alerta;

    RelativeLayout relativeTela1;
    ImageButton imageButtonTela1, imageButton2Tela1;
    ImageView imageCapa;
    String tema = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);

        relativeTela1 = (RelativeLayout) findViewById(R.id.relativeTela1);
        imageButtonTela1 = (ImageButton) findViewById(R.id.imageButtonTela1);
        imageButton2Tela1 = (ImageButton) findViewById(R.id.imageButton2Tela1);
        imageCapa = (ImageView) findViewById(R.id.imageView3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela1, menu);
        setTitle("Lista de Contatos");

        return true;
    }


    /*
    Este método vai setar o tema escolhido.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resu = item.getItemId();

        if (resu == R.id.action_tema_padrao) {
            relativeTela1.setBackgroundColor(Color.LTGRAY);
            imageCapa.setBackgroundResource(R.drawable.image_capa_padrao);
            imageButtonTela1.setBackgroundResource(R.drawable.botao_cadastra);
            imageButton2Tela1.setBackgroundResource(R.drawable.botao_listar_conta);
            tema = "branco";
        }
        if (resu == R.id.action_tema_madeira_escuro) {
            relativeTela1.setBackgroundResource(R.drawable.tema3);
            imageCapa.setBackgroundResource(R.drawable.image_capa);
            imageButtonTela1.setBackgroundResource(R.drawable.button_cadastrar);
            imageButton2Tela1.setBackgroundResource(R.drawable.button_listar);
            tema = "tema3";
        }
        if (resu == R.id.action_tema_madeira_claro) {
            relativeTela1.setBackgroundResource(R.drawable.tema4);
            imageCapa.setBackgroundResource(R.drawable.image_capa);
            imageButtonTela1.setBackgroundResource(R.drawable.button_cadastrar);
            imageButton2Tela1.setBackgroundResource(R.drawable.button_listar);
            tema = "tema4";
        }
        return super.onOptionsItemSelected(item);
    }


    public void botaoCadastrar(View view) {

        Intent it = new Intent(Tela1Activity.this, Tela_CadastroActivity.class);
        it.putExtra("msg", tema);
        startActivity(it);
    }


    public void botaoListar(View view) {

        dialog_Espera();
    }


    /* Método que é chamado enquanto a tela de (listar contatos) nao aparece.
     */
    public void dialog_Espera() {

        boolean podeCancelar = true;
        boolean indeterminado = false;
        String titulo = "Carregando Contatos";
        String mensagem = "Aguarde...";
        final ProgressDialog dialog = ProgressDialog.show(this, titulo, mensagem, indeterminado, podeCancelar); //Progress de espera

        new Thread() {
            //vai chamar a outra activity
            public void run() {
                Intent it = new Intent(Tela1Activity.this, Tela2Activity.class);
                it.putExtra("msg", tema);
                startActivity(it);

                // acessar a thread principal.
                runOnUiThread(new Runnable() { // depois que a outra activity for chamada o progress vai ser cancelado.
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        }.start();
    }
}






