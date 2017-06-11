package kris.hazen.dev.fulliot.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import kris.hazen.dev.fulliot.Fragments.AccountFragment;
import kris.hazen.dev.fulliot.Fragments.FragmentDrawer;
import kris.hazen.dev.fulliot.Fragments.HomeFragment;
import kris.hazen.dev.fulliot.Fragments.LibraryFragment;
import kris.hazen.dev.fulliot.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new AccountFragment();
                title = getString(R.string.title_account);
                break;
            case 2:
                fragment = new LibraryFragment();
                title = getString(R.string.title_library);
                break;
            case 3:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                // set title
                alertDialogBuilder.setTitle(getResources().getString(R.string.nav_item_logout));
                // set dialog message
                alertDialogBuilder
                        .setMessage(getResources().getString(R.string.areYouSure))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.option_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.option_setting){
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
