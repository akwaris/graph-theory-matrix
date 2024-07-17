import java.util.ArrayList;
import java.util.List;

public class Matrix {

    private int[][] values;
    public CsvReader csvReader;

    public Matrix(String csvFilePath) {
        csvReader = new CsvReader();
        setValues(csvFilePath);
    }

    public Matrix(int dim) {
        values = new int[dim][dim];
    }

    public Matrix(Matrix matrix) {                                                  //copy matrix
        values = new int[matrix.values.length][matrix.values.length];

        for (int row = 0; row < matrix.values.length; row++) {
            for (int column = 0; column < matrix.values.length; column++) {
                values[row][column] = matrix.values[row][column];
            }
        }
    }

    public void setValues(String csvFilePath) {
        values = csvReader.reader(csvFilePath);
    }   //reading values from csv

    public int[][] getAdjMatrix() {
        return values;
    }

    public Matrix multiply(Matrix otherMatrix) {
        Matrix result = new Matrix(values.length);

        for (int row = 0; row < values.length; row++){                           //result matrix = called matrix * other matrix
            for (int column = 0; column < values.length; column++) {
                for (int otherColumn = 0; otherColumn < values.length; otherColumn++) {
                    result.values[row][column] += this.values[row][otherColumn] * otherMatrix.values[otherColumn][column];
                }
            }
        }

        return result;
    }

    public Matrix potentiate(int exponent) {
        Matrix result = new Matrix(this);                                                 //copy matrix

        for (int i = 1; i < exponent; i++) {                                           //result matrix is ​​multiplied by the original matrix
            result = result.multiply(this);
        }

        return result;
    }

    public Matrix distanceMatrix() {
        Matrix result = new Matrix(values.length);

        for (int row = 0; row < result.values.length; row++) {                        //main diagonal is set to 0 -> the remaining to -1
            for (int column = 0; column < result.values.length; column++) {
                if (row == column) {
                    result.values[row][column] = 0;
                }else {
                    result.values[row][column] = -1;
                }
            }
        }

        for (int exponent = 1; exponent < result.values.length; exponent++) {
            boolean changed = false;
            boolean finished = true;
            Matrix pot = potentiate(exponent);

            for (int row = 0; row < result.values.length; row++) {                    //if result matrix contains -1 -> not finished
                for (int column = 0; column < result.values.length; column++) {
                    if (result.values[row][column] == -1) {
                        finished = false;
                    }
                }
            }

            for (int row = 0; row < result.values.length; row++) {                    //is raised to the power until there is no more -1
                for (int column = 0; column < result.values.length; column++) {
                    if (result.values[row][column] == -1 && pot.values[row][column] > 0) {
                        result.values[row][column] = exponent;
                        changed = true;
                    }
                }
            }

            if (!changed || finished) break;
        }

        return result;
    }

    public Matrix pathMatrix() {
        Matrix result = new Matrix(this.values.length);

        for (int row = 0; row < result.values.length; row++) {                        //the main diagonal of the matrix is filled with 1
            for (int column = 0; column < result.values.length; column++) {
                if (row == column) result.values[row][column] = 1;
            }
        }

        for (int exponent = 1; exponent < result.values.length; exponent++) {               //loop for the size of the power matrix
            boolean changed = false;
            boolean finished = true;
            Matrix powerMatrix = potentiate(exponent);                                //support power matrix with the given size

            for (int reihe = 0; reihe < result.values.length; reihe++) {                    //check whether matrix is filled with 1
                for (int spalte = 0; spalte < result.values.length; spalte++) {
                    if (result.values[reihe][spalte] != 1) finished = false;
                }
            }

            for (int reihe = 0; reihe < result.values.length; reihe++) {
                for (int spalte = 0; spalte < result.values.length; spalte++) {
                    if (powerMatrix.values[reihe][spalte] > 0) {                        //if new values are greater than 0 -> result matrix gets a 1 at that point
                     result.values[reihe][spalte] = 1;
                     changed = true;
                    }
                }
            }

            if (!changed || finished) break;                              //if nothing has changed or is only filled with 1 -> the loop is aborted
        }

        return result;
    }

    public int[] eccentricities() {
        Matrix distanceMatrix = distanceMatrix();
        int[] eccentricities = new int[values.length];

        for (int row = 0; row < distanceMatrix.values.length; row++) {                  //the highest value per line is taken from the distance matrix
            for (int column = 0; column < distanceMatrix.values.length; column++) {
                eccentricities[row] = Math.max(distanceMatrix.values[row][column], eccentricities[row]);
            }
        }

        return eccentricities;
    }

    public int radius() {                                                                   //radius = smallest eccentricity
        int[] eccentricities = eccentricities();
        int radius = Integer.MAX_VALUE;

        for (int row = 0; row < eccentricities.length; row++) {
            radius = Math.min(eccentricities[row], radius);
        }

        return  radius;
    }

    public int diameter() {                                                              //diameter = greatest eccentricity
        int[] eccentricities = eccentricities();
        int diameter = 0;

        for (int row = 0; row < eccentricities.length; row++) {
            diameter = Math.max(eccentricities[row], diameter);
        }

        return diameter;
    }

