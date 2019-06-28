package com.example.siditoure.androidtranscription;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExplorateurActivity extends ListActivity implements OnSharedPreferenceChangeListener {
    /**
     * ReprÈsente le texte qui s'affiche quand la liste est vide
     */
    private TextView mEmpty = null;
    /**
     * La liste qui contient nos fichiers et rÈpertoires
     */
    private ListView mList = null;
    /**
     * Notre Adapter personnalisÈ qui lie les fichiers ‡ la liste
     */
    private FileAdapter mAdapter = null;

    /**
     * ReprÈsente le rÈpertoire actuel
     */
    private File mCurrentFile = null;
    /**
     * Couleur voulue pour les rÈpertoires
     */
    private int mColor = 0;
    /**
     * Indique si l'utilisateur est ‡ la racine ou pas
     * pour savoir s'il veut quitter
     */
    private boolean mCountdown = false;

    /**
     * Les prÈfÈrences partagÈes de cette application
     */
    private SharedPreferences mPrefs = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorateur);

        // On rÈcupËre la ListView de notre activitÈ
        mList = (ListView) getListView();

        // On vÈrifie que le rÈpertoire externe est bien accessible
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // S'il ne l'est pas, on affiche un message
            mEmpty = (TextView) mList.getEmptyView();
            mEmpty.setText("Vous ne pouvez pas accÈder aux fichiers");
        } else {
            // S'il l'est...
            // On dÈclare qu'on veut un menu contextuel sur les ÈlÈments de la liste
            registerForContextMenu(mList);

            // On rÈcupËre les prÈfÈrences de l'application
            mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            // On indique que l'acitivtÈ est ‡ l'Ècoute des changements de prÈfÈrence
            mPrefs.registerOnSharedPreferenceChangeListener(this);
            // On rÈcupËre la couleur voulue par l'utilisateur, par dÈfaut il s'agira du rouge
            mColor = mPrefs.getInt("repertoireColorPref", Color.RED);

            // On rÈcupËre la racine de la carte SD pour qu'elle soit
            mCurrentFile = Environment.getExternalStorageDirectory();

            // On change le titre de l'activitÈ pour y mettre le chemin actuel
            setTitle(mCurrentFile.getAbsolutePath());

            // On rÈcupËre la liste des fichiers dans le rÈpertoire actuel
            File[] fichiers = mCurrentFile.listFiles();

            // On transforme le tableau en une structure de donnÈes de taille variable
            ArrayList<File> liste = new ArrayList<File>();
            for(File f : fichiers)
                liste.add(f);

            mAdapter = new FileAdapter(this, android.R.layout.simple_list_item_1, liste);
            // On ajoute l'adaptateur ‡ la liste
            mList.setAdapter(mAdapter);
            // On trie la liste
            mAdapter.sort();

            // On ajoute un Listener sur les items de la liste
            mList.setOnItemClickListener(new OnItemClickListener() {

                // Que se passe-il en cas de cas de clic sur un ÈlÈment de la liste ?
                public void onItemClick(AdapterView<?> adapter, View view,
                                        int position, long id) {
                    File fichier = mAdapter.getItem(position);
                    // Si le fichier est un rÈpertoire...
                    if(fichier.isDirectory())
                        // On change de rÈpertoire courant
                        updateDirectory(fichier);
                    else
                        // Sinon on lance l'irzm
                        seeItem(fichier);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
// On peut créer le menu via le code
        case R.id.item1: Toast.makeText(this, "à propos", Toast.LENGTH_LONG).show();
            Intent intent0 = new Intent(this, Apropos.class);
            startActivity(intent0);
            break;
        case R.id.item2: Toast.makeText(this, "transcrire audio", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(this, Maintranscription.class);
            startActivity(intent1);
            break;
        case R.id.item3: Toast.makeText(this, "voir fichier.txt", Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(this, ExplorateurActivity.class);
            startActivity(intent2);
            break;
        case R.id.item4: Toast.makeText(this, "vous avez quitter l'application", Toast.LENGTH_LONG).show();
            finish();
            moveTaskToBack(true);
            break;
    }
        return super.onOptionsItemSelected(item); }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View vue,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, vue, menuInfo);

        MenuInflater inflater = getMenuInflater();
        // On rÈcupËre des informations sur l'item par apport ‡ l'Adapter
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        // On rÈcupËre le fichier consernÈ par le menu contextuel
        File fichier = mAdapter.getItem(info.position);
        // On a deux menus, en fonction qu'il s'agit d'un rÈpertoire ou d'un fichier
        if(!fichier.isDirectory())
            inflater.inflate(R.menu.context_file, menu);
       else
            inflater.inflate(R.menu.context_dir, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // On rÈcupËre la position de l'item concernÈ
        File fichier = mAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.deleteItem:
                mAdapter.remove(fichier);
                fichier.delete();
                return true;

            case R.id.seeItem:
                seeItem(fichier);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        reader.close();
        return stringBuilder.toString();
    }
    /**
     * UtilisÈ pour visualiser un fichier
     * @param pFile le fichier ‡ visualiser
     */
    private void seeItem(File pFile) {
        // On crÈÈ un Intent
//        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////        i.setAction(android.content.Intent.ACTION_VIEW);
//
//        // On dÈtermine son type MIME
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        String ext = pFile.getName().substring(pFile.getName().indexOf(".") + 1).toLowerCase();
//        String type = mime.getMimeTypeFromExtension(ext);
//
//        i.addCategory(Intent.CATEGORY_OPENABLE);

        // On ajoute les informations nÈcessaires
//        i.setDataAndType(Uri.fromFile(pFile), type);
//
//        if(Uri.fromFile(pFile).toString().contains(".txt"))
//            i.setDataAndType(Uri.fromFile(pFile), "text/plain");



        try {
            String text0=readTextFromUri(Uri.fromFile(pFile));
            Intent intent0=new Intent(this,vocalText.class);
            intent0.putExtra("text1",text0);
            this.startActivity(intent0);

            //startActivity(i);
            // Et s'il n'y a pas d'activitÈ qui puisse gÈrer ce type de fichier
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Oups, vous n'avez pas d'application qui puisse lancer ce fichier", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * UtilisÈ pour naviguer entre les rÈpertoires
     * @param pFile le nouveau rÈpertoire dans lequel aller
     */

    public void updateDirectory(File pFile) {
        // On change le titre de l'activitÈ
        setTitle(pFile.getAbsolutePath());

        // L'utilisateur ne souhaite plus sortir de l'application
        mCountdown = false;

        // On change le repertoire actuel
        mCurrentFile = pFile;
        // On vide les rÈpertoires actuels
        setEmpty();

        // On rÈcupËre la liste des fichiers du nouveau rÈpertoire
        File[] fichiers = mCurrentFile.listFiles();

        // Si le rÈpertoire n'est pas vide...
        if(fichiers != null)
            // On les ajoute ‡  l'adaptateur
            for(File f : fichiers)
                mAdapter.add(f);
        // Et on trie l'adaptateur
        mAdapter.sort();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Si on a appuyÈ sur le retour arriËre
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // On prend le parent du rÈpertoire courant
            File parent = mCurrentFile.getParentFile();
            // S'il y a effectivement un parent
            if(parent != null)
                updateDirectory(parent);
            else {
                // Sinon, si c'est la premiËre fois qu'on fait un retour arriËre
                if(mCountdown != true) {
                    // On indique ‡ l'utilisateur qu'appuyer dessus une seconde fois le fera sortir
                    Toast.makeText(this, "Nous sommes dÈj‡ ‡ la racine ! Cliquez une seconde fois pour quitter", Toast.LENGTH_SHORT).show();
                    mCountdown  = true;
                } else
                    // Si c'est la seconde fois on sort effectivement
                    finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * On enlËve tous les ÈlÈments de la liste
     */

    public void setEmpty() {
        // Si l'adapteur n'est pas vide...
        if(!mAdapter.isEmpty())
            // Alors on le vide !
            mAdapter.clear();
        return;
    }

    /**
     * L'adaptateur spÈcifique ‡ nos fichiers
     */

    private class FileAdapter extends ArrayAdapter<File> {
        /**
         * Permet de comparer deux fichiers
         *
         */
        private class FileComparator implements Comparator<File> {

            public int compare(File lhs, File rhs) {
                // si lhs est un rÈpertoire et pas l'autre, il est plus petit
                if(lhs.isDirectory() && rhs.isFile())
                    return -1;
                // dans le cas inverse, il est plus grand
                if(lhs.isFile() && rhs.isDirectory())
                    return 1;

                //Enfin on ordonne en fonction de l'ordre alphabÈtique sans tenir compte de la casse
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }

        }

        public FileAdapter(Context context, int textViewResourceId,
                           List<File> objects) {
            super(context, textViewResourceId, objects);
            mInflater = LayoutInflater.from(context);
        }

        private LayoutInflater mInflater = null;

        /**
         * Construit la vue en fonction de l'item
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView vue = null;

            if(convertView != null)
                vue = (TextView) convertView;
            else
                vue = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, null);
            File item = getItem(position);
            //Si c'est un rÈpertoire, on choisit la couleur dans les prÈfÈrences
            if(item.isDirectory())
                vue.setTextColor(mColor);
            else
                // Sinon c'est du noir
                vue.setTextColor(Color.BLACK);
            vue.setText(item.getName());
            return vue;
        }

        /**
         * Pour trier rapidement les ÈlÈments de l'adaptateur
         */
        public void sort () {
            super.sort(new FileComparator());
        }
    }

    /**
     * Se dÈclenche dËs qu'une prÈfÈrence a changÈ
     */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        mColor = sharedPreferences.getInt("repertoireColorPref", Color.BLACK);
        mAdapter.notifyDataSetInvalidated();
    }



}
