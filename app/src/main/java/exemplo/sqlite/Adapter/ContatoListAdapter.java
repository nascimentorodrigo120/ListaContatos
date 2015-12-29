package exemplo.sqlite.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import exemplo.sqlite.Contatos;
import exemplo.sqlite.R;

/**
 * Created by rodrigo on 03/11/2015.
 * adapter da Lista que vai mostrar todos os contatos cadastrados.
 */

  public class ContatoListAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<Contatos> listContatos;


    public ContatoListAdapter(Context context, List<Contatos> listContatos) {
        this.listContatos = listContatos;                             //Itens do listview
        mInflater = LayoutInflater.from(context);                     //Objeto responsavel por pegar o Layout do item.
    }


    @Override
    public int getCount() {
        return listContatos.size();
    }

    @Override
    public Contatos getItem(int position) {
        return listContatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ItemSuporte itemHolder;

        if (view == null) {    //se a view estiver nula (nunca será criada), inflamos o layout nela.

            view = mInflater.inflate(R.layout.item_list, null);  //infla o layout para podermos pegar as views

            //cria um item de suporte para nao precisarmos sempre inflar as mesmas informacões
            itemHolder = new ItemSuporte();
            itemHolder.txtTitle = ((TextView) view.findViewById(R.id.text));
            itemHolder.imgIcon = ((ImageView) view.findViewById(R.id.imagemview));

            view.setTag(itemHolder);    //define os itens na view;

        } else {
            itemHolder = (ItemSuporte) view.getTag();    //se a view ja existe pega os itens.
        }

        //pega os dados da lista e define os valores nos itens.
        Contatos contato = listContatos.get(position);
        itemHolder.txtTitle.setText(contato.getNome());

         if (contato.getFoto() != null) {    // ver se getfoto() do banco nao estar nulo e seta a foto da listView

             try {
                 Bitmap bitmap = BitmapFactory.decodeFile(contato.getFoto());
                 Bitmap scale = Bitmap.createScaledBitmap(bitmap, 40, 40, true);   // defini o tamanho da imagem depois seta a imagem.
                 itemHolder.imgIcon.setImageBitmap(scale);

            }catch(NullPointerException e){
             itemHolder.imgIcon.setImageResource(R.drawable.foto); }

         }
         else{
             itemHolder.imgIcon.setImageResource(R.drawable.foto); // se caso a getFoto() do banco tiver nulo sera setado com o drawable
         }
        return view;
    }


    private class ItemSuporte {

        ImageView imgIcon;
        TextView txtTitle;
    }

}
