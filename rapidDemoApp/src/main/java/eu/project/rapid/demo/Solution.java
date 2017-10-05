package eu.project.rapid.demo;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static android.os.Build.VERSION_CODES.N;

public class Solution extends AppCompatActivity {
    TableLayout table;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        table = (TableLayout) findViewById(R.id.view_root);
//        String s = getIntent().getStringExtra("dimension");
//        if (s == null || s == "0") {
//            s = "4";
//        }
        ArrayList<byte[][]> result_board = (ArrayList<byte[][]>) getIntent().getExtras().getSerializable("solution");
        int N = result_board.get(0).length;
//        int N = 4;
        int TABLE_WIDTH = N;
//        board = new int[][] {{1,0,1,0},{0,1,0,1},{1,0,1,0},{0,1,0,1}};
//
//        Log.i("qqq", Arrays.toString(result_board.get(0)[0]));
//        Log.i("qqq", Arrays.toString(result_board.get(0)[1]));
//        Log.i("qqq", Arrays.toString(result_board.get(0)[2]));
//        Log.i("qqq", Arrays.toString(result_board.get(0)[3]));

        // Populate the table with stuff
        for (int y = 0; y < TABLE_WIDTH; y++) {
            final int row = y;
            TableRow r = new TableRow(this);
            table.addView(r);
            for (int x = 0; x < TABLE_WIDTH; x++) {
                final int col = x;
                //final ImageButton b = new ImageButton(this);
                final Button b = new Button(this);
                b.setEnabled(false);
                //   b.setImageResource(R.drawable.blank);
                if (result_board.get(1)[row][col] == 1)
                    b.setBackgroundColor(Color.RED);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),
                                "You clicked (" + row + "," + col + ")",
                                Toast.LENGTH_SHORT).show();
//                            b.setImageResource(R.drawable.q);
                    }
                });
                r.addView(b);
            }
        }
    }
}
