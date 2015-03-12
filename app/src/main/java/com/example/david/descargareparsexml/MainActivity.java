package com.example.david.descargareparsexml;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String ns = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class Emp_row {
        public final String apellido;
        public final String salario;

        private Emp_row(String apellido, String salario) {
            this.apellido = apellido;
            this.salario = salario;
        }
    }

    public void parseandoClick(View v){
        try {
            Toast toast=Toast.makeText(this, "Chega1", Toast.LENGTH_SHORT);
            toast.show();



            InputStream is=getAssets().open("empleados.xml");
            List<Emp_row> lista=parse(is);
            TextView tv=(TextView) findViewById(R.id.textView);
            tv.setText(""+lista.size());

            //Adaptamos a lista para un listview
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
            for(Emp_row emp: lista) {
                arrayAdapter.add(emp.apellido);
                arrayAdapter.add(emp.salario);
            }
            ListView lv=(ListView) findViewById(R.id.listView);
            lv.setAdapter(arrayAdapter);

            Toast toast2=Toast.makeText(this, "Chega2", Toast.LENGTH_SHORT);
            toast2.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            Toast toast2=Toast.makeText(this, "Chega3", Toast.LENGTH_SHORT);
            toast2.show();
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readEmpleados(parser);
        } finally {
            in.close();
        }
    }

    private List readEmpleados(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();
        Toast toast2=Toast.makeText(this, "Chega4", Toast.LENGTH_SHORT);
        toast2.show();
        parser.require(XmlPullParser.START_TAG, ns, "EMPLEADOS");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("EMP_ROW")) {
                entries.add(readEmp_row(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Emp_row readEmp_row(XmlPullParser parser) throws XmlPullParserException, IOException {
        Toast toast2=Toast.makeText(this, "Chega5", Toast.LENGTH_SHORT);
        toast2.show();
        parser.require(XmlPullParser.START_TAG, ns, "EMP_ROW");
        String apellido = null;
        String salario = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("APELLIDO")) {
                apellido = readApellido(parser);
            } else if (name.equals("SALARIO")) {
                salario = readSalario(parser);
            } else {
                skip(parser);
            }
        }
        return new Emp_row(apellido, salario);
    }

    // Processes title tags in the feed.
    private String readApellido(XmlPullParser parser) throws IOException, XmlPullParserException {
        Toast toast2=Toast.makeText(this, "Chega6", Toast.LENGTH_SHORT);
        toast2.show();
        parser.require(XmlPullParser.START_TAG, ns, "APELLIDO");
        String apellido = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "APELLIDO");
        return apellido;
    }

    // Processes summary tags in the feed.
    private String readSalario(XmlPullParser parser) throws IOException, XmlPullParserException {
        Toast toast2=Toast.makeText(this, "Chega7", Toast.LENGTH_SHORT);
        toast2.show();
        parser.require(XmlPullParser.START_TAG, ns, "SALARIO");
        String salario = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "SALARIO");
        return salario;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        Toast toast2=Toast.makeText(this, "Chega8", Toast.LENGTH_SHORT);
        toast2.show();
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        Toast toast2=Toast.makeText(this, "Chega9", Toast.LENGTH_SHORT);
        toast2.show();
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
