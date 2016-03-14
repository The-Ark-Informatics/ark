import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 4/12/12
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */

public class LifePoolGeneralDataParser {
    public static void main(String[] args) {
        System.out.println("Lifepool collection data");



        BufferedReader br = null;
        StringBuffer sampleBuffer = new StringBuffer();
        StringBuffer collectionBuffer = new StringBuffer();


        Set<Integer> A = new  TreeSet<Integer>();
        Set<Integer> B = new  TreeSet<Integer>();
        Set<Integer> C = new  TreeSet<Integer>();
        Set<String> D = new  TreeSet<String>();

        try {

            String dataLine;

            br = new BufferedReader(new FileReader(args[0]));
            boolean firstLine=false;

            boolean selectNextLine = false;

            StringBuffer psb=new StringBuffer();

            String[] dates =null;

            String preSample=null;


            while ((dataLine = br.readLine()) != null) {
                if(!firstLine){
                    firstLine=true;
                    continue;
                }
                String[] tokens = dataLine.split("[,]");

                int length =tokens.length;

                if(length>0){

                    String id =  tokens[0];

                    A.add(Integer.valueOf(tokens[13]) );
                    B.add(Integer.valueOf(tokens[14]) );
                    C.add(Integer.valueOf(tokens[15]) );
                    D.add(tokens[16]);

                    if(id.length()>0){

                        psb.setLength(0);

                        psb.append(id + ",");
                        psb.append(tokens[1]+",");
                        psb.append(tokens[2]+",");
//                        psb.append(tokens[3]+",");
//                        psb.append(dateParser(tokens[4])+",");
//                        psb.append(tokens[5]+",");
//                        psb.append(tokens[6]+",");
//                        psb.append(collectionId+",");
//                        psb.append(dateParser(tokens[7])+",");
                        psb.append(dateParser(tokens[8])+",");
                        psb.append(dateParser(tokens[9]));

                        String sampleUID = tokens[11];



                        String formattedsampleUID = sampleUID.substring(0,sampleUID.length()-5);

                        String volumeParam = tokens[10];
                        String[] volumeParams = volumeParam.split("x");
                        volumeParam = volumeParams[1].replaceAll("(?i)ml","").trim();

                        collectionBuffer.append(tokens[2]+",");

                        collectionBuffer.append(collectionUidParser(tokens[2],tokens[7])+",");


                        collectionBuffer.append(dateParser(tokens[7])+"\n");;

                        if(!volumeParams[0].startsWith("1")){

                          sampleBuffer.append(psb.toString() + "," + volumeParam +","+formattedsampleUID+" 90" + "\n");
                          sampleBuffer.append(psb.toString() + "," + volumeParam +","+formattedsampleUID+" 91"+ "\n");

                        }else{
                          selectNextLine =true;
                          preSample=volumeParam;

//                          sampleBuffer.append(psb.toString() + "," + volumeParam +","+sampleUID+"0"+ "\n");
                        }

                    }
                    else if(selectNextLine){
                        String volumeParam = tokens[10];
                        String[] volumeParams=volumeParam.split("x");

                        String sampleUID = tokens[11];

                        sampleUID = sampleUID.substring(0,sampleUID.length()-5);

                        if(volumeParams.length>1){
                            volumeParam = volumeParams[1].replaceAll("(?i)ml","").trim();
                            if(Integer.parseInt(preSample) > Integer.parseInt(volumeParam)){
                                sampleBuffer.append(psb.toString() + "," + preSample +","+sampleUID+" 90"+ "\n");
                                sampleBuffer.append(psb.toString() + "," + volumeParam +","+sampleUID+" 91"+ "\n");
                            }else{
                                sampleBuffer.append(psb.toString() + "," + volumeParam +","+sampleUID+" 90"+ "\n");
                                sampleBuffer.append(psb.toString() + "," + preSample +","+sampleUID+" 91"+ "\n");
                            }
                            selectNextLine = false;
                        }
                    }
                    System.out.println(dataLine);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ---------------------Collection Buffer---------------------------");
        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");

        System.out.println(collectionBuffer.toString());


        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ---------------------Sample Buffer---------------------------");
        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");


        System.out.println(sampleBuffer.toString());


        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");



        System.out.println(" ------------------------------Freezer---------------------------------------");

        for(Integer a :A){
            System.out.println(a);
        }
        System.out.println(" ------------------------------Rack---------------------------------------");

        for(Integer b :B){
            System.out.println(b);
        }

        System.out.println(" ------------------------------Box---------------------------------------");
        for(Integer c :C){
            System.out.println(c);
        }

        System.out.println(" ------------------------------Location---------------------------------------");
        for(String d :D){
            System.out.println(d);
        }




        try{
            File file = new File("COLLECTION.csv");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(collectionBuffer.toString());
            bw.close();

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("--------------------------Collection Done-------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            File file = new File("SPECIMEN.csv");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sampleBuffer.toString());
            bw.close();

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("--------------------------Specimen Done-------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String dateParser(String date){
        SimpleDateFormat collectionsdf= new SimpleDateFormat("yyyy-MM-dd");
        String[] dates =null;
        Calendar cal = Calendar.getInstance();
        String result="\\N";
        if(date!=null && date.trim().length()>0){

            dates = date.split("[/]");

            cal.set(Integer.parseInt(dates[2]),Integer.parseInt(dates[1] ),Integer.parseInt(dates[0]));

           result=collectionsdf.format(cal.getTime());
        }

        return result;
    }

    private static String collectionUidParser(String subjectUid, String date){
        SimpleDateFormat collectionsdf= new SimpleDateFormat("yyyyMMdd");
        String[] dates =null;
        Calendar cal = Calendar.getInstance();
        String result="\\N";
        if(date!=null && date.trim().length()>0){

            dates = date.split("[/]");

            cal.set(Integer.parseInt(dates[2]),Integer.parseInt(dates[1] ),Integer.parseInt(dates[0]));

            result="B-"+subjectUid+"-"+collectionsdf.format(cal.getTime());
        }

        return result;
    }
}
