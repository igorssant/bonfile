package com.bonfile;

import com.bonfile.controller.appendController.AppendController;
import com.bonfile.controller.bonfileObjectController.BonfileObjectController;
import com.bonfile.controller.readController.ReadController;
import com.bonfile.controller.tupleController.*;
import com.bonfile.util.tokens.Tokens;
import java.util.HashMap;
import java.util.LinkedList;

public class Bonfile {
    public static void printAllTokens() {
        System.out.println(Tokens.TOKENS);
    }

    public static HashMap<String, String> getAllTokens() {
        return Tokens.TOKENS;
    }

    public static void readTest(String path) {
        try(ReadController readController = new ReadController(path)) {
            /* TEST READING OF TUPLES */
            UnitController<Object> unitController = readController.readUnit("unit");
            System.out.println(unitController.getItem());

            PairController<Object, Object> pairController = readController.readPair("pair");
            System.out.println(pairController.getTuple());

            /* TEST READING BONFILE OBJECT */
            readController.rewind();

            BonfileObjectController bonfileObjectController = new BonfileObjectController(readController.readObject("someone"));
            System.out.println(bonfileObjectController.getInt("age"));

            /* TEST READING OF PRIMITIVES */
            readController.rewind();

            System.out.println(readController.readInteger());

            /* TEST READING LIST WITH NAME */
            readController.rewind();

            System.out.println(readController.readList("list"));

            /* TEST READING LIST WITHOUT NAME */
            readController.rewind();

            System.out.println(readController.readList());

            readController.rewind();

            System.out.println(readController.readDict());
        } catch(Exception e) {
            System.err.println("ERROR\n" + e.getMessage());
        }
    }

    public static void writeTest(String path) {
        try(AppendController appendController = new AppendController(path)) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("CAR", "FOUR WHEELS");
            hashMap.put("MOTORBIKE", "TWO WHEELS");
            hashMap.put("BIKE", "TWO WHEELS");
            hashMap.put("YATCH", "NO WHEELS");

            appendController.writeDict("vehicles", hashMap);

            appendController.writePrimitive("integer_value", 1);
            appendController.writePrimitive("floating_point", 1.2F);
            appendController.writePrimitive("double_precision", 1.5D);
            appendController.writePrimitive("boolean_value", true);
            appendController.writePrimitive("character", 'a');
            appendController.writePrimitive("character_array", "It is an array!!!");

            LinkedList<Object> linkedList = new LinkedList<>();
            linkedList.add("ABRA");
            linkedList.add("KADABRA");
            linkedList.add("ALAKAZAN");
            linkedList.add("GASTLY");
            linkedList.add("HAUNTER");
            linkedList.add("GENGAR");

            appendController.writeList("list", linkedList, 5);

            BonfileObjectController bonfileObjectController = new BonfileObjectController("someone", "person");
            bonfileObjectController.put("age", 23);
            bonfileObjectController.put("city", "somewhere");
            bonfileObjectController.put("state", "bahia");
            bonfileObjectController.put("country", "brazil");

            appendController.writeObject(bonfileObjectController.getBonfileObject());

            UnitController<Object> unitController= new UnitController<>("Hello!!!");
            PairController<Object, Object> pairController= new PairController<>("value", 2);
            TripletController<Object, Object, Object> tripletController = new TripletController<>('A', true, 32.1d);
            QuartetController<Object, Object, Object, Object> quartetController = new QuartetController<>(0, 1, 2, 3);
            QuintupletController<Object, Object, Object, Object, Object> quintupletController = new QuintupletController<>(5, 6, 7, 8.8d, 0.3f);
            SextetController<Object, Object, Object, Object, Object, Object> sextetController = new SextetController<>("phrase", 'C', -100, 45.6d, 0.4f, true);

            appendController.writeTuple("unit", unitController);
            appendController.writeTuple("pair", pairController);
            appendController.writeTuple("triplet", tripletController);
            appendController.writeTuple("quartet", quartetController);
            appendController.writeTuple("quintuplet", quintupletController);
            appendController.writeTuple("sextet", sextetController);
        } catch(Exception e) {
            System.err.println("ERROR");
        }
    }

    public static void main(String [] args) {
        String path = "/home/igorsssantana/Documents/trabalho/intelliJ/bonfile/src/scratch.bon";
        writeTest(path);
        readTest(path);
    }
}
