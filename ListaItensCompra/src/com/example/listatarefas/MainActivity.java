package com.example.listatarefas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private List<ItemCompra> listaCompras = new ArrayList<ItemCompra>();
	
	private String nomeSelecionado;
	private int indiceSelecionado;
	
	private ArrayAdapter<ItemCompra> adapter;
	
	private ProgressDialog pDialogList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		pDialogList = ProgressDialog.show(this, "Aviso", "Buscando itens. Por favor aguarde.");
		
		AQuery aq = new AQuery(this);
		aq.ajax("http://1-dot-listacomprasartigo.appspot.com/listItensJson", JSONObject.class, new AjaxCallback<JSONObject>(){

			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				try {
					JSONArray itens = object.getJSONArray("itens");
					for (int i = 0; i < itens.length(); i++){
						JSONObject item = itens.getJSONObject(i);
						ItemCompra itemCompra = new ItemCompra();
						itemCompra.setComprado(item.getBoolean("comprado"));
						itemCompra.setNome(item.getString("nome"));
						itemCompra.setQuantidade(item.getLong("quantidade"));
						listaCompras.add(itemCompra);
					}
					
					adapter = new ArrayAdapter<ItemCompra>(MainActivity.this, android.R.layout.simple_list_item_1, listaCompras);
					setListAdapter(adapter);
					pDialogList.dismiss();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		nomeSelecionado = listaCompras.get(position).getNome();
		indiceSelecionado = position;
		showDialog(1);
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("A compra deste item já foi efetuada?");
		b.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final ProgressDialog pDialogInsert = ProgressDialog.show(MainActivity.this, "Aviso", "Deletando tarefa. Por favor aguarde!");
				
				AQuery aq = new AQuery(MainActivity.this);
				aq.ajax("http://1-dot-listacomprasartigo.appspot.com/deleteItem?nome="+nomeSelecionado, JSONObject.class, new AjaxCallback<JSONObject>(){
					@Override
					public void callback(String url, JSONObject object, AjaxStatus status) {
						try {
							int statusRetornado = object.getInt("status");
							if (statusRetornado == 1){
								Toast.makeText(MainActivity.this, "Tarefa concluída com sucesso!", Toast.LENGTH_LONG).show();
							}
							
							listaCompras.remove(indiceSelecionado);
							adapter.notifyDataSetChanged();
							pDialogInsert.dismiss();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		b.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		return b.create();
	}
}