    public List<Integer> center() {
        int[] eccentricities = eccentricities();
        int radius = radius();
        List<Integer> center = new ArrayList<>();

        for (int row = 0; row < eccentricities.length; row++) {
            if (eccentricities[row] == radius) center.add(row +1);                      //if node equals radius -> center
        }

        return center;
    }

    public int componentAmount() {                                                          //number of components list is returned = components.size()
        boolean[] visited = new boolean[values.length];
        ArrayList<ArrayList<Integer>> components = new ArrayList<>();
        int componentAmount = 0;
        for (int v = 0; v < values.length; v++) {
            if (!visited[v]) {
                ArrayList<Integer> component = new ArrayList<>();
                dfsComp(v, visited, component);
                components.add(component);
                componentAmount++;
            }
        }

        return componentAmount;
    }

    public ArrayList<ArrayList<Integer>> components() {
        boolean[] visited = new boolean[values.length];
        ArrayList<ArrayList<Integer>> components = new ArrayList<>();
        for (int v = 0; v < values.length; v++) {
            if (!visited[v]) {
                ArrayList<Integer> component = new ArrayList<>();
                dfsComp(v, visited, component);                                                //depth-first search is called when node is not visited.
                components.add(component);                                                    //when dfs finished from component -> list will be added to component list
            }
        }

        return components;
    }

    public void dfsComp(int v, boolean visited[], ArrayList<Integer> component) {              //visited node -> visited, added to the list
        visited[v] = true;
        component.add(v);
        for (int neighbor = 0; neighbor < values.length; neighbor++) {                             //if neighbor 1 -> neighbor becomes v and next neighbor is searched
            if (values[v][neighbor] == 1 && !visited[neighbor]) {
                dfsComp(neighbor, visited, component);
            }
        }
    }

    public List<Integer> articulations() {
        int components = componentAmount();
        ArrayList<Integer> articulations = new ArrayList<>();

        for (int node = 0; node < values.length; node++) {
            Matrix copy = new Matrix(this);                                                     //reset

            for (int i = 0; i < values.length; i++) {                                            //edges connected to current node -> temporarily removed
                copy.values[node][i] = 0;
                copy.values[i][node] = 0;
            }

            if (copy.componentAmount() -1 > components) articulations.add(node +1);        //component count increases -> articulation point
        }

        return articulations;
    }

    public ArrayList<ArrayList<Integer>> bridges() {
        ArrayList<ArrayList<Integer>> bridges = new ArrayList<>();
        Matrix copy = new Matrix(this);
        int components = copy.componentAmount();

        for (int row = 0; row < copy.values.length; row++) {
            for (int column = 0; column < copy.values.length; column++) {
                if (copy.values[row][column] == 1) {                                           //edge present -> will be removed
                    copy.values[row][column] = 0;
                    copy.values[column][row] = 0;

                    if (copy.componentAmount() > components) {                               //number of components increases -> bridge
                        ArrayList<Integer> bridge = new ArrayList<>();
                        bridge.add(Math.min(column +1, row +1));
                        bridge.add(Math.max(column +1, row +1));

                        if (!bridges.contains(bridge)) bridges.add(bridge);                 //bridge newly found -> will be added
                    }
                }
                copy.values[row][column] = 1;
                copy.values[column][row] = 1;
            }
        }
        return bridges;
    }

    public String toString() {
        String matrix = "";

        for (int[] row : values) {
            matrix += "[";
            for (int i = 0; i < row.length; i++) {
                matrix += row[i];
                if (i < row.length -1) matrix += ", ";
            }
            matrix += "]\n";
        }

        return matrix;
    }

    public String toString(String method) {
        if (method == "eccentricities") {
            int[] eccentricities = eccentricities();
            String erg = "";

            for (int i = 0; i < eccentricities.length; i++) {
                erg += "Eccentricity of node " + (char) ('A' + i) + ": " + eccentricities[i] + "\n";
            }

            return erg;
        }

        if (method == "radius") {
            int radius = radius();
            String erg = "Radius: " + radius;
            return erg;
        }

        if (method == "diameter") {
            int durchmesser = diameter();
            String erg = "Diameter of the matrix: " + diameter();
            return erg;
        }

        if (method == "center") {
            List<Integer> center = center();
            String erg = "Center: " + center + "\n";

            return erg;
        }

        if (method == "components") {
            ArrayList<ArrayList<Integer>> components = components();
            String erg = "";

            for (int i = 0; i < components.size(); i++) {
                erg += "Components " + (i + 1) + ": " + components.get(i) + "\n";
            }

            return erg;
        }

        if (method == "articulations") {
            List<Integer> articulations = articulations();
            String erg = "";

            for (int i = 0; i < articulations.size(); i++) {
                erg += "Articulation Point " + (i + 1) + ": " + articulations.get(i) + "\n";
            }

            return erg;
        }

        if (method == "bridges") {
            ArrayList<ArrayList<Integer>> bridges = bridges();
            String erg = "";

            for (int i = 0; i < bridges.size(); i++) {
                ArrayList<Integer> bridge = bridges.get(i);
                erg += "Bridge " + (i + 1) + ": [" + bridge.get(0) + ", " + bridge.get(1) + "]\n";
            }

            return erg;
        }

        return null;
    }
}
