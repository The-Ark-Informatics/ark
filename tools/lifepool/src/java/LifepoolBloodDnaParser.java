import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 19/02/13
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class LifepoolBloodDnaParser {
    public static void main(String[] args) {
        BufferedReader br = null;
        StringBuffer psb=new StringBuffer();
        try{
            br=new BufferedReader(new FileReader(args[0]));
            String dataLine=null;
            boolean firstLine=false;


            while ((dataLine = br.readLine()) != null) {
                if(!firstLine){
                    firstLine=true;
                    continue;
                }
                String[] tokens = dataLine.split("[,]");
                String participantId=tokens[0];
                psb.append(participantId);

                String specimenId = tokens[1];

                String parentId=  participantId.substring(0,2).trim()+" "+participantId.substring(2).trim()+" "+specimenId.trim();
                psb.append(","+parentId);

                String biospecimenUid=parentId+"-500";
                psb.append(","+biospecimenUid);

                String volumeTxt = tokens[6];

                String[] volumeArray=volumeTxt.split("x");
                double volumeCount =    Double.parseDouble(volumeArray[0].trim());
                double volume =   Double.parseDouble(volumeArray[1].trim().substring(0,volumeArray[1].length()-3));
                double totalVolume =   (volumeCount * volume)/1000;

                psb.append(","+totalVolume);

                String purity = tokens[8];

                psb.append(","+purity+"\n");

            }

            System.out.println(psb.toString());

        }catch (Exception e){
            e.printStackTrace();
        }


        try{
            File file = new File("BLOOD_DNA_SPECIMEN.csv");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(psb.toString());
            bw.close();

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("--------------------------Processed biospecimen Blood to DNA Done--------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
