public class Main {
    public static void main(String[] args) {

        Matrix adjacencyMatrix = new Matrix("24n_01.csv");
        Matrix distanceMatrix = adjacencyMatrix.distanceMatrix();
        String eccentricities = "eccentricities";
        String radius = "radius";
        String diameter = "diameter";
        String center = "center";
        String components = "components";
        String articulations = "articulations";
        String bridges = "bridges";

        System.out.println("Distanzmatrix:\n"+ distanceMatrix.toString());

        System.out.println("Potenzmatrix A^2:\n"+ adjacencyMatrix.potentiate(2));

        System.out.println("Potenzmatrix A^4:\n"+ adjacencyMatrix.potentiate(4));

        System.out.println("Potenzmatrix A^6:\n"+ adjacencyMatrix.potentiate(6));

        System.out.println(adjacencyMatrix.toString(eccentricities));

        System.out.println(adjacencyMatrix.toString(radius));

        System.out.println(adjacencyMatrix.toString(diameter));

        System.out.println(adjacencyMatrix.toString(center));

        System.out.println(adjacencyMatrix.toString(components));

        System.out.println(adjacencyMatrix.toString(articulations));

        System.out.println(adjacencyMatrix.toString(bridges));
    }
}