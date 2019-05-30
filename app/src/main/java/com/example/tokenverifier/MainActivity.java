package com.example.tokenverifier;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
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
    public String ipstring, encdecstring,share1="", hashshare2 ="",tablehash="",share2,s1="";
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
                  //Toast.makeText(getApplicationContext(),/*"share 1 :"+share1+"\n\n"+*/"share 2 : "+share2 +    "\n\ntablejson : "+tablejson,Toast.LENGTH_LONG).show();
                  //  Toast.makeText(getApplicationContext(),"share1 is "+share1.length()+"\n\nshare2 is "+share2.length()+"\n\njson is  "+tablejson.toString(),Toast.LENGTH_LONG).show();
                temp1= new int[s1.length()][8];
                temp2 = new int[share2.length()][8];

               // temp1 = To2dArray(s1);
               //   temp2 = To2dArray(share2);
//---------
               // temp1 = To2dArray("10011101011001010000011001011111100111010101001101101100000010111000111101000001010011001111110100101001011011110101110001001000010100000011111101110001011101011000111000111010111101001110011111111011001010111110110010011100110010101001110101110100000111010001111011000010101111001010100011000010101010000000110011111110011010000111101011001001001001001111110010100001010011010010111001011101110101001011110010111111101000001010111111111001110010100111001001000100110100100110111011010000001000000110010001100001011000000101101111000000100100010001101110000100111110011010110101111011000110001001101000111110110001110110111110001000010011000001011111101000111101001000010011100101010011011100010011011011100001011111101010110111101101110101110111101101110010100000100111001001100011010010101010000101011101111011111011001001010010000101011011100001110000101010111010010100010011000011011100010111101110000010100110011111011100001011100000100101001101010111000011101110001101011101100111001110011000011110010111111110011001010001000101001101111011100100100001101000010010011110110011100001100100110001100101100100100100001001111011110011100101010101010010000011011101011011101011011000000101010100000101101110100100011101101100100111100011011111010111101101010000111110011100100000010011000111100000100110110111111001111101100000110111001010000111010011100011111110010001000011111000100100011100110000011101110101100101011111011100000000011111111010111000001100011100101011101001001100101000101101000111010010101010010100011011111001100001010011100111100101110010110010110110101111000100111001010011001001010011100011101100110000001111101001010110011011010001101110011101101010001100011000000110111100101011011101011010001110111001101111011111111011001101110000010100000001101110011010001010001100011111110110101100000110001100010111100101110010010101010010000111010111001001011100111110110011111001011011100011001000100010111101001000011111011101001110111011000000011000111101100100110011001011011101111011111111100001100101001100100001111000000001110100000111010100111010010111001000011000100001010101101100000111101110000001000000001011110001010011011100110101000000011100101101110010011000110010001101000001100000010101110101000111100010101010011111100111100110001000000011010111100010101001101001001001011101110101000101011010110010000110011110101001011100001100000011100010001111001011101000010010000010000010011000011001010010001111100000000110000110000000010010101110010010100010111111001110110101000101100000010001100111000011011110001000101101100011000011011001111011011000000110110110001101101110000111000110011011000101110000110110101100110100000110100000010000011111111111101100111101010100000101001111100110101101001011000001001011011110111110011000110100011111000101011100000010110001100100010110100110");
               // temp2 = To2dArray("00101110010110101010100101011000010101100101101110111111011110011010101111100100000011001111110001001110111100100100111000100101110110010111111011000100010100000001110011101110110100010100001001000110011000111100111110110101111111010101000100111100001101100101111101110100110001100000100010010010001100100100010100110011110110100101110010100110011111011011110010111101101011110101010010111000011111011111111101001110010100010001011010010101100001100100001001011010101001101001011110001010110011000000010010100100111011111111111010100110001001111111010110001101001100011101011011011110010011000111010011010101011110100000000100111011110011110010000000101110001010101011100011010000011110000111110010010111100110111000010111111010000001110011000011000111100100011111101110001110001000011101111010111101001100110101000000010010010101110001101010111011011111110000011011001101111111101100000001111100101110111101101101101000001010000101110001111111110010100101001111101111100011101111110111000110000000101001011100111101010000100000000011011001100101111010000100001010110100111100001010110011110000011001110010011101110110000001110101101100000010110010011101011011101000001100010000111000110000011001110110000101111111111010000111110110000010100100011010001101111101001000111010100110011010100010010010000111100100010101011101111110001101101110100111011010101000001111001110011001010001101000100011010110001000100000001011101111000000100101101110000000001001111000001100100000101110011011100100000001001110011101101111000011101000100011001001010100000101111101010100100111011101001000000110000000000001111010010010000111110000000100110011000110011001011110011011010001111100101011000100101011000000001111110010110000000110001001100111111100111011011100100011101000000001110111001111000001010000110111101001101100110000001000011000111001111100011110000010011111011010110110111001111011011011100101001111101110101010100011110010010010011001101000110000100011011011010001111001010011001000100011110011001010010011000010100000111001110000100111101110100111110011110011001111010101100100101111010100111000010111000100001111010000110110101011111101001000101011000100010011100001000101100101110100010001110111010011101010110111100101111101110000100100001000111010011111010101111001010011110110110100101100111011000010001011110001001111010001001001000111011100000110001001011010000010100010101010111001100100100101011010011110101101101110000010110000010001010111010010010111000100000110001100001011010100000100011001010101110011010001111000101101011011101100111011110010111010001100000110011001110100100010111001000010100011101001010001011011010100010011011001110010011110100010101010111010111110100110001001001001111011000011000011010001001101100101011011011010100101110010001011101000100001100011011010010101101001101000101000");
