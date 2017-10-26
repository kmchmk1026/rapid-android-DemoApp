package eu.project.rapid.demo;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import static android.os.Build.VERSION_CODES.N;

public class Solution extends AppCompatActivity {
    TableLayout table;
    Context context = this;
    TextView index;
    ArrayList<byte[][]> result_board;
    int N;
    int currentSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        index = (TextView) findViewById(R.id.index);
        table = (TableLayout) findViewById(R.id.view_root);
        result_board = (ArrayList<byte[][]>) getIntent().getExtras().getSerializable("solution");
        N = getIntent().getIntExtra("N", 2);

        currentSolution = 0;
        populate_with_cuurrent_solution();
//        int TABLE_WIDTH = N;
        // Populate the table with stuff
//        for (int y = 0; y < TABLE_WIDTH; y++) {
//            final int row = y;
//            TableRow r = new TableRow(this);
//            table.addView(r);
//            for (int x = 0; x < TABLE_WIDTH; x++) {
//                final int col = x;
//                //final ImageButton b = new ImageButton(this);
//                final Button b = new Button(this);
//                b.setEnabled(true);
//                //   b.setImageResource(R.drawable.blank);
//                if (result_board.get(1)[row][col] == 1)
//                    b.setBackgroundColor(Color.RED);
////                b.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Toast.makeText(getApplicationContext(),
////                                "You clicked (" + row + "," + col + ")",
////                                Toast.LENGTH_SHORT).show();
//////                            b.setImageResource(R.drawable.q);
////                    }
////                });
//                r.addView(b);
//            }
//        }
    }


    void populate_with_cuurrent_solution() {
        table.removeAllViews();
        for (int y = 0; y < N; y++) {
            final int row = y;
            TableRow r = new TableRow(this);
            table.addView(r);
            for (int x = 0; x < N; x++) {
                final int col = x;
                final ImageButton b = new ImageButton(this);
                b.setEnabled(true);
                if (result_board.get(currentSolution)[row][col] == 1) {
//                    b.setBackgroundColor(Color.RED);
                    b.setImageResource(R.drawable.queen);
                }
                else{
                    b.setImageResource(R.drawable.blank);
                }
                r.addView(b);
            }
        }
        index.setText(Integer.toString(currentSolution + 1) + "/" + Integer.toString(result_board.size()));
    }

    public void decrease(View v) {
        if (currentSolution != 0) {
            currentSolution -= 1;
            populate_with_cuurrent_solution();
        }
    }

    public void increase(View v) {
        if (currentSolution != (result_board.size() - 1)) {
            currentSolution += 1;
            populate_with_cuurrent_solution();
        }
    }

}
