import java.io.*;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 11/12/12
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */

public class LifePoolAllocationDataParser {
    public static void main(String[] args) {
        System.out.println("----------------------- Allocations -----------------------------------------------------");
        BufferedReader br = null;

        String dataLine;
        boolean firstLine=false;

        StringBuffer sb = new StringBuffer();

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

        try{
            br = new BufferedReader(new FileReader(args[0]));
            while ((dataLine = br.readLine()) != null) {
                if(!firstLine){
                    firstLine=true;
                    continue;
                }
                String[] tokens = dataLine.split("[,]");

                int length =tokens.length;
                if(length>0){

                    String sampleUID = tokens[11];
                    String preUID = sampleUID.substring(0,sampleUID.length()-5);
                    String sufUID = sampleUID.substring(sampleUID.length()-4,sampleUID.length());

                    sb.append(preUID+" "+sufUID+",");
                    sb.append(tokens[13] + ",");
                    sb.append(tokens[14] + ",");
                    sb.append(tokens[15] + ",");

                    String location = tokens[16];

                    sb.append(map.get(location.substring(0,1)) + ",");
                    sb.append(location.substring(1) + "\n");

                }

            }

        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        System.out.println(sb.toString());

        try{
            File file = new File("ALLOCATION.csv");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sb.toString());
            bw.close();

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("--------------------------Allocation Done--------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