//----------
                try {
                    checkshare();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * For QR
     */

    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }


    private void updateText(String contents) {
        int i=0,j;

        encdecstring = contents;
        while(encdecstring.charAt(i)!=','){
            share1+=encdecstring.charAt(i);
            i++;
        }
        i++;

        for(j=0;j<share1.length();j++){
            s1 += decompress(share1.substring(j, j+1));
        }

        while(encdecstring.charAt(i)!=','){
            hashshare2 +=encdecstring.charAt(i);
            i++;
        }
        i++;
        while(encdecstring.length()!=i){
            tablehash+=encdecstring.charAt(i);
            i++;
        }


        client.get(ipstring+hashshare2, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Connection issue for string response",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                share2 = responseString;
                proc1 = true;
            }
        });


        client.get(ipstring+tablehash, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                tablejson =  timeline;
                //Toast.makeText(getApplicationContext(), "Jsonresponse:  "+firstEvent,Toast.LENGTH_LONG).show();
                if(proc1==true)
                    go.setEnabled(true);
            }

            public void onFailure(int statusCode, Header[] headers, JSONArray timeline) {
                Toast.makeText(getApplicationContext(),"Connection issue for json response",Toast.LENGTH_LONG).show();
            }

        });





    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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


    public void checkshare() throws JSONException {
        int i,j,k,sum;
        boolean verified = true;
        String inform="";
        String hash,tobesnt;
        for(k=0; k<4; k++) {
            JSONObject object = tablejson.getJSONObject(k);
            tobesnt = object.getString("bankHash");
            hash = converttobinary(tobesnt);
            hash=hash.replaceAll("\\s+","");
          //  hash="0101001001001110011001100101101001010111011011110111001001001011001100100110011100110000010110100101010101000100001101000110010101101001001010110110111101100111010100010111001001001111001101010110000101100011011010100110011001110110011001010101000101100111011101100011001001111001011100100110101101101101010011000110011001001100010010110011100000111101";
            for (i = 0; i < temp1.length; i++) {
                sum = 0;
                for (j = 0; j < temp1[i].length; j++)
                     sum += temp1[i][j] * temp2[i][j];

                sum %= 2;
                inform+=sum;
                if (sum != (hash.charAt(i) - 48)){
                    verified = false;
                    break;
                }

            }
            if(verified==true)
            {
                Toast.makeText(getApplicationContext(),object.getString("Type"),Toast.LENGTH_LONG).show();
                return;
            }
            else
            {
                if(k==3)
                {
                    Toast.makeText(getApplicationContext(),"Found no match",Toast.LENGTH_LONG).show();

                }
            }
        }
    return;
    }

}





