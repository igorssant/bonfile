package com.bonfile;

import com.bonfile.controller.appendController.AppendController;
import com.bonfile.controller.bonfileObjectController.BonfileObjectController;
import com.bonfile.util.tokens.Tokens;
import java.util.HashMap;
import java.util.LinkedList;

public class Bonfile {
    public static void printAllTokens() {
        Tokens tokens = new Tokens();
        System.out.println(Tokens.TOKENS);
    }

    public static void main(String [] args) {
        String path = "/home/igorsssantana/Documents/trabalho/intelliJ/bonfile/src/scratch.bon";

        try {
            AppendController appendController = new AppendController(path);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("CARRO", "VEICULO");
            hashMap.put("MOTO", "VEICULO");
            hashMap.put("BICICLETA", "POBRE");
            hashMap.put("YATCH", "RICO");

            appendController.writeDict("veiculos", hashMap);
            appendController.writePrimitive("valor_inteiro", 1);
            appendController.writePrimitive("ponto_flutuante", 1.0f);
            appendController.writePrimitive("precisao_dupla", 1.5d);
            appendController.writePrimitive("valor_booleano", true);
            appendController.writePrimitive("caractere", 'a');
            appendController.writePrimitive("cadeia_caracteres", "Isto é uma frase!!!");

            LinkedList<Object> linkedList = new LinkedList<>();
            linkedList.add("ABRA");
            linkedList.add("KADABRA");
            linkedList.add("ALAKAZAN");
            linkedList.add("GASTLY");
            linkedList.add("HAUNTER");
            linkedList.add("GENGAR");

            appendController.writeList("lista", linkedList, 5);

            BonfileObjectController bonfileObjectController = new BonfileObjectController("someone", "person");
            bonfileObjectController.put("age", 23);
            bonfileObjectController.put("city", "somewhere");
            bonfileObjectController.put("state", "bahia");
            bonfileObjectController.put("country", "brazil");

            appendController.writeObject(bonfileObjectController.getBonfileObject());

            appendController.close();
        } catch (Exception e) {
            System.err.println("ERRO");
        }
    }
}
