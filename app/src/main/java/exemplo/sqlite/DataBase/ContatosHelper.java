package exemplo.sqlite.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import exemplo.sqlite.Contatos;

/**
 * Created by rodrigo on 03/11/2015.
 */
public class ContatosHelper extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String TABELA = "contatos";
    private static final String DATABASE = "MPContatos";
    private static final String TAG = "CADASTRO_CONTATOS";


    public ContatosHelper(Context context) {
        super(context, DATABASE, null, VERSAO);
    }


    public ContatosHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String criar = "CREATE TABLE " + TABELA + "( id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, endereco TEXT, telefone TEXT, email TEXT, foto TEXT )";
        db.execSQL(criar);
    }

    /*
    Vai apagar a tabela, depois recria-lá.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String deletar = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(deletar);           // apaga o banco se existir

        onCreate(db);                  // recriar de novo o banco
    }


    /*
    Recebe um objeto Contatos de onde será extraído os dados, que serão setados dentro do values, que por sua vez será usado com
    parâmetro no metodo insert().
     */
    public void cadastra(Contatos contato) {

        ContentValues values = new ContentValues();

        values.put("nome", contato.getNome());
        values.put("endereco", contato.getEndereco());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        values.put("foto", contato.getFoto());

        getWritableDatabase().insert(TABELA, null, values);
        close();
        Log.i(TAG, "contato cadastrado: " + contato.getNome());
    }


    /*
    Vai retornar uma lista de Contatos.
     */
    public List<Contatos> carregaContatos() {

        List<Contatos> lista = new ArrayList<Contatos>();

        String pegar = "Select * from Contatos order by nome";

        Cursor cursor = getReadableDatabase().rawQuery(pegar, null);

        try {
            while (cursor.moveToNext()) {

                Contatos contato = new Contatos();

                contato.setId(cursor.getLong(0));
                contato.setNome(cursor.getString(1));
                contato.setEndereco(cursor.getString(2));
                contato.setTelefone(cursor.getString(3));
                contato.setEmail(cursor.getString(4));
                contato.setFoto(cursor.getString(5));

                lista.add(contato);
            }

        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cursor.close();
        }
        close();
        return lista;
    }

    /*
    Recebe um objeto Contatos, onde desse objeto será extraído seu id, que será usado como parâmetro no método delete().
     */
    public void excluir(Contatos contato) {

        String id = String.valueOf(contato.getId());
        String[] args = {id};

        getWritableDatabase().delete(TABELA, "id=?", args);

        Log.i(TAG, "contato deletado " + contato.getNome());
    }

    /*
    Recebe um objeto Contatos com os dados atualizados, e chama o método Update() que vai atualizar o banco.
     */
    public void alterar(Contatos contato) {

        ContentValues values = new ContentValues();

        values.put("nome", contato.getNome());
        values.put("endereco", contato.getEndereco());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        values.put("foto", contato.getFoto());


        String id = String.valueOf(contato.getId());
        String[] args = {id};

        getWritableDatabase().update(TABELA, values, "id=?", args);
        Log.i(TAG, "contato Alterado: " + contato.getNome());
    }
}
