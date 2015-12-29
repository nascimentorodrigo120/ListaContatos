package exemplo.sqlite;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import exemplo.sqlite.Criar_Importar_Xml.Cria_ImportaXml;
import exemplo.sqlite.DataBase.ContatosHelper;

public class Tela_BackupActivity extends AppCompatActivity {

    TextView txtCaminhoBackup;
    EditText edtNomeBackup;
    File localBackup;
    List<Contatos> listaContatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela__backup);
        setTitle("Backup");

        txtCaminhoBackup = (TextView) findViewById(R.id.textViewCaminhoBackup);
        edtNomeBackup = (EditText) findViewById(R.id.editTextCaminhoBackup);
        listaContatos = new ContatosHelper(this).carregaContatos();  // lista que vai ser passada para o método criar backup.

        localBackup = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ContatosBackup.xml");

    }


//----------------------------------------------------botões de criar backup e impotar backup--------------------------------------

    /* Método responsável por criar o arquivo de backup. Instância o objeto e chama o método que vai criar o arquivo xml no device.
    */
    public void botaoCriarBackup(View view) {
        try {
            new Cria_ImportaXml().criarXml(listaContatos, localBackup);
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtCaminhoBackup.setText(localBackup.getAbsolutePath());
        Toast.makeText(Tela_BackupActivity.this, "Backup Criado com Sucesso.", Toast.LENGTH_SHORT).show();
    }

    /* Método responsável por restaurar o arquivo de backup. Instância o objeto e chama o método que vai restaurar o arquivo xml
       no device. Este método recebe como parâmetro um Contexto e um File(caminho do arquivo xml).
    */
    public void botaoImportarBackup(View view) {

        String nomeArquivo = edtNomeBackup.getText().toString();
        File fileImportacaoBackup = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nomeArquivo);

        try {          // instancia objeto e chamar o metodo que vai importar o backup para aplicacao (recebe contexto File).
            new Cria_ImportaXml().importarContatos(this, fileImportacaoBackup);
        } catch (JDOMException e) {
            Toast.makeText(Tela_BackupActivity.this, "Erro na importacao do Backup.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(Tela_BackupActivity.this, "Arquivo nao encontrado \n ou nao estar errado. ", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(Tela_BackupActivity.this, "Apague o arquivo de backup ", Toast.LENGTH_SHORT).show();
        finish();
    }


    //----------------------------------------------------opção enviar backup para o email ou dropbox--------------------------------------
    /*
    Vai inflar o menu na actionBar(opcao de enviar backup pra nuvem: através de email ou dropbox)
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_backup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resu = item.getItemId();

        switch (resu) {
            case R.id.action_enviar_email:
                dialog_enviar_BackupEmail();
                break;

            // vai chamar activity, na qual faz o upload do xml para o dropbox.
            case R.id.action_enviar_dropbox:
                startActivity(new Intent(this, DropBoxActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//-----------------------------------------------método que enviar o backup para o email-----------------------------------------------

    /*
    Método responsável por inflar o layout do dialog, e enviar o arquivo xml do backup por email (utilizando a intent nativa do android).
     */
    public void dialog_enviar_BackupEmail() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_backup);
        dialog.setTitle("Enviar Backup");

        Button enviar = (Button) dialog.findViewById(R.id.buttonEnviarEmail); //componentes do layout do dialog.
        Button cancelar = (Button) dialog.findViewById(R.id.buttonCancelarEmail);
        final EditText edtMensagem = (EditText) dialog.findViewById(R.id.editTextMensagem);
        final EditText edtDestino = (EditText) dialog.findViewById(R.id.editText2Destino);
        final EditText edtAnexo = (EditText) dialog.findViewById(R.id.editText3Anexo);
        edtAnexo.setText(localBackup.getAbsolutePath());               // seta o edittext com o local do backup

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itEmail = new Intent(Intent.ACTION_SENDTO);
                itEmail.putExtra(Intent.EXTRA_SUBJECT, "Backup Contatos");
                itEmail.putExtra(Intent.EXTRA_TEXT, edtMensagem.getText().toString());
                itEmail.putExtra(Intent.EXTRA_EMAIL, edtDestino.getText().toString());

                itEmail.putExtra(Intent.EXTRA_STREAM, Uri.parse(localBackup.getAbsolutePath())); // local do anexo
                itEmail.setType("application/xml");    // a extenssao do anexo.
                startActivity(Intent.createChooser(itEmail, "Escolha a App para envio do e-mail..."));
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
