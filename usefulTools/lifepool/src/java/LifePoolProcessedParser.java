import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 11/12/12
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LifePoolProcessedParser {
    public static void main(String[] args) {
        System.out.println("----------------------- Processed  -----------------------------------------------------");
        BufferedReader br = null;

        String dataLine;

        StringBuffer sb = new StringBuffer();


        try{
            br = new BufferedReader(new FileReader(args[0]));
            while ((dataLine = br.readLine()) != null) {

                String[] tokens = dataLine.split("[,]");

                String sampleUid = tokens[tokens.length-1];

                String volume = tokens[tokens.length-2];

//                double vol = Double.parseDouble(volume)/2;
                double vol = 0.5;

                StringBuffer tmpSb=new StringBuffer();
                for( int i =0; i< tokens.length;++i){
                    if(i==5){
                        tmpSb.append(vol+",");
                    }else if(i==tokens.length-1){
                        tmpSb.append(tokens[i]);
                    }
                    else{
                        tmpSb.append(tokens[i]+",");
                    }

                }

                String processedOne = tmpSb.toString()+"-1,"+sampleUid+"\n";
                String processedTwo = tmpSb.toString()+"-2,"+sampleUid+"\n";

                sb.append(processedOne);
                sb.append(processedTwo);



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
            File file = new File("PROCESSED_SPECIMEN.csv");

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
            System.out.println("--------------------------Allocation Done-------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
