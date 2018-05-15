package heapfile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class hashquery {
    public static List<String> index = new ArrayList<>();
    public static HashMap<Integer, String> hashtable = new HashMap<>();
    public void loadIndex() {
        Scanner in;
        try {
            in = new Scanner(new File("indexFile.txt"));
            while (in.hasNext()) {
                String line = in.nextLine();
                String[] tokens = line.split("\t");
                Integer hashcode = Integer.valueOf(tokens[0]);

                hashtable.put(hashcode, tokens[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getRecord(byte[] buffer, String str, int p_num, int pageSize) {
        String[] tokens = str.split(",");
        String[] pos1 = tokens[0].split("\\|");
        if (Long.parseLong(pos1[0]) > (p_num + 1) * pageSize) {
            return "";
        }
        String[] pos8 = tokens[8].split("\\|");
        if (Long.parseLong(pos8[1]) > (p_num + 1) * pageSize) {
            return "";
        }
        try {
            long base = p_num * pageSize;
            String resisiter_name = getField(buffer, pos1, base);
            String bn_name = getField(buffer, tokens[1].split("\\|"), base);
            String bn_status = getField(buffer, tokens[2].split("\\|"), base);
            String bn_reg_dt = getField(buffer, tokens[3].split("\\|"), base);
            String bn_cancel_dt = getField(buffer, tokens[4].split("\\|"), base);
            String bn_renew_dt = getField(buffer, tokens[5].split("\\|"), base);
            String bn_state_num = getField(buffer, tokens[6].split("\\|"), base);
            String bn_state_of_reg = getField(buffer, tokens[7].split("\\|"), base);
            Long ibn_abn = getLong(buffer, tokens[8].split("\\|"), base);

            return resisiter_name + "\t" + bn_name + "\t" + bn_status + "\t" + bn_reg_dt +
                    "\t" + bn_cancel_dt + "\t" + bn_renew_dt + "\t" + bn_state_num + "\t" +
                    bn_state_num + "\t" + bn_state_of_reg + "\t" + ibn_abn;
        } catch (Exception e) {

        }
        return "";
    }

    public String getField(byte[] buffer, String[] pos, long base) {
        int s1 = (int) (Long.parseLong(pos[0]) - base);
        int s2 = (int) (Long.parseLong(pos[1]) - base);
        byte[] bt = new byte[s2 - s1];
        for (int i = s1; i < s2; i++) {
            bt[i - s1] = buffer[i];
        }
        return new String(bt);
    }

    public Long getLong(byte[] buffer, String[] pos, long base) {
        int s1 = (int) (Long.parseLong(pos[0]) - base);
        int s2 = (int) (Long.parseLong(pos[1]) - base);
        byte[] bt = new byte[s2 - s1];
        for (int i = s1; i < s2; i++) {
            bt[i - s1] = buffer[i];
        }
        ByteBuffer buffer2 = ByteBuffer.allocate(Long.BYTES);
        buffer2.put(bt);
        buffer2.flip();
        return buffer2.getLong();
    }

    public void query(String text, int pageSize) {
        FileInputStream input = null;
        Integer hashcode  = text.hashCode();
        String str = hashtable.get(hashcode);

        String out = "";
        if(str != null) {
            Long f_start = Long.valueOf(str.split(",")[0].split("|")[0]);

            long p_cnt = f_start/pageSize;


            try {
                input = new FileInputStream("heap.4096");
                byte[] buffer = new byte[pageSize];
                long count = p_cnt;
                while(count >= 0){
                    int len = 0;
                    try {
                        len = input.read(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (len == -1) {
                        break;
                    }
                    count--;
                }
                out = getRecord(buffer, str, (int)p_cnt, pageSize);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println(out);

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    //    if (args.length < 2) {
		//	System.exit(-1);
		//}
		//String text = args[0];
		int pageSize = 4096;

        hashquery myQuery = new hashquery();
        myQuery.loadIndex();
        long startTime = System.currentTimeMillis();
        myQuery.query("HARVEY NORMAN AV/IT SUPERSTORE CANNINGTON", 4096);
        long endTime = System.currentTimeMillis();
        System.out.println("query costs： " + (endTime - startTime) + "ms");
    }
}
