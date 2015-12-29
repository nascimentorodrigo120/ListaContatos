package Adapter_Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import exemplo.sqlite.R;

/**
 * Created by irom on 07/11/2015.
 */
public class DialogImagemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ItemDialog> list ;


    public DialogImagemAdapter(Context context, List<ItemDialog> list) {
        this.list = list;                                       //Itens do listview
        mInflater = LayoutInflater.from(context);              //Objeto responsavel por pegar o Layout do item.
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Componentes itemHolder;

        if (view == null) {    //se a view estiver nula (nunca criada) inflamos o layout nela.

            view = mInflater.inflate(R.layout.dialog_lista_image, null);  //infla o layout para podermos pegar as views

            //cria um item de suporte para nao precisarmos sempre inflar as mesmas informacoes
            itemHolder = new Componentes();
            itemHolder.txtNome = ((TextView) view.findViewById(R.id.textViewNomeDialog));
            itemHolder.imgIcon = ((ImageView) view.findViewById(R.id.imageView2));

            view.setTag(itemHolder);    //define os itens na view;

        } else {
            itemHolder = (Componentes) view.getTag();    //se a view ja existe pega os itens.
        }



        ItemDialog item = list.get(position);        //pega os dados da lista e define os valores nos itens.
        itemHolder.txtNome.setText(item.getNome());
        itemHolder.imgIcon.setImageResource(item.getImagem());

        return view;            //retorna a view com as informacoes
    }


    private class Componentes {

        ImageView imgIcon;
        TextView txtNome;
    }
}
