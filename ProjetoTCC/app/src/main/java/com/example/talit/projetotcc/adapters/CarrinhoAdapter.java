package com.example.talit.projetotcc.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talit.projetotcc.R;
import com.example.talit.projetotcc.activities.Carrinho;
import com.example.talit.projetotcc.activities.DetalhesProdutos;
import com.example.talit.projetotcc.logicalView.Produtos;
import com.example.talit.projetotcc.sqlight.DbConn;

import java.util.List;

/**
 * Created by talit on 12/06/2017.
 */

public class CarrinhoAdapter extends BaseAdapter {
    Activity act;
    Context c;
    List<Produtos> prods;
    LayoutInflater inflater;
    private DbConn dbconn;

    public CarrinhoAdapter(Activity act, Context c, List<Produtos> prods){
        this.act = act;
        this.c = c;
        this.prods = prods;
        this.inflater  = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return prods.size();
    }

    @Override
    public Object getItem(int position) {
        return prods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CarrinhoAdapter.ViewHolder holder;

        if(view == null){
            holder = new CarrinhoAdapter.ViewHolder();
            view = this.inflater.inflate(R.layout.card_view_produto_carrinho, parent, false);
            holder.txtNome = (TextView)view.findViewById(R.id.txtNomeProduto);
            holder.txtMarca = (TextView)view.findViewById(R.id.txt_marca_prod);
            holder.txtPreco = (TextView)view.findViewById(R.id.txt_preco);
            holder.imagem = (ImageView)view.findViewById(R.id.im_logo_produto);
            holder.btnExcluir= (Button)view.findViewById(R.id.btnExcluir);
            holder.txtCod = (TextView)view.findViewById(R.id.txt_cod);
            view.setTag(holder);

        }
        else{
            holder = (CarrinhoAdapter.ViewHolder) view.getTag();
        }

        final Produtos produtos = prods.get(position);
       /*ver depois
        holder.txtNome.setText(produtos.getNome());
        holder.txtMarca.setText(produtos.getMarca());
        holder.txtPreco.setText("R$ " + produtos.getPreco());
        holder.txtCod.setText(""+ produtos.getCodReferencia());*/
        //holder.imagem.setImageBitmap(DetalhesProdutos.b);
        //holder.imagem.setBackgroundResource(produtos.getIdImagem());
        holder.btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(c);
                final View alertLayout = inflater.inflate(R.layout.custom_alert_dialog_edit_carrinho, null);
                final Button excluir = (Button) alertLayout.findViewById(R.id.btn_excluir);
                final Button editar = (Button) alertLayout.findViewById(R.id.btn_editar);

                AlertDialog.Builder alerta = new AlertDialog.Builder(Carrinho.context);
                alerta.setView(alertLayout);
                alerta.setCancelable(false);
                final AlertDialog dialogo = alerta.create();
                //dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogo.show();

                excluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*ver depois
                        dbconn = new DbConn(Carrinho.act);
                        dbconn.deleteCarrinhoId(produtos.getCodReferencia());
                        CarrinhoAdapter carAdapter = new CarrinhoAdapter(Carrinho.act, Carrinho.context, dbconn.selectProutos());
                        Carrinho.listas.setAdapter(carAdapter);
                        Carrinho.listas.deferNotifyDataSetChanged();
                        Carrinho.txtValorTotal.setText("R$ " + dbconn.totalCarrinho());
                        Carrinho.txtQtd.setText(""+ dbconn.totalItensCarrinho());

                        if(Carrinho.listas.getCount() == 0){
                            Carrinho.no_list.setVisibility(View.VISIBLE);
                            Carrinho.cardFinal.setVisibility(View.INVISIBLE);
                            Carrinho.btnFinal.setVisibility(View.INVISIBLE);
                        }*/

                        dialogo.dismiss();
                    }
                });
                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogo.dismiss();
                    }
                });

               /* String nomeRemov;
                nomeRemov = prods.get(position).getNome();
                Intent intent = new Intent();
                intent.setClass(c,Carrinho.class);
                intent.putExtra("NOME_DEL",nomeRemov);
                act.startActivity(intent);8/


                Toast.makeText(Carrinho.context, produtos.getCodReferencia(), Toast.LENGTH_SHORT).show();
                /*Toast.makeText(Carrinho.context, produtos.getNome(), Toast.LENGTH_SHORT).show();
                DbConn dbconn = new DbConn(Carrinho.context);
                Toast.makeText(Carrinho.context, dbconn.selectIdProduto(produtos.getNome()).getCodReferencia()+"", Toast.LENGTH_SHORT).show();
                dbconn.deleteCarrinhoId(dbconn.selectIdProduto(produtos.getNome()));*/
            }
        });

        return view;
    }
    private class ViewHolder {

        private TextView txtNome;
        private TextView txtMarca;
        private TextView txtPreco;
        private ImageView imagem;
        private Button btnExcluir;
        private TextView txtCod;
    }

    public void mostarOpções() {

        LayoutInflater inflater = LayoutInflater.from(c);
        final View alertLayout = inflater.inflate(R.layout.custom_alert_dialog_edit_carrinho, null);
        final Button excluir = (Button) alertLayout.findViewById(R.id.btn_excluir);
        final Button editar = (Button) alertLayout.findViewById(R.id.btn_editar);

        AlertDialog.Builder alerta = new AlertDialog.Builder(Carrinho.context);
        alerta.setView(alertLayout);
        alerta.setCancelable(false);
        final AlertDialog dialogo = alerta.create();
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.show();

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogo.dismiss();
            }
        });
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogo.dismiss();
            }
        });
    }
}
