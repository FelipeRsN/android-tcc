package com.example.talit.projetotcc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talit.projetotcc.R;
import com.example.talit.projetotcc.activities.DetalhesProdutos;
import com.example.talit.projetotcc.activities.PaginaInicialEstabelecimentos;
import com.example.talit.projetotcc.activities.ProdutosEstabelecimento;
import com.example.talit.projetotcc.connectionAPI.LotePorCategoria;
import com.example.talit.projetotcc.fragments.TabCategorias;
import com.example.talit.projetotcc.fragments.TabDestaques;
import com.example.talit.projetotcc.logicalView.CategoriasProdutos;
import com.example.talit.projetotcc.logicalView.Produtos;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.talit.projetotcc.fragments.TabDestaques.activity;

/**
 * Created by talit on 11/06/2017.
 */

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriasAdapterHolder>{

    public static class CategoriasAdapterHolder extends RecyclerView.ViewHolder  {
        TextView nomeCategoria;
        ImageView imCategorias;
        private View view;

        public CategoriasAdapterHolder(View v) {
            super(v);
            nomeCategoria = (TextView)v.findViewById(R.id.txtNomeCategoria);
            imCategorias = (ImageView) v.findViewById(R.id.img_categorias);
            view = v;
        }
    }

    private Activity act;
    private List<CategoriasProdutos> categProd;
    private View v;

    public CategoriasAdapter(Activity act, List<CategoriasProdutos> categ){
        this.act = act;
        this.categProd = categ;
    }
    @Override
    public CategoriasAdapter.CategoriasAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_categorias, parent, false);
        v = itemView;
        return new CategoriasAdapter.CategoriasAdapterHolder (itemView);
    }

    @Override
    public void onBindViewHolder(CategoriasAdapterHolder holder, int position) {
        final CategoriasProdutos categs = categProd.get(position);
        holder.nomeCategoria.setText(categs.getDescricaoCategoria());

        try{
            holder.imCategorias.setImageBitmap(convert(categs.getImagem64()));
        }catch (Exception e){
            e.printStackTrace();
            holder.imCategorias.setImageResource(R.drawable.errorcategoria);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabDestaques.idMarca = null;
                TabDestaques.idSubCateg= null;
                TabDestaques.idCateg= null;
                TabDestaques.idCateg = categs.getIdCategoria()+"";
                ((FragmentActivity)TabDestaques.context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new TabDestaques())
                        .commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return categProd.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                    base64Str.substring(base64Str.indexOf(",") + 1),
                    Base64.DEFAULT
            );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
