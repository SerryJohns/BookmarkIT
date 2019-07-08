package com.andela.bookmarkit.ui.cities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.andela.bookmarkit.MainActivity;
import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.local.model.City;
import com.andela.bookmarkit.ui.base.BaseFragment;
import com.andela.bookmarkit.ui.base.CustomViewModelFactory;

import java.util.ArrayList;
import java.util.List;


public class CitiesFragment extends BaseFragment {
    private Toolbar toolbar;
    private RecyclerView citiesRecyclerView;
    private CitiesAdapter citiesAdapter;
    private List<City> citiesList = new ArrayList<>();
    private TextView txtNoContent;

    private CustomViewModelFactory viewModelFactory;
    private CitiesFragmentViewModel viewModel;

    public CitiesFragment() {
        // Required empty public constructor
    }

    public static CitiesFragment newInstance() {
        return new CitiesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModelFactory = new CustomViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CitiesFragmentViewModel.class);

        initViews();
        initAppBar();
        loadCities();
    }

    private void initViews() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        txtNoContent = (TextView) getActivity().findViewById(R.id.txt_no_content);
        citiesRecyclerView = (RecyclerView) getActivity().findViewById(R.id.cities_recycler_view);

        citiesAdapter = new CitiesAdapter(citiesList, onItemClickListener);
        citiesRecyclerView.setAdapter(citiesAdapter);
    }

    private CitiesAdapter.OnItemClickListener onItemClickListener = new CitiesAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(City city) {
            MainActivity activity = (MainActivity) getActivity();
            activity.fragmentSwitcher.showDetailsFragment(city);
        }
    };

    private void initAppBar() {
        MainActivity activity = (MainActivity) getActivity();

        setHasOptionsMenu(true);
        toolbar.setTitle(R.string.title_bookmarked_cities);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadCities() {
        viewModel.getCities().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cities) {
                if (!cities.isEmpty()) {
                    txtNoContent.setVisibility(View.GONE);
                    citiesList.clear();
                    citiesList.addAll(cities);
                    citiesAdapter.updateRecyclerView();
                } else {
                    txtNoContent.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cities_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryRefinementEnabled(true);
            searchView.setIconifiedByDefault(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
