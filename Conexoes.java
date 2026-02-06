import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Conexoes {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Entrada de dados conforme sua solicitação de interação [cite: 18, 19, 20]
        System.out.print("Digite o número de linhas (R): ");
        int R = sc.nextInt();
        System.out.print("Digite o número de colunas (C): ");
        int C = sc.nextInt();
        System.out.print("Digite o custo máximo (M): ");
        int M = sc.nextInt();

        int[][] matriz = fazMatriz(R, C);
        preencheMatrizPadrao(matriz, M);

        System.out.println("\n--- Tabuleiro de Custos ---");
        printMatriz(matriz);

        exibirMelhorCaminho(matriz);

        sc.close();
    }

    private static int[][] fazMatriz(int qtdLinha, int qtdColuna) {
        return new int[qtdLinha][qtdColuna];
    }

    private static void preencheMatrizPadrao(int[][] matriz, int M) {
        int contagem = 1;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = contagem;
                contagem++;
                if (contagem > M) contagem = 1;
            }
        }
    }

    private static void exibirMelhorCaminho(int[][] matriz) {
        int R = matriz.length;
        int C = matriz[0].length;

        // dp[i][j] = custo mínimo para chegar na linha i, coluna j
        long[][] dp = new long[R][C];
        // deOndeVem[i][j] = índice da coluna na linha i-1 que gerou o menor custo
        int[][] deOndeVem = new int[R][C];

        // 1. Caso Base: primeira linha [cite: 14]
        for (int j = 0; j < C; j++) {
            dp[0][j] = matriz[0][j];
        }

        // 2. Transição: Programação Dinâmica com restrição de adjacência
        for (int i = 1; i < R; i++) {
            for (int j = 0; j < C; j++) {

                // Opção 1: Diretamente acima (sempre existe)
                long menorCustoAnterior = dp[i-1][j];
                int melhorColAnterior = j;

                // Opção 2: Acima e à esquerda (se não for a primeira coluna)
                if (j > 0) {
                    if (dp[i-1][j-1] < menorCustoAnterior) {
                        menorCustoAnterior = dp[i-1][j-1];
                        melhorColAnterior = j - 1;
                    }
                }

                // Opção 3: Acima e à direita (se não for a última coluna)
                if (j < C - 1) {
                    if (dp[i-1][j+1] < menorCustoAnterior) {
                        menorCustoAnterior = dp[i-1][j+1];
                        melhorColAnterior = j + 1;
                    }
                }

                dp[i][j] = matriz[i][j] + menorCustoAnterior;
                deOndeVem[i][j] = melhorColAnterior;
            }
        }

        // 3. Encontrar o ponto final com menor custo na última linha [cite: 22]
        int colFinal = 0;
        for (int j = 1; j < C; j++) {
            if (dp[R-1][j] < dp[R-1][colFinal]) {
                colFinal = j;
            }
        }

        // 4. Backtracking para recuperar o caminho [cite: 15]
        ArrayList<String> caminho = new ArrayList<>();
        int colAtual = colFinal;
        long custoTotal = dp[R-1][colFinal];

        for (int i = R - 1; i >= 0; i--) {
            caminho.add("Linha " + (i + 1) + ", Coluna " + (colAtual + 1) + " (Custo: " + matriz[i][colAtual] + ")");
            colAtual = deOndeVem[i][colAtual];
        }
        Collections.reverse(caminho);

        System.out.println("\n--- Caminho de Menor Custo ---");
        for (String passo : caminho) {
            System.out.println(passo);
        }
        System.out.println("\nCusto Total Final: " + custoTotal);
    }

    private static void printMatriz(int[][] matriz) {
        for (int[] linha : matriz) {
            for (int valor : linha) {
                System.out.printf("%3d ", valor);
            }
            System.out.println();
        }
    }
}
