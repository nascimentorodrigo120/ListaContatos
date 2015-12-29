package exemplo.sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import exemplo.sqlite.DataBase.ContatosHelper;


public class Tela_CadastroActivity extends AppCompatActivity {

    public EditText edit, edit2, edit3, edit4;
    RelativeLayout relativeTelaCadastro;
    Button buttonTelaCadastro1, buttonTelaCadastro2;
    public ImageView imageFoto; //ImageView vai tirar a foto
    String caminho;
    String tema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edit  = (EditText) findViewById(R.id.edt);
        edit2 = (EditText) findViewById(R.id.edt2);
        edit3 = (EditText) findViewById(R.id.edt3);
        edit4 = (EditText) findViewById(R.id.edt4);

        imageFoto = (ImageView) findViewById(R.id.imageView);

        Intent it = getIntent();
        tema = it.getStringExtra("msg");

        setarTema(tema);
    }


    /*
     Método responsável por deixar os editText em branco quando o onRestart() for chamado.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        edit.setText("");
        edit2.setText("");
        edit3.setText("");
        edit4.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela__cadastro, menu);
        setTitle("Cadastro");
        return true;
    }


    /*
    Método responsável por setar o tema. Vai receber uma string especificando o tema.
     */
    public void setarTema(String tema) {

        relativeTelaCadastro = (RelativeLayout) findViewById(R.id.relativeTelaCadastro);
        buttonTelaCadastro1 = (Button) findViewById(R.id.buttonTelaCadastro1);
        buttonTelaCadastro2 = (Button) findViewById(R.id.buttonTelaCadastro2);

        if (tema.equals("branco")) {
            relativeTelaCadastro.setBackgroundColor(Color.LTGRAY);
            buttonTelaCadastro1.setBackgroundResource(R.drawable.botao_cadastra);
            buttonTelaCadastro2.setBackgroundResource(R.drawable.botao_listar_conta);
        }
        if (tema.equals("tema3")) {
            relativeTelaCadastro.setBackgroundResource(R.drawable.tema3);
            buttonTelaCadastro1.setBackgroundResource(R.drawable.button_cadastrar);
            buttonTelaCadastro2.setBackgroundResource(R.drawable.button_listar);
        }
        if (tema.equals("tema4")) {
            relativeTelaCadastro.setBackgroundResource(R.drawable.tema4);
            buttonTelaCadastro1.setBackgroundResource(R.drawable.button_cadastrar);
            buttonTelaCadastro2.setBackgroundResource(R.drawable.button_listar);
        }
        if (tema.equals("")) ;
    }


    public void tirarFoto(View view) {

        try {
            File dir = new File("///sdcard/fotoAluno/");   //cria um novo diretorio.
            if (!dir.exists()) {
                dir.mkdir();
            }
            String hora = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File(dir, hora + ".png");
            file.createNewFile();

            caminho = file.getAbsolutePath();
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 1);       // starta a intent da camera e tem uma requisicao que eh 1

        } catch (IOException e) {
            Log.e("ERRO", e.getMessage());
        }

    }


    /*
     vai tratar a requisição se é a mesma e se ocorreu tudo bem então vai exibir a foto. Então vai setar a foto no imageView.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 & resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(caminho);     //pegar o diretorio e seta a imagem.
            Bitmap scale = Bitmap.createScaledBitmap(bitmap, 100, 70, true);
            imageFoto.setImageBitmap(scale);
        }
    }


    public void botaoCadastrar(View v) {

        // aluno que recebe os dados passados nos textView
        final Contatos contato = new Contatos(edit.getText().toString(), edit2.getText().toString(), edit3.getText().toString(), edit4.getText().toString(), caminho);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Tela_CadastroActivity.this);
        alertDialog.setTitle("CADASTRO");
        alertDialog.setMessage("CONTATO: \n " + contato.getNome() + "\n" + contato.getEndereco()
                + "\n" + contato.getTelefone() + "\n" + contato.getEmail());

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                ContatosHelper helper = new ContatosHelper(Tela_CadastroActivity.this);
                helper.cadastra(contato);
                Toast.makeText(Tela_CadastroActivity.this, "CONTATO  CADASTRADO.", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {// evento do botao cancelar
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        Bitmap bitmap = verificaFoto();

        Drawable draw = new BitmapDrawable(bitmap);
        alertDialog.setIcon(draw);

        alertDialog.create();
        alertDialog.show();
    }


    public void botaoListar(View v) {               // botao que chama a outra activity onde vai mostrar o listView.
        dialog_Espera();
    }


    /*
    Método que vai setar a foto quando o usuário apertar em cadastrar. Vai receber o caminho da foto.
    Se o caminho for nulo significa que não foi tirado nehuma foto, então será setado com a foto padrão.
    Se o caminho for diferente de nulo significa que o usuario tirou a foto, então esta foto será setada.
     */
    public Bitmap verificaFoto() {

        Bitmap bitmap;

        if (caminho == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.foto);

        } else {
            bitmap = BitmapFactory.decodeFile(caminho);
        }

        Bitmap scale = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        return scale;
    }


    // dialog que é chamado enquanto a tela de (listar contatos) não aparece.
    public void dialog_Espera() {

        boolean podeCancelar = true;
        boolean indeterminado = false;
        String titulo = "Carregando Contatos";
        String mensagem = "Aguarde...";
        final ProgressDialog dialog = ProgressDialog.show(this, titulo, mensagem, indeterminado, podeCancelar);

        new Thread() {

            public void run() {
                Intent it = new Intent(Tela_CadastroActivity.this, Tela2Activity.class);
                it.putExtra("msg", tema);
                startActivity(it);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        }.start();
    }
}
