package zhou.gank.io.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.data.DataManager
import zhou.gank.io.data.TimeProvider
import zhou.gank.io.model.Gank
import zhou.gank.io.model.GankDaily
import zhou.gank.io.model.ResultDaily
import zhou.gank.io.ui.adapter.DailyAdapter
import zhou.gank.io.util.JsonKit
import zhou.gank.io.util.TimeKit

@CompileStatic
class DailyFragment extends BaseFragment {

    ImageView icon;
    RecyclerView recyclerView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TimeProvider provider
    DailyAdapter dailyAdapter;
    int year, month, day


    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        def b = getArguments()
        List<Integer> time = TimeKit.getTime()
        year = time[0]
        month = time[1]
        day = time[2]
        if (b) {
            year = b.getInt(Config.Static.YEAR)
            month = b.getInt(Config.Static.MONTH)
            day = b.getInt(Config.Static.DAY)
        }

        provider = new TimeProvider(year, month, day)
    }

    @Nullable
    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        icon = view.findViewById(R.id.icon) as ImageView

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_48px);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        String title = getString(R.string.app_name);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);

        dailyAdapter = new DailyAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()))
        recyclerView.setAdapter(dailyAdapter)

        DataManager.getInstance().get(provider, this.&setUpData)

        return view;
    }

    protected void setUpData(GankDaily daily) {
        if (daily) {
            if (daily.isEmpty()) {
                Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show()
            } else {
                List<List<Gank>> ganks = daily.ganks
                List<String> types = daily.types
                Picasso.with(getActivity()).load(ganks.get(types.indexOf(Config.Type.WELFARE)).get(0).url).into(icon)

                dailyAdapter.setDaily(daily)
            }
        } else {
            Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show()
        }
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}