package heapfile;
import java.io.*;
import java.util.*;


public class hashload {
    public static long offSet = 0;
    public static List<Record> records = new ArrayList<>();
    public static List<Pages> pages = new ArrayList<>();
    public static HashMap<Integer, String> hashtable = new HashMap<>();
    /**
     * load records
     * @param csv_file
     */
    public void loadRecords(String csv_file) {
        try {
            PrintWriter pw = new PrintWriter(new File("indexFile.txt"));
            Scanner in = new Scanner(new File(csv_file));
            // remove header
            in.nextLine();
            while (in.hasNext()){
                String line = in.nextLine();
                String[] tokens = line.split("\t");
                if (tokens.length < 9) {
                    continue;
                }
                Record record = new Record(tokens);

                records.add(record);
                StringBuilder sb = new StringBuilder();
                sb.append(tokens[1].hashCode());
                sb.append("\t");

                for (int i = 0; i < record.fields.length; i++) {
                    long f_start = offSet;
                    long f_end = offSet + record.fields[i].content.length;
                    sb.append(f_start);
                    sb.append("|");
                    sb.append(f_end);
                    sb.append(",");
                    offSet = f_end;
                }
                pw.println(sb.substring(0, sb.length() - 1));
       
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }

    /**
     * load pages
     * @param
     */
    public void loadPages(List<Record> records) {
        int byte_cnt = 0;
        int i = 0;
        List<Record> tmp = new ArrayList<Record>();


        while (i < records.size()) {
            if (byte_cnt + records.get(i).content.length <= 4096) {
                byte_cnt += records.get(i).content.length;
                tmp.add(records.get(i));
            } else {
                Pages page = new Pages(tmp);
                pages.add(page);
                byte_cnt = 0;
                tmp = new ArrayList<>();
                tmp.add(records.get(i));
            }
            i += 1;
        }
    }

    /**
     * write heapfile
     * @param outfile
     */
    public void writeHeapFile(String outfile) {
        File out = new File(outfile);
        if (out.exists() && out.isFile()){
            out.delete();
        }
        try {
            if (out.createNewFile()){
                for (int i = 0; i < pages.size(); i++) {
                    DataOutputStream outWriter = new DataOutputStream(new FileOutputStream(outfile, true));
                    byte[] bytes = new byte[pages.get(i).contents.size()];
                    for (int j = 0; j < bytes.length; j++) {
                        bytes[j] = pages.get(i).contents.get(j);
                    }
                    outWriter.write(bytes);
                    outWriter.close();
                }

            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
//    	if (args.length < 3) {
//            System.exit(-1);
//        }
        int pageSize = 4096;
        String outpath = "heap";
        String csv_file = "BUSINESS_NAMES_201804.csv";
        hashload myLoad = new hashload();
        myLoad.loadRecords(csv_file);
        System.out.println("records is " + records.size());
        myLoad.loadPages(records);
        System.out.println("pages is " + pages.size());
        // start time
        long startTime = System.currentTimeMillis();
        myLoad.writeHeapFile("heap" + "." + 4096);
        long endTime = System.currentTimeMillis();
        System.out.println("inserting the data costs： " + (endTime - startTime) + "ms");
    }
}
