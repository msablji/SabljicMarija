package com.example.marij.sabljicmarija;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    Integer[] imageIDs = {
            R.id.img1,
            R.id.img2,
            R.id.img3,
            R.id.img4,
            R.id.img5,
            R.id.img6,
            R.id.img7,
            R.id.img8,
            R.id.img9,
            R.id.img10
    };
    int broj=1;
    int brojac=0;
    private AsyncTask MyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

        DBAdapter db = new DBAdapter(this);


        //---add a contact---

        db.open();
        long id = db.insertRokovnik("22.12.2013.", "16h", "Sastanak uprave");
        id = db.insertRokovnik("22.12.2013.", "17h", "Večera");
        id = db.insertRokovnik("23.12.2013.", "12h", "Ručak s investitorima");
        id = db.insertRokovnik("23.12.2013.", "18h", "Frizer");
        id = db.insertRokovnik("25.12.2013.", "16h", "Sastanak uprave");
        db.close();

        db.open();
        id = db.insertVrsta(2, 1);
        id = db.insertVrsta(3, 2);
        id = db.insertVrsta(1, 3);
        id = db.insertVrsta(5, 4);
        id = db.insertVrsta(2, 5);

       db.close();


        /*
        db.open();
        for(int i=1;i<300;i++)
        {
            db.deleteVrsta(i);
            db.deleteRokovnik(i);
        }
        db.close();
        */


        //--get all contacts---
        db.open();
       Cursor c = db.getAllRokovnik();
       if (c.moveToFirst())
        {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();


        //--
        db.open();
        Cursor ca = db.getAllVrsta();
        if (ca.moveToFirst())
        {
            do {
               DisplayAddress(ca);
            } while (c.moveToNext());
        }
        db.close();
        /*

        // ispis naziva događaja na zadani datum
        db.open();
        Cursor cz = db.getZadani("25.12.2013.");

        TextView a = (TextView)findViewById(R.id.dat);
        a.setText(cz.getString(2));

        db.close();
        */

       new DownloadImageTask().execute("https://files.vladstudio.com/joy/drums/wall/vladstudio_drums_240x160_signed.jpg",
        "https://files.vladstudio.com/joy/full_moon/wall/vladstudio_full_moon_204x160_signed.jpg",
        "https://files.vladstudio.com/joy/witch/wall/vladstudio_witch_240x160_signed.jpg",
        "https://files.vladstudio.com/joy/home/wall/vladstudio_home_240x160_signed.jpg",
        "https://files.vladstudio.com/joy/selfie/wall/vladstudio_selfie_240x160_signed.jpg",
        "https://files.vladstudio.com/joy/selfie_blue/wall/vladstudio_selfie_blue_240x160_signed.jpg",
        "https://files.vladstudio.com/joy/14_owls/wall/vladstudio_14_owls_240x160_signed.jpg",
       "https://files.vladstudio.com/joy/colin_huggins/wall/vladstudio_colin_huggins_240x160_signed.jpg",
        "https://files.vladstudio.com/joy/where_tahrs_live/wall/vladstudio_where_tahrs_live_240x160_signed.jpg",
        "https://files.vladstudio.com/joy/neptune_and_triton/wall/vladstudio_neptune_and_triton_240x160_signed.jpg");

        Gallery gallery = (Gallery) findViewById(R.id.gallery1);

        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id)
            {
                Toast.makeText(getBaseContext(),
                        "pic" + (position + 1) + " selected",
                        Toast.LENGTH_SHORT).show();

                //---display the images selected---
                ImageView imageView = (ImageView) findViewById(R.id.img1);
                imageView.setImageResource(imageIDs[position]);
            }
        });



    }
    public class ImageAdapter extends BaseAdapter
    {
        Context context;
        int itemBackground;

        public ImageAdapter(Context c)
        {
            context = c;
            //---setting the style---
            TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);

            itemBackground = a.getResourceId(
                    R.styleable.Gallery1_android_galleryItemBackground, 0);

            a.recycle();
        }

        //---returns the number of images---
        public int getCount() {
            return imageIDs.length;
        }


        //---returns the item---
        public Object getItem(int position) {
            return position;
        }

        //---returns the ID of an item---
        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setImageResource(imageIDs[position]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }
    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }
    private Bitmap DownloadImage(String URL)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        return bitmap;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            return DownloadImage(urls[brojac++]);
        }

        protected void onPostExecute(Bitmap result) {

            String naziv = "img" + broj;
            Log.d(naziv, "naziv");
             ImageView notes = (ImageView)findViewById(getResources().getIdentifier(naziv, "id", getPackageName()));

            notes.setImageBitmap(result);




           /* TextView[] textViews = new TextView[] {
                    (TextView)findViewById(R.id.text1),
                    (TextView)findViewById(R.id.text2),
                    (TextView)findViewById(R.id.text3),
                    (TextView)findViewById(R.id.text4),
                    (TextView)findViewById(R.id.text5),
                    (TextView)findViewById(R.id.text6),
                    (TextView)findViewById(R.id.text7),
                    (TextView)findViewById(R.id.text8),
                    (TextView)findViewById(R.id.text9),
                    (TextView)findViewById(R.id.text10)
            };
            textViews[broj].setText("some text");
            */
             broj=broj+1;
        }
    }
    public void brisanje(View view)
    {
        EditText a = (EditText) findViewById(R.id.obrisati);
        int number = Integer.parseInt(a.getText().toString());

        DBAdapter db = new DBAdapter(this);
        db.open();
        db.deleteRokovnik(number);
        db.deleteVrsta(number);
    }

    //funkcija za ispis
    public void DisplayContact(Cursor c)
    {

        Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Datum: " + c.getString(1) + "\n" +
                        "Termin:  " + c.getString(2)+ "\n" +
                        "Dogadjaj: " + c.getString(3)
                ,
                Toast.LENGTH_SHORT).show();
    }

    public void DisplayAddress(Cursor c)
    {
        Toast.makeText(this,
                "id_vrste: " + c.getString(0) + "\n" +
                        "kategorija: " + c.getString(1) + "\n" +
                        "id:  " + c.getString(2) ,

                Toast.LENGTH_LONG).show();
    }

}

