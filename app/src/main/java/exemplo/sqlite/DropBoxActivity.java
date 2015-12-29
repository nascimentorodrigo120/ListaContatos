package exemplo.sqlite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DropBoxActivity extends AppCompatActivity {

    final static private String APP_KEY = "";       //chave de acesso
    final static private String APP_SECRET = "";    // chave secreta

    final static private String ACCOUNT_PREFS_NAME = "prefs_dropbox";
    final static private String ACCESS_TOKEN = "ACCESS_TOKEN";

    private DropboxAPI<AndroidAuthSession> dropboxApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_box);

        // inicia e carrega a sessão
        AndroidAuthSession session = buildSession();
        dropboxApi = new DropboxAPI<AndroidAuthSession>(session);

        dropboxApi.getSession().startOAuth2Authentication(DropBoxActivity.this);
    }


    /* Método responsável de quando voltar da autenticacao, vai executar o metodo que faz upload, depois vai encerrar acitivity.
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        new Thread() {
            public void run() {
                uploadBackup();
                finish();

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(DropBoxActivity.this, " Aguarde...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        AndroidAuthSession session = dropboxApi.getSession();  // vai obter uma sessao

        if (session.authenticationSuccessful()) {  //ver se estar conectado
            session.finishAuthentication();     // é obrigatório chamar este metodo(encerra autenticacao)

            saveLogged(session);  // vai salvar uma sessao.
        }
    }


    /* Este método vai pegar as chaves - que vai carregar a sessao.
     */
    public AndroidAuthSession buildSession() {
        AppKeyPair akp = new AppKeyPair(APP_KEY, APP_SECRET);       // vai pegar as chaves
        AndroidAuthSession session = new AndroidAuthSession(akp);  // vai passar as chaves para abrir a sessão
        loadSession(session);                                     // método que vai carregar sessão (criada)

        return (session);
    }


    /*
     Método responsável por pegar as constantes do sharedPreferences declaradas em cima(token), e carregar a sessão.
     */
    public void loadSession(AndroidAuthSession session) {
        SharedPreferences sp = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String token = sp.getString(ACCESS_TOKEN, null);   // vai verificar o token.

        if (token == null || token.length() == 0) {  // se for vázio vai retornar nulo
            return;
        } else {
            session.setOAuth2AccessToken(token);   // se não vai abrir a sessão com o token.
        }
    }


    /* Método responsável por salvar uma sessão passada como parâmetro.
     */
    public void saveLogged(AndroidAuthSession session) {
        String token = session.getOAuth2AccessToken();           // vai obeter o token da sessão
        if (token != null) {                                     // vai se é diferente de nulo
            SharedPreferences sp = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);    //  vai salvar a sessão com sharedPrefenreces
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(ACCESS_TOKEN, token);
            edit.commit();
        }
    }


//---------------------------------------------------método que envia o backup para o dropBox------------------------------------------

    /* Método responsável por enviar o arquivo de backup para o dropbox
     */
    public void uploadBackup() {

        new Thread() {   // vai criar uma thread secundária para fazer a operação de upload (pois pode demorar).
            String resu;  // vai receber o resultado de DropboxAPI.Entry (um hash que indica que foi enviado) (se for nulo indica que não foi enviado)

            public void run() {
                try {
                    dropboxApi.metadata("/", 0, null, false, null);  // coloca o arquivo no diretorio geral.

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ContatosBackup.xml");  // pega o diretorio no sdcard (onde estar o arquivo).
                    FileInputStream is = new FileInputStream(file);
                    DropboxAPI.Entry reponse = dropboxApi.putFile("/Backup dos Contatos.xml", is, file.length(), null, null);

                    Log.i("Script", "Revision HASH (uploadFile): " + reponse.rev); // mostra o hash no log
                    resu = reponse.rev;

                } catch (DropboxException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() { // runOnUiThread permite a thread secundaria acessar a thread principal e exibir o toast
                    public void run() {
                        if (resu != null) {
                            Toast.makeText(DropBoxActivity.this, "Backup enviado com sucesso.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }.start();  // inicia a thread secundária que faz o upload
    }
}
