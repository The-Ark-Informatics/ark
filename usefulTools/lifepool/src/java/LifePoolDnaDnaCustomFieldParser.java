import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 27/02/13
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class LifePoolDnaDnaCustomFieldParser {
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
                int count=0;
                String[] tokens = dataLine.split("[,]");
                String participantId=tokens[0];

                String specimenId = tokens[1];

                String parentId =  participantId.substring(0,2).trim()+" "+participantId.substring(2).trim()+" "+specimenId.trim();

                String biospecimenUid = parentId +"-" +tokens[34];

                //customfield 1
                psb.append(biospecimenUid);
                psb.append(","+ (++count));
                String value = tokens[5];
                psb.append(","+value+"\n");

                //customfield 2
                psb.append(biospecimenUid);
                psb.append(","+ (++count));
                value = tokens[7];
                psb.append(","+value+"\n");

                //customfield 3
                psb.append(biospecimenUid);
                psb.append(","+ (++count));
                value = tokens[8];
                psb.append(","+value+"\n");


                //customfield 4
                psb.append(biospecimenUid);
                psb.append(","+ (++count));
                value = tokens[9];
                psb.append(","+value+"\n");

            }

            System.out.println(psb.toString());

        }catch (Exception e){
            e.printStackTrace();
        }


        try{
            File file = new File("DNA_DNA_CUSTOM_FIELD.csv");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(psb.toString());
            bw.close();

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("--------------------------Processed DNA to DNA Custom Fields Done--------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
