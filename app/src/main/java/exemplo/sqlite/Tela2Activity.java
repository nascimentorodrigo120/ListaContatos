package exemplo.sqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter_Dialog.DialogImagemAdapter;
import Adapter_Dialog.ItemDialog;
import exemplo.sqlite.Adapter.ContatoListAdapter;
import exemplo.sqlite.DataBase.ContatosHelper;

/**
 * Created by rodrigo on 03/11/2015.
 */
public class Tela2Activity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ContatoListAdapter adapter;
    public ListView listView;
    public AlertDialog alerta;
    RelativeLayout relativeTela2;

    public List<Contatos> listContatos = new ArrayList<Contatos>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela2);

        Intent it = getIntent();              // pegar o valor de quando foi setado o tema na tela1Activity
        String resu = it.getStringExtra("msg");
        setarTema(resu);                      // metodo que detecta o tema e seta-o

        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.textViewListaVazia));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // Evento da lista(click rapido).
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {

                String caminhoFoto;  // vai receber o caminho da foto do contato especificado

                AlertDialog alertDialog = new AlertDialog.Builder(Tela2Activity.this).create();
                alertDialog.setTitle("Contato");
                alertDialog.setMessage("Dados do Contato: \n" +
                        listContatos.get(position).getNome() + "\n" +  // mostra os dados do Contatos
                        listContatos.get(position).getEndereco() + "\n" +
                        listContatos.get(position).getTelefone() + "\n" +
                        listContatos.get(position).getEmail());
                caminhoFoto = listContatos.get(position).getFoto();

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {    // acao do botao OK do dialog

                        dialog.cancel();
                    }
                });

                Bitmap bitmap = verificaFoto2(caminhoFoto);

                Drawable draw = new BitmapDrawable(bitmap); // passa a imagem para o drawable
                alertDialog.setIcon(draw);
                alertDialog.show();
            }
        });


        /* Evento da lista (pressionado por muito tempo). Vai oferecer um menu de opções.
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Contatos contatoSelecionado = (Contatos) parent.getItemAtPosition(position);  // pega a posicão

                ArrayList<ItemDialog> itens = new ArrayList<ItemDialog>();     //lista de nomes do alertDialog
                itens.add(new ItemDialog("Atualizar", R.drawable.ic_atualizar));
                itens.add(new ItemDialog("Procurar no Mapa", R.drawable.ic_mapa));
                itens.add(new ItemDialog("Enviar SMS", R.drawable.ic_sms));
                itens.add(new ItemDialog("Excluir", R.drawable.ic_excluir));
                itens.add(new ItemDialog("Ligar", R.drawable.ic_chamaada));
                itens.add(new ItemDialog("Enviar Email", R.drawable.ic_email));

                //adapter utilizando um layout customizado (TextView)
                ListAdapter adapter = new DialogImagemAdapter(Tela2Activity.this, itens);

                AlertDialog.Builder builder = new AlertDialog.Builder(Tela2Activity.this);
                builder.setTitle("Menu");
                builder.setIcon(R.drawable.menu_contatos);


                builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        metodoSwitch(arg1, contatoSelecionado);  //método na qual vai chamar cada item de acordo com selecionado.
                        arg0.dismiss();
                    }
                });
                alerta = builder.create();
                alerta.show();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela2, menu);
        setTitle("Contatos");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int resu = item.getItemId();

        if (resu == R.id.action_search) {
        }

        if (resu == R.id.action_backup) {
            startActivity(new Intent(this, Tela_BackupActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    Método responsável por setar o tema da Tela de cadastro. Vai receber uma string(referencia do tema).
     */
    public void setarTema(String tema) {

        relativeTela2 = (RelativeLayout) findViewById(R.id.relativeTela2);

        if (tema.equals("branco")) {
            relativeTela2.setBackgroundColor(Color.LTGRAY);
        }
        if (tema.equals("tema3")) {
            relativeTela2.setBackgroundResource(R.drawable.tema3);
        }
        if (tema.equals("tema4")) {
            relativeTela2.setBackgroundResource(R.drawable.tema4);
        }
        if (tema.equals("")) {
        }
    }

    /*
      Método responsável por receber uma lista com os contatos do banco, e repassa-lá para uma listaContatos na activity, na qual vai
      ser setado no listView.
     */
    @Override
    protected void onResume() {
        super.onResume();
        ContatosHelper helper = new ContatosHelper(this);
        listContatos = helper.carregaContatos();  // a lista vai receber o retorno do medoto(vai retorna uma lista de contato).

        adapter = new ContatoListAdapter(Tela2Activity.this, listContatos);
        listView.setAdapter(adapter);
    }

    /*
    Método responsável por chamar os métodos do menu do dialog(Click longo), vai receber o posiçao clickada e o objeto.
     */
    public void metodoSwitch(int i, Contatos contato) {

        switch (i) {

            case 0:
                atualizarContato(contato);
                break;

            case 1:
                mapaContato(contato);
                break;

            case 2:
                enviarSms(contato);
                break;

            case 3:
                deletarContato(contato);
                break;

            case 4:
                ligarContato(contato);
                break;

            case 5:
                enviarEmail(contato);
                break;
        }
    }

    //----------------------------------método que serao chamados no switch--------------------------------------------------------------

    /*
    Método responsável por deletar um Contato.
     */
    public void deletarContato(final Contatos contato) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirma a Exclusao de " + contato.getNome());

        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //deleta o contato
                ContatosHelper helper = new ContatosHelper(Tela2Activity.this);
                helper.excluir(contato);
                helper.close();

                MediaPlayer mp = MediaPlayer.create(Tela2Activity.this, R.raw.explosao);
                mp.start();

                listContatos = helper.carregaContatos(); // atualiza a lista chamando o metodo carrega Contatos
                helper.close();

                adapter = new ContatoListAdapter(Tela2Activity.this, listContatos);
                listView.setAdapter(adapter);     //seta o adapter e o listView novamente.
                adapter.notifyDataSetChanged();

                Toast.makeText(Tela2Activity.this, contato.getNome() + " Excluido ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("NAO", null);
        alerta = builder.create();
        alerta.setTitle("EXCLUSAO");
        alerta.show();
    }

    /*
    Método responsável por atualizar o contato passado como parâmetro. Vai chamar um dialog criado para atualizaçaõ dos contatos.
     */
    public void atualizarContato(final Contatos contato) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog2);
        dialog.setTitle("Atualizar contato");

        //componentes do layout do dialog.
        Button salvar = (Button) dialog.findViewById(R.id.button3);
        Button cancelar = (Button) dialog.findViewById(R.id.button4);
        final EditText edtNome = (EditText) dialog.findViewById(R.id.editTextNome);
        final EditText edtEndereco = (EditText) dialog.findViewById(R.id.editTextEndereco);
        final EditText edtTelefone = (EditText) dialog.findViewById(R.id.editTextTelefone);
        final EditText edtEmail = (EditText) dialog.findViewById(R.id.editTextEmail);

        // Verifica se o contato passado com parâmetro não é nulo.
        if (contato != null) {
            edtNome.setText(contato.getNome());
            edtEndereco.setText(contato.getEndereco());
            edtTelefone.setText(contato.getTelefone());
            edtEmail.setText(contato.getEmail());

        } else {
            Toast.makeText(Tela2Activity.this, "Contato null ", Toast.LENGTH_SHORT).show();
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                contato.setNome(edtNome.getText().toString());   //recebe os nomes dos editText atualizado e seta no contato os novos dados.
                contato.setEndereco(edtEndereco.getText().toString());
                contato.setTelefone(edtTelefone.getText().toString());
                contato.setEmail(edtEmail.getText().toString());
                contato.setFoto(contato.getFoto());

                ContatosHelper helper = new ContatosHelper(Tela2Activity.this); // recebe o contato com os dados novos, e chama o metodo alterar.
                helper.alterar(contato);       //passando o novo contato.
                helper.close();

                Toast.makeText(Tela2Activity.this, "Atualizado " + edtNome.getText().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /*
    Método responsável por fazer a chamada telefônica. Vai receber um Contato como parâmetro e pegar seu número telefone.
     */
    public void ligarContato(Contatos contato) {

        Intent it = new Intent(Intent.ACTION_CALL);
        it.setData(Uri.parse("tel:" + contato.getTelefone()));  // pega o telefone do contato passado.
        startActivity(it);
    }

    /*
    Método responsável por enviar um sms. Vai receber um Contato como parâmetro e pegar seu número telefone.
    */
    public void enviarSms(Contatos contato) {

        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse("sms:" + contato.getTelefone()));
        it.putExtra("sms_body", "Oi " + contato.getNome());
        startActivity(it);

    }

    /*
    Método responsável por enviar email. Vai receber um Contato como parâmetro e pegar seu email cadastrado.
    */
    public void enviarEmail(Contatos contato) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, contato.getEmail());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subtitulo");
        intent.putExtra(Intent.EXTRA_TEXT, "Mensagem.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    /*
    Método responsável por mostrar a localizaçao do contato. Vai receber um Contato como parâmetro e pegar seu endereço.
    */
    public void mapaContato(Contatos contato) {

        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse("geo:0,0?q=" + contato.getEndereco()));
        it.putExtra("sms_body", "Menssagem ");
        startActivity(it);
    }

    /*
    Método que vai setar a foto quando o usuário der um click rápido. Vai receber o caminho da foto.
    Se o caminho for nulo significa que não foi tirado nehuma foto, então será setado com a foto padrão.
    Se o caminho for diferente de nulo significa que o usuario tirou a foto, então esta foto será setada.
     */
    public Bitmap verificaFoto2(String novoCaminho) {

        Bitmap bitmap;

        if (novoCaminho == null) {       // ver se o caminho da foto estar nulo se tiver vai setar a foto com a imagem drawable
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.foto);

        } else {
            bitmap = BitmapFactory.decodeFile(novoCaminho);  //se o caminho da foto nao tiver nulo vai setar a foto com ele(do banco)
        }

        Bitmap scale = Bitmap.createScaledBitmap(bitmap, 50, 50, true);  // vai definir o tamanho
        return scale;
    }

    /*
    Método do Seach da actionBar.
     */
    @Override
    public boolean onQueryTextSubmit(String text) {

        text = text.toLowerCase();

        for (Contatos contato : listContatos) {
            if (contato.getNome().contains(text)) {
                Toast.makeText(Tela2Activity.this, contato.getNome() + " Encontrado", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        Toast.makeText(Tela2Activity.this, "NAO ENCONTRADO", Toast.LENGTH_SHORT).show();
        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(Tela2Activity.this, "Nome: ", Toast.LENGTH_LONG).show();
        return true;
    }
}
