package example.com.assignment2_part2addressplus;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditAddressdetailActivity extends Activity {
    private Spinner designation;
    private EditText firstname;
    private EditText lastname;
    private EditText Address;
    private Spinner province;
    private EditText country;
    private EditText postalcode;
    private Uri MyAddressUri;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.edit_address);
        designation = (Spinner) findViewById(R.id.SpinnerDesignation_id);
        firstname= (EditText) findViewById(R.id.firstName_id);
        lastname = (EditText) findViewById(R.id.LastName_id);
        Address = (EditText) findViewById(R.id.Address_id);
        province = (Spinner) findViewById(R.id.SpinnerProvince_id);
        country = (EditText) findViewById(R.id.country_id);
        postalcode = (EditText) findViewById(R.id.postalcode_id);
        Button confirmButton = (Button) findViewById(R.id.confirm_id);
        Bundle extras = getIntent().getExtras();
        MyAddressUri = (bundle == null) ? null : (Uri) bundle.getParcelable(MyAdressplusContentProvider.CONTENT_ITEM_TYPE);
        if (extras != null) {
            MyAddressUri = extras.getParcelable(MyAdressplusContentProvider.CONTENT_ITEM_TYPE);
            fillData(MyAddressUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ((TextUtils.isEmpty(firstname.getText().toString())) || (TextUtils.isEmpty(lastname.getText().toString()))
                        || (TextUtils.isEmpty(Address.getText().toString())) || (TextUtils.isEmpty(country.getText().toString()))
                        || (TextUtils.isEmpty(postalcode.getText().toString()))){
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    private void fillData(Uri uri) {
        String[] projection = { AddressTableHandler.COLUMN_DESIGNATION, AddressTableHandler.COLUMN_FIRSTNAME, AddressTableHandler.COLUMN_LASTNAME,AddressTableHandler.COLUMN_ADDRESS,
                AddressTableHandler.COLUMN_PROVINCE,AddressTableHandler.COLUMN_COUNTRY,AddressTableHandler.COLUMN_POSTALCODE};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.COLUMN_DESIGNATION));

            for (int i = 0; i < designation.getCount(); i++) {

                String s = (String) designation.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    designation.setSelection(i);
                }
            }
            firstname.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.COLUMN_FIRSTNAME)));
            lastname.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.COLUMN_LASTNAME)));
            Address.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.COLUMN_ADDRESS)));
            String myprovince = cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.COLUMN_PROVINCE));
            for (int i = 0; i < province.getCount(); i++) {

                String s = (String) province.getItemAtPosition(i);
                if (s.equalsIgnoreCase(myprovince)) {
                    province.setSelection(i);
                }
            }

            country.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.COLUMN_COUNTRY)));
            postalcode.setText(cursor.getString(cursor.getColumnIndexOrThrow(AddressTableHandler.COLUMN_POSTALCODE)));

            // Always close the cursor
            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyAdressplusContentProvider.CONTENT_ITEM_TYPE, MyAddressUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    private void saveState() {
        String mydesignation = (String) designation.getSelectedItem();
        String myfirstname = firstname.getText().toString();
        String mylastname = lastname.getText().toString();
        String myaddress = Address.getText().toString();
        String myprovince = (String) province.getSelectedItem();
        String mycountry = country.getText().toString();
        String mypostalcode = postalcode.getText().toString();


       if (myfirstname.length() == 0 || mylastname.length() == 0 || myaddress.length() ==0 || mycountry.length() == 0
               || mypostalcode.length()==0 ) {
            makeToast();
        }

        ContentValues values = new ContentValues();
        values.put(AddressTableHandler.COLUMN_DESIGNATION, mydesignation);
        values.put(AddressTableHandler.COLUMN_FIRSTNAME, myfirstname);
        values.put(AddressTableHandler.COLUMN_LASTNAME, mylastname);
        values.put(AddressTableHandler.COLUMN_ADDRESS, myaddress);
        values.put(AddressTableHandler.COLUMN_PROVINCE, myprovince);
        values.put(AddressTableHandler.COLUMN_COUNTRY, mycountry);
        values.put(AddressTableHandler.COLUMN_POSTALCODE, mypostalcode);



        if (MyAddressUri == null) {
            // New ToDo
            MyAddressUri = getContentResolver().insert(MyAdressplusContentProvider.CONTENT_URI, values);
        } else {
            // Update ToDo
            getContentResolver().update(MyAddressUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(EditAddressdetailActivity.this, "INCOMPLETE!! ",Toast.LENGTH_LONG).show();
    }
}
