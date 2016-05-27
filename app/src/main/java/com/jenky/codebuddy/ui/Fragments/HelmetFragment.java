package com.jenky.codebuddy.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jenky.codebuddy.R;
import com.jenky.codebuddy.adapters.ItemAdapter;
import com.jenky.codebuddy.models.Item;
import com.jenky.codebuddy.util.TestData;

import java.util.ArrayList;

/**
 * Created by Jason on 12-May-16.
 */
public class HelmetFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ItemAdapter itemAdapter;
    private ArrayList<Item> items = new ArrayList<>();
    private ListView resultListView;

    public static HelmetFragment newInstance() {
        return new HelmetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        resultListView = (ListView) view.findViewById(R.id.result_list_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemAdapter = new ItemAdapter(getContext(), R.layout.component_item, items);
        resultListView.setAdapter(itemAdapter);
        resultListView.setOnItemClickListener(this);
        items.clear();
        TestData.addTestHelmets(items);
        itemAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = items.get(position);
        purchaseItem(item);
    }

    private void purchaseItem(Item item) {
        //TODO Request Item Purchase
    }
}
