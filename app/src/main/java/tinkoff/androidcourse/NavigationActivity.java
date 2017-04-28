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

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                DialogFragment.OnLoadChat {

    private final static int MENU_DIALOGS = 0;
    private ActionBarDrawerToggle toggle;
    private Toolbar mToolbar;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dialogs:
                mToolbar.setTitle(getString(R.string.menu_dialogs));
                addFragment(DialogFragment.newInstance(""));
                break;
            case R.id.nav_settings:
                //StubFragment settingsFragment = StubFragment.newInstance("Настройки");
                mToolbar.setTitle(getString(R.string.menu_settings));
                addFragment(new SettingsFragment());
                break;
            case R.id.nav_about:
                mToolbar.setTitle(getString(R.string.menu_about));
                addFragment(AboutFragment.newInstance(""));
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigationView.getMenu().getItem(MENU_DIALOGS).setChecked(true);
            //otherwise it will show empty screen after BACK button press
            //onNavigationItemSelected(navigationView.getMenu().getItem(MENU_DIALOGS));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
            fragmentTransaction = fragmentTransaction.replace(R.id.content_navigation, DialogFragment.newInstance(""), "navigationFragmentTag");
            fragmentTransaction.commit();
        }

        /*
        mSharedPreferences = getApplicationContext()
                .getSharedPreferences(LoginActivity.PREFERENCES_FILENAME, Context.MODE_PRIVATE);

        //get EXTRA from LoginActivity
        String sLogin = "Login: ";

        Intent intent = getIntent();

        //we can get login from Intent or SharedPreferences
        if(intent.getStringExtra(LoginActivity.EXTRA_LOGIN) != null){
            sLogin += intent.getStringExtra(LoginActivity.EXTRA_LOGIN);
        }else{
            sLogin += mSharedPreferences.getString(LoginActivity.EXTRA_LOGIN, "NaN");
        }
        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.textView);
        text.setText(sLogin);
        */
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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left);
        fragmentTransaction = fragmentTransaction.replace(R.id.content_navigation, fragment, "navigationFragmentTag");
        fragmentTransaction.addToBackStack("navigationFragmentTag");
        fragmentTransaction.commit();
    }

    /** erase previous login values */
    private void logOut(){

        /*
        mSharedPreferences
                .edit()
                .putString(LoginActivity.EXTRA_LOGIN, "")
                .putString(LoginActivity.EXTRA_PASSW, "")
                .apply();
                */

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
}
