import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: thilina
 * Date: 3/12/12
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class LifePool {
    public static void main(String[] args) {
        System.out.println(" ------------------- Lifepool parser --------------------------------");


        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();

        try {

            String dataLine;

            br = new BufferedReader(new FileReader(args[0]));
            boolean firstLine=false;


            StringBuffer psb=new StringBuffer();


            while ((dataLine = br.readLine()) != null) {
                if(!firstLine){
                    firstLine=true;
                    continue;
                }
                String[] tokens = dataLine.split("[,]");

                int length =tokens.length;

                if(length>0){

                String id =  tokens[0];


                if(id.length()>0){

                    psb.setLength(0);

                    psb.append(id + ",");
                    psb.append(tokens[1]+",");
                    psb.append(tokens[2]+",");
                    psb.append(tokens[3]+",");
                    psb.append(tokens[4]+",");
                    psb.append(tokens[5]+",");
                    psb.append(tokens[6]+",");
                    psb.append(tokens[7]+",");
                    psb.append(tokens[8]+",");
                    psb.append(tokens[9]);

                    sb.append(dataLine+"\n");
                }
                else {
                    StringBuffer tmpSb = new StringBuffer();
                    for(int i=10;i<tokens.length;++i){
                        tmpSb.append(","+tokens[i]);
                    }

                   sb.append(psb.toString()+tmpSb.toString()+"\n");
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
        System.out.println(" ---------------------Remove empty line---------------------------");
        System.out.println(" ----------------------------------------------------------------------------");
        System.out.println(" ----------------------------------------------------------------------------");

        System.out.println(sb.toString());

        try{
            File file = new File(args[1]);

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
            System.out.println("--------------------------Done-------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
