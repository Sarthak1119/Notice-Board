package starlabs.noticeboard;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class BaseFragmentAdapter extends FragmentPagerAdapter {
    public BaseFragmentAdapter(FragmentManager fm, int tabCount) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                CsFragment csFragment=new CsFragment();
                return csFragment;

            }
            case 1:{
                EcFragment ecFragment=new EcFragment();
                return ecFragment;
            }
            case 2:{
                MeFragment meFragment=new MeFragment();
                return meFragment;
            }
            case 3:{
                EeFragment eeFragment=new EeFragment();
                return eeFragment;
            }
            case 4:{
                CeFragment ceFragment=new CeFragment();
                return ceFragment;

            }

            default:{
                return null;
            }
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}


