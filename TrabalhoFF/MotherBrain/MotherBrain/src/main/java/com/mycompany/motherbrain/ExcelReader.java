package com.mycompany.motherbrain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author willi
 */
public class ExcelReader {

    private InputLayer il;
    private ArrayList<Double> ol;
    
    
    
    public void open(String PATH) {
        String excelFilePath = PATH; // Substitua com o caminho do seu arquivo

        // Cria ArrayLists para armazenar os valores das colunas
        ArrayList<Double> coluna1 = new ArrayList<>();
        ArrayList<Double> coluna2 = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath)); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            // Avaliador de fórmulas para obter o valor das células de fórmula
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // Pega a primeira planilha (sheet) do arquivo
            Sheet sheet = workbook.getSheetAt(0);

            // Itera por todas as linhas (rows) da planilha
            for (Row row : sheet) {

                // Armazena o valor das células de acordo com a coluna
                Cell cellColuna1 = row.getCell(0);  // Primeira coluna (X1)
                Cell cellColuna2 = row.getCell(1);  // Segunda coluna (Y)

                // Processa e imprime a primeira coluna
                if (cellColuna1 != null) {
                    double value = cellColuna1.getNumericCellValue();
                    coluna1.add(value);
                }

                // Processa e imprime a segunda coluna
                if (cellColuna2 != null) {
                    if (cellColuna2.getCellType() == CellType.FORMULA) {
                        evaluator.evaluateFormulaCell(cellColuna2);  // Avalia a fórmula
                        Double value = cellColuna2.getNumericCellValue();
                        coluna2.add(value);  // Salva no ArrayList da segunda coluna
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList inputs = new ArrayList<Input>(); //
        Input x1 = new Input(coluna1);

        inputs.add(x1);

        this.il = new InputLayer(inputs);
        this.ol = coluna2;

    }

    public InputLayer getIl() {
        return il;
    }

    public ArrayList<Double> getOl() {
        return ol;
    }
    
    
}
