import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 25/02/13
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class LifepoolDnaToDnaParser {
    public static void main(String[] args) {
        BufferedReader br = null;
        StringBuffer psb=new StringBuffer();
        try{
            br=new BufferedReader(new FileReader(args[0]));
            String dataLine = null;
            boolean firstLine = false;


            while ((dataLine = br.readLine()) != null) {
                if(!firstLine){
                    firstLine=true;
                    continue;
                }
                String[] tokens = dataLine.split("[,]");

                //1st DNA sample
                String participantId=tokens[0];
                psb.append(participantId);

                String specimenId = tokens[1];

                String parentId =  participantId.substring(0,2).trim()+" "+participantId.substring(2).trim()+" "+specimenId.trim();
                String parentUid=parentId+"-500";
                psb.append(","+parentUid);

                String biospecimenUid = parentId +"-" +tokens[10];
                psb.append(","+biospecimenUid);

                String volumeTxt = tokens[11];
                double volume =   Double.parseDouble(volumeTxt);
                psb.append(","+toMl(volume));
                String concentrationTxt = tokens[12];

                double concentration = Double.parseDouble(concentrationTxt);
                psb.append(","+toMl(concentration)+"\n");

                //2nd DNA sample
                psb.append(participantId);
                psb.append(","+parentUid);
                biospecimenUid = parentId +"-" +tokens[16];
                psb.append(","+biospecimenUid);
                volumeTxt = tokens[17];
                volume =   parseDouble(volumeTxt);
                psb.append(","+toMl(volume));
                concentrationTxt = tokens[18];
                concentration = parseDouble(concentrationTxt);
                psb.append(","+toMl(concentration)+"\n");

                //3rd DNA sample
                psb.append(participantId);
                psb.append(","+parentUid);
                biospecimenUid = parentId +"-" +tokens[22];
                psb.append(","+biospecimenUid);
                volumeTxt = tokens[23];
                volume =   parseDouble(volumeTxt);
                psb.append(","+toMl(volume));
                concentrationTxt = tokens[24];
                concentration = parseDouble(concentrationTxt);
                psb.append(","+toMl(concentration)+"\n");

                //4th DNA sample
                psb.append(participantId);
                psb.append(","+parentUid);
                biospecimenUid = parentId +"-" +tokens[28];
                psb.append(","+biospecimenUid);
                volumeTxt = tokens[29];
                volume =   parseDouble(volumeTxt);
                psb.append(","+toMl(volume));
                concentrationTxt = tokens[30];
                concentration = parseDouble(concentrationTxt);
                psb.append(","+toMl(concentration)+"\n");

                //5th DNA sample
                psb.append(participantId);
                psb.append(","+parentUid);
                biospecimenUid = parentId +"-" +tokens[34];
                psb.append(","+biospecimenUid);
                volumeTxt = tokens[36];
                volume =   parseDouble(volumeTxt);
                psb.append(","+toMl(volume));
                concentrationTxt = tokens[38];
                concentration = parseDouble(concentrationTxt);
                psb.append(","+toMl(concentration)+"\n");


            }

            System.out.println(psb.toString());

        }catch (Exception e){
            e.printStackTrace();
        }


        try{
            File file = new File("DNA_DNA_SPECIMEN.csv");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(psb.toString());
            bw.close();

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("--------------------------Processed biospecimen DNA to DNA Done--------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static double parseDouble(String txt){
        double value=0;
        try{
            value = Double.parseDouble(txt);
         }
         catch (Exception e){

         }
        return  value;
    }

    private static String toMl(double  value){
        NumberFormat nf= new DecimalFormat("#0.0000");
        return nf.format((value/1000));

    }
}
