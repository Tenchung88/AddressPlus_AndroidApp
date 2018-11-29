package example.com.assignment2_part2addressplus;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends  ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "MyAddressPlus";
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private SimpleCursorAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"TENZIN CHOZOM 2709999");
        this.getListView().setDividerHeight(2);
        fillData();
        registerForContextMenu(getListView());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_id:
                createTodo();
                return true;
        case R.id.about_id:
              Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();//?????????
                Uri uri = Uri.parse(MyAdressplusContentProvider.CONTENT_URI + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createTodo() {
        Intent i = new Intent(this, EditAddressdetailActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);//????????????????
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EditAddressdetailActivity.class);
        Uri AddressUri = Uri.parse(MyAdressplusContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MyAdressplusContentProvider.CONTENT_ITEM_TYPE, AddressUri);//what it pass to next activity with CONTent_item_type which has one row

        // Activity returns an result if called with startActivityForResult
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) // when it called???????
    {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void fillData() {

        String[] from = new String[] { AddressTableHandler.COLUMN_FIRSTNAME };
        int[] to = new int[] { R.id.label};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.address_row, null, from, to, 0);//?????????????

        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }


    //@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) //what does cursorloader do????
    {
        String[] projection = { AddressTableHandler.COLUMN_ID, AddressTableHandler.COLUMN_FIRSTNAME };
        CursorLoader cursorLoader = new CursorLoader(this, MyAdressplusContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    //@Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    //@Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

}
