package com.example.tokenverifier;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


public class MainActivity extends AppCompatActivity {
    private static SecretKeySpec secretKey;
    private static byte[] key;
    public Button save_ip, scan, go;
    public EditText iptext;
    public int count=0;
    public static int temp1[][], temp2[][];
    public String ipstring, encdecstring,share1="",tablehash="",share2,s1="",hash,passwd="AM.EN.U4CSE15011",bankname;
    JSONArray tablejson;
    public boolean proc1;
    public AsyncHttpClient client = new AsyncHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(this,decrypt("pqTSYiLuLR1W3ts1OSVJVWRs6hIiEWX84JtJG585oTe1kHOqvMaO4TPunYN4vAuSTJ2VZalqNWYgZw6hjwJ+vg==","AM.EN.U4CSE15011"),Toast.LENGTH_LONG).show();

        save_ip = (Button) findViewById(R.id.button);
        scan = (Button) findViewById(R.id.button2);
        go = (Button) findViewById(R.id.button3);
        iptext = (EditText) findViewById(R.id.editText);

        go.setEnabled(false);


        save_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipstring="http://";
                ipstring+=String.valueOf(iptext.getText());
                ipstring += ":9090/getfile?hash=";
                Toast.makeText(getApplicationContext(),"set ip as : "+ipstring,Toast.LENGTH_LONG).show();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   temp1= new int[s1.length()/8][8];
                temp2 = new int[share2.length()/8][8];

                temp1 = To2dArray(s1);
                temp2 = To2dArray(share2);

                checkshare();
            }
        });

    }


    /**
     * For QR
     */

    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }


    private void updateText(String contents)  {
        int i=0,j;
        tablehash="";
        share1="";
        s1="";
        share2="";
        /*
        Inflater inflater = new Inflater();
        inflater.setInput(contents, 0, contents.length());
        byte[] result = new byte[1024];
        int resultLength = inflater.inflate(result);
        inflater.end();*/

        Log.d("Encrypted content is",contents);
        encdecstring = decrypt(contents,passwd);

        while(encdecstring.charAt(i)!=','){
            share1 += encdecstring.charAt(i);
            i++;
        }

        for(j=0;j<share1.length();j++){
            s1 += decompress(share1.substring(j, j+1));
        }
        i++;
        while(encdecstring.length()!=i) {
            tablehash+=encdecstring.charAt(i);
            i++;
        }



        Log.d("Share1 in binary",s1);



        client.get(ipstring+tablehash, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject timeline) {

                try {
                    JSONObject resp = timeline;
                    share2 = resp.getString("share2");
                    bankname = resp.getString("bankType");
                    Log.d("share2 in binary",share2);
                    hash = resp.getString("bankHash");
                    Log.d("Bankhash in binary",hash);
                    go.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Recieved Response",Toast.LENGTH_LONG).show();
            }

            public void onFailure(int statusCode, Header[] headers, JSONArray timeline) {
                Toast.makeText(getApplicationContext(),"Connection issue for json response",Toast.LENGTH_LONG).show();
            }

        });





    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //QR scanner

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                updateText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     *
     * @param tobeconverted
     * @return
     */


    public static String decompress(String tobeconverted){
        int decimal = Integer.parseInt(tobeconverted, 16);
        String binary = Integer.toBinaryString(decimal);
        return ("0000" + binary).substring(binary.length());
    }



    public static int[][] To2dArray(String sec) {
        int i, j, k = 0;
        int[][] array2d = new int[sec.length() / 8][8];
        for (i = 0; i < sec.length()-7; i += 8) {
            for (j = i; j < i + 8; j++) {
                array2d[k][j - i] = (sec.charAt(j) - 48);
            }
            k++;
        }

        return array2d;
    }

/*
    public  String converttobinary(String strs){
        byte[] bytes = strs.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes){
            int val = b;
            for (int i = 0; i < 8; i++){
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }
*/


    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }

        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }



    public void checkshare() {
        int i,j,sum,temps = -1,temps1;
        boolean verified = true;

        for (i = 0; i < temp1.length; i++) {
            sum = 0;
            for (j = 0; j < temp1[i].length; j++)
                sum += temp1[i][j] * temp2[i][j];
            sum %= 2;

            if (sum != (hash.charAt(i) - 48)){
                verified = false;
                break;
            }

        }
        if(verified==true)
        {
            Toast.makeText(getApplicationContext(),"Verified token came from "+bankname,Toast.LENGTH_LONG).show();
            return;
        }
        else
            Toast.makeText(getApplicationContext(),"Found no match"+temps,Toast.LENGTH_LONG).show();

    return;
    }

}





