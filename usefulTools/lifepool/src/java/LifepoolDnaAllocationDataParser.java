import java.io.*;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 26/02/13
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LifepoolDnaAllocationDataParser {
    public static void main(String[] args) {
        System.out.println("----------------------- DNA Allocations -----------------------------------------------------");
        BufferedReader br = null;

        String dataLine;
        boolean firstLine=false;

        StringBuffer psb=new StringBuffer();

        HashMap<String,String> map = new HashMap<String, String>();

        map.put("A","1");
        map.put("B","2");
        map.put("C","3");
        map.put("D","4");
        map.put("E","5");
        map.put("F","6");
        map.put("G","7");
        map.put("H","8");
        map.put("I","9");
        map.put("J","10");
        map.put("K","11");
        map.put("L","12");
        map.put("M","13");
//        map.put("N","14");
//        map.put("O","15");

        try{
            br = new BufferedReader(new FileReader(args[0]));
            while ((dataLine = br.readLine()) != null) {
                if(!firstLine){
                    firstLine=true;
                    continue;
                }
                String[] tokens = dataLine.split("[,]");

                //1st DNA sample
                String participantId=tokens[2];

                String specimenId = tokens[3];

                String parentId =  participantId.substring(0,2).trim()+" "+participantId.substring(2).trim()+" "+specimenId.trim();

                String biospecimenUid = parentId +"-" +tokens[12];
                psb.append(biospecimenUid);

                String freezer = insertNull(tokens[17]);
                psb.append(","+freezer);
                String rack = insertNull(tokens[18]);
                psb.append(","+rack);
                String box = insertNull(tokens[19]);
                psb.append(","+box);
                String row = insertNull(map.get(tokens[20]));
                psb.append(","+row);
                String col = insertNull(tokens[21]);
                psb.append(","+col+"\n");

                //2nd DNA sample
                biospecimenUid = parentId +"-" +tokens[22];
                psb.append(biospecimenUid);
                freezer = insertNull(tokens[25]);
                psb.append(","+freezer);
                rack = insertNull(tokens[26]);
                psb.append(","+rack);
                box = insertNull(tokens[27]);
                psb.append(","+box);
                row = insertNull(map.get(tokens[28]));
                psb.append(","+row);
                col = insertNull(tokens[29]);
                psb.append(","+col+"\n");

                //3rd DNA sample
                biospecimenUid = parentId +"-" +tokens[30];
                psb.append(biospecimenUid);
                freezer = insertNull(tokens[33]);
                psb.append(","+freezer);
                rack = insertNull(tokens[34]);
                psb.append(","+rack);
                box = insertNull(tokens[35]);
                psb.append(","+box);
                row = insertNull(map.get(tokens[36]));
                psb.append(","+row);
                col = insertNull(tokens[37]);
                psb.append(","+col+"\n");

                //4th DNA sample
                biospecimenUid = parentId +"-" +tokens[38];
                psb.append(biospecimenUid);
                freezer = insertNull(tokens[41]);
                psb.append(","+freezer);
                rack = insertNull(tokens[42]);
                psb.append(","+rack);
                box = insertNull(tokens[43]);
                psb.append(","+box);
                row = insertNull(map.get(tokens[44]));
                psb.append(","+row);
                col = insertNull(tokens[45]);
                psb.append(","+col+"\n");

                //5th DNA sample
                biospecimenUid = parentId +"-" +tokens[46];
                psb.append(biospecimenUid);
                freezer = insertNull(tokens[50]);
                psb.append(","+freezer);
                rack = insertNull(tokens[51]);
                psb.append(","+rack);
                box = insertNull(tokens[49]);
                psb.append(","+box);
                row = insertNull(map.get(tokens[52]));
                psb.append(","+row);
                col = insertNull(tokens[53]);
                psb.append(","+col+"\n");

            }

            System.out.println(psb.toString());

        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        try{
            File file = new File("DNA_ALLOCATION.csv");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(psb.toString());
            bw.close();

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("--------------------------DNA Allocation Done----------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String insertNull(String val){
        String result="\\N";
        if(val!=null && val.length()>0 && isAlphaNumeric(val)){
            result = val;
        }
        return  result;
    }

    public static boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }
}