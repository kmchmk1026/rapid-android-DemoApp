/*******************************************************************************
 * Copyright (C) 2015, 2016 RAPID EU Project
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *******************************************************************************/
package eu.project.rapid.queens;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.project.rapid.ac.DFE;
import eu.project.rapid.ac.Remote;
import eu.project.rapid.ac.Remoteable;
import eu.project.rapid.ac.utils.Utils;

public class NQueens extends Remoteable {

    private static final long serialVersionUID = 5687713591581731140L;
    private static final String TAG = "NQueens";
    private int N = 8;
    private int nrVMs;
    private transient DFE dfe;
//    private ArrayList<byte[][]> result_board = new ArrayList<>();

    /**
     * @param dfe   The dfe taking care of the execution.
     * @param nrVMs In case of remote execution specify the number of VMs needed.
     */
    public NQueens(DFE dfe, int nrVMs) {
        this.dfe = dfe;
        this.nrVMs = nrVMs;
    }

    /**
     * @param dfe The execution dfe taking care of the execution
     */
    public NQueens(DFE dfe) {
        this(dfe, 1);
    }

    @Override
    public void prepareDataOnClient() {

    }

    /**
     * Solve the N-queens problem
     *
     * @param N The number of queens
     * @return The number of solutions found
     */
    public ArrayList<byte[][]> solveNQueens(int N) {
        this.N = N;
        Method toExecute;
        Class<?>[] paramTypes = {int.class};
        Object[] paramValues = {N};

        ArrayList<byte[][]> result = new ArrayList<>();
        try {
            toExecute = this.getClass().getDeclaredMethod("localSolveNQueens", paramTypes);
            result = (ArrayList<byte[][]>) dfe.execute(toExecute, paramValues, this);
        } catch (SecurityException e) {
            // Should never get here
            e.printStackTrace();
            throw e;
        } catch (NoSuchMethodException e) {
            // Should never get here
            e.printStackTrace();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Remote
    public ArrayList<byte[][]> localSolveNQueens(int N) {

        ArrayList<byte[][]> countSolutions = new ArrayList<>();

        byte[][] board = new byte[N][N];

        int start = 0, end = N;

        if (Utils.isOffloaded()) {
            // cloneId == 0 if this is the main clone
            // or [1, nrVMs-1] otherwise
            int cloneId = Utils.readCloneHelperId();
            int howManyCols = (N) / nrVMs; // Integer division, we may
            // loose some columns.
            start = cloneId * howManyCols; // cloneId == 0 if this is the main clone
            end = start + howManyCols;

            // If this is the clone with the highest id let him take care
            // of the columns not considered due to the integer division.
            if (cloneId == nrVMs - 1) {
                end += N % nrVMs;
            }
        }

        Log.i(TAG, "Finding solutions for " + N + "-queens puzzle.");
        Log.i(TAG, "Analyzing columns: " + start + "-" + (end - 1));

        for (int i = start; i < end; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < N; l++) {
                        if (N == 4) {
                            countSolutions.add(setAndCheckBoard(board, i, j, k, l));
                            continue;
                        }
                        for (int m = 0; m < N; m++) {
                            if (N == 5) {
                                countSolutions.add(setAndCheckBoard(board, i, j, k, l, m));
                                continue;
                            }
                            for (int n = 0; n < N; n++) {
                                if (N == 6) {
                                    countSolutions.add(setAndCheckBoard(board, i, j, k, l, m, n));
                                    continue;
                                }
                                for (int o = 0; o < N; o++) {
                                    if (N == 7) {
                                        countSolutions.add(setAndCheckBoard(board, i, j, k, l, m, n, o));
                                        continue;
                                    }
                                    for (int p = 0; p < N; p++) {
                                        countSolutions.add(setAndCheckBoard(board, i, j, k, l, m, n, o, p));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        countSolutions.removeAll(Collections.singleton(null));
        /*for (int t = 0; t < countSolutions.size(); t++) {
            if (countSolutions.get(t) != null) {
                Log.i("qqq", Arrays.toString(countSolutions.get(t)[0]));
                Log.i("qqq", Arrays.toString(countSolutions.get(t)[1]));
                Log.i("qqq", Arrays.toString(countSolutions.get(t)[2]));
                Log.i("qqq", Arrays.toString(countSolutions.get(t)[3]));
//                break;
            }
        }*/

        Log.i(TAG, "Found " + countSolutions.size() + " solutions.");

//        Log.i("qqq", Arrays.toString(result_board.get(0)[0]));
//        Log.i("qqq", Arrays.toString(result_board.get(0)[1]));
//        Log.i("qqq", Arrays.toString(result_board.get(0)[2]));
//        Log.i("qqq", Arrays.toString(result_board.get(0)[3]));

        return countSolutions;
//        return new int[][]{{1, 0, 1, 0, 1}, {0, 1, 0, 1, 0}, {1, 0, 1, 0, 1}, {0, 1, 0, 1, 0}, {1, 0, 1, 0, 1}};
    }

    /**
     * When having more than one clone running the method there will be partial results which should
     * be combined to get the total result. This will be done automatically by the main clone by
     * calling this method.
     *
     * @param params Array of partial results.
     * @return The total result.
     */
    public int localSolveNQueensReduce(int[] params) {
        int solutions = 0;
        for (int param : params) {
            Log.i(TAG, "Adding " + param + " partial solutions.");
            solutions += param;
        }
        return solutions;
    }

    private byte[][] setAndCheckBoard(byte[][] board, int... cols) {

        clearBoard(board);

        for (int i = 0; i < N; i++)
            board[i][cols[i]] = 1;

        if (isSolution(board)) {
            printBoard(board);


            //this part is working
//            Log.i("qqq", Arrays.toString(board[0]));
//            Log.i("qqq", Arrays.toString(board[1]));
//            Log.i("qqq", Arrays.toString(board[2]));
//            Log.i("qqq", Arrays.toString(board[3]));
            return cloneArray(board);
        }

        return null;
    }


    private byte[][] cloneArray(byte[][] src) {
        int length = src.length;
        byte[][] target = new byte[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    private void clearBoard(byte[][] board) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = 0;
            }
        }
    }

    private boolean isSolution(byte[][] board) {

        int rowSum = 0;
        int colSum = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                rowSum += board[i][j];
                colSum += board[j][i];

                if (i == 0 || j == 0)
                    if (!checkDiagonal1(board, i, j))
                        return false;

                if (i == 0 || j == N - 1)
                    if (!checkDiagonal2(board, i, j))
                        return false;

            }
            if (rowSum > 1 || colSum > 1)
                return false;
            rowSum = 0;
            colSum = 0;
        }

        return true;
    }

    private boolean checkDiagonal1(byte[][] board, int row, int col) {
        int sum = 0;
        int i = row;
        int j = col;
        while (i < N && j < N) {
            sum += board[i][j];
            i++;
            j++;
        }
        return sum <= 1;
    }

    private boolean checkDiagonal2(byte[][] board, int row, int col) {
        int sum = 0;
        int i = row;
        int j = col;
        while (i < N && j >= 0) {
            sum += board[i][j];
            i++;
            j--;
        }
        return sum <= 1;
    }

    private void printBoard(byte[][] board) {
        for (int i = 0; i < N; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < N; j++) {
                row.append(board[i][j]);
                if (j < N - 1)
                    row.append(" ");
            }
            Log.i(TAG, row.toString());
        }
        Log.i(TAG, "----------------");
    }

    public void setNumberOfClones(int nrClones) {
        this.nrVMs = nrClones;
    }

    @Override
    public void copyState(Remoteable state) {

    }
}

