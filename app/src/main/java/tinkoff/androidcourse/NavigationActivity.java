package tinkoff.androidcourse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import tinkoff.androidcourse.model.PrefManager;

import static tinkoff.androidcourse.App.ARG_MENU_ID;
import static tinkoff.androidcourse.App.ARG_TITLE;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                DialogFragment.OnLoadChat {

    private final static int MENU_DIALOGS = 0;
    private final static int MENU_SETTINGS = 1;
    private final static int MENU_ABOUT = 2;

    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    private Toolbar mToolbar;
    private Fragment previousFragment;
    private Fragment prevFragment2;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
            previousFragment = getPreviousFragment();
            if (previousFragment != null) {
                String title = previousFragment.getArguments().getString(ARG_TITLE, null);
                int menu_id = previousFragment.getArguments().getInt(ARG_MENU_ID, -1);
                mToolbar.setTitle(title);
                switch(menu_id){
                    case MENU_DIALOGS: navigationView.getMenu().getItem(MENU_DIALOGS).setChecked(true); break;
                    case MENU_SETTINGS: navigationView.getMenu().getItem(MENU_SETTINGS).setChecked(true); break;
                    case MENU_ABOUT: navigationView.getMenu().getItem(MENU_ABOUT).setChecked(true); break;
                    default: navigationView.getMenu().getItem(MENU_DIALOGS).setChecked(true); break;
                }
            }else{
                mToolbar.setTitle(getString(R.string.menu_dialogs));
                navigationView.getMenu().getItem(MENU_DIALOGS).setChecked(true);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String fragment_title;
        switch (item.getItemId()) {
            case R.id.nav_dialogs:
                fragment_title = getString(R.string.menu_dialogs);
                addFragment(DialogFragment.newInstance(fragment_title,MENU_DIALOGS));
                break;
            case R.id.nav_settings:
                fragment_title = getString(R.string.menu_settings);
                addFragment(SettingsFragment.newInstance(fragment_title,MENU_SETTINGS));
                break;
            case R.id.nav_about:
                fragment_title = getString(R.string.menu_about);
                addFragment(AboutFragment.newInstance(fragment_title, MENU_ABOUT));
                break;
            case R.id.nav_logout:
                logOut();
                break;
            case R.id.nav_exit:
                finish();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_navigation);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, 0, 0);
        drawer.addDrawerListener(toggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigationView.getMenu().getItem(MENU_DIALOGS).setChecked(true);
            //otherwise it will show empty screen after BACK button press
            //onNavigationItemSelected(navigationView.getMenu().getItem(MENU_DIALOGS));
            previousFragment = DialogFragment.newInstance(getString(R.string.menu_dialogs),MENU_DIALOGS);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
            fragmentTransaction = fragmentTransaction.replace(
                    R.id.content_navigation, previousFragment, Integer.toString(getFragmentCount()));
            fragmentTransaction.commit();
        }

        //get EXTRA from LoginActivity
        String sLogin = "Login: ";
        Intent intent = getIntent();

        //we will get login from Intent
        if(intent.hasExtra(LoginActivity.EXTRA_LOGIN) && intent.getStringExtra(LoginActivity.EXTRA_LOGIN) != null){
            sLogin += intent.getStringExtra(LoginActivity.EXTRA_LOGIN);
        }

        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.textView);
        text.setText(sLogin);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
        //update title in toolbar
        mToolbar.setTitle(getString(R.string.menu_dialogs));
    }

    /** fragment replacement with animation */
    private void addFragment(Fragment fragment) {
        previousFragment = getSupportFragmentManager().findFragmentByTag("navigationFragmentTag");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
        fragmentTransaction = fragmentTransaction.replace(R.id.content_navigation, fragment, Integer.toString(getFragmentCount()));
        fragmentTransaction.addToBackStack(Integer.toString(getFragmentCount()));
        fragmentTransaction.commit();

        //currentFragment = fragment;
        mToolbar.setTitle(fragment.getArguments().getString(ARG_TITLE));
    }

    /** erase previous logged bool value (we need login value for re-enter) */
    private void logOut(){

        PrefManager.getInstance().saveLoggedIn(false);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /** replace fragment to Chat */
    public void startChatScreen(long pos){

        Bundle bundle = new Bundle();
        bundle.putLong(ChatFragment.ARG_POSITION, pos);

        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);

        //addToBackStack to correctly return to Dialogs
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction = fragmentTransaction.replace(R.id.content_navigation, chatFragment, "chatFragmentTag");
        fragmentTransaction.addToBackStack("showChat");
        fragmentTransaction.commit();
    }

    /** Activity DialogAdd will send the result back to Fragment through NavigationActivity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /** black Stack Overflow magic */
    protected int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }
    private Fragment getFragmentAt(int index) {
        return getFragmentCount() > 0 ? getSupportFragmentManager().findFragmentByTag(Integer.toString(index)) : null;
    }
    protected Fragment getCurrentFragment() {
        return getFragmentAt(getFragmentCount() - 1);
    }
    protected Fragment getPreviousFragment() {
        return getFragmentAt(getFragmentCount() - 2);
    }
}
