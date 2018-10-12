package starlabs.noticeboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BaseFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        addTab();

        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }

    private void addTab() {

        TabLayout.Tab csTab = tabLayout.newTab();
        csTab.setText("CSE");
        csTab.setIcon(R.drawable.cse);
        tabLayout.addTab(csTab);
        

        TabLayout.Tab ecTab = tabLayout.newTab();
        ecTab.setText("ECE");
        ecTab.setIcon(R.drawable.ece);
        tabLayout.addTab(ecTab);

        TabLayout.Tab meTab = tabLayout.newTab();
        meTab.setText("Mechanical");
        meTab.setIcon(R.drawable.me);
        tabLayout.addTab(meTab);

        TabLayout.Tab eeTab = tabLayout.newTab();
        eeTab.setText("Electrical");
        eeTab.setIcon(R.drawable.power);
        tabLayout.addTab(eeTab);


        TabLayout.Tab ceTab = tabLayout.newTab();
        ceTab.setText("Civil");
        ceTab.setIcon(R.drawable.ce);
        tabLayout.addTab(ceTab);


    }
}

