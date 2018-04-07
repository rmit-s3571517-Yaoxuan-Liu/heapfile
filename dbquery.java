
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

public class dbquery {
	public static List<String> index = new ArrayList<>();
	
	public void loadIndex() {
		Scanner in;
		try {
			in = new Scanner(new File("indexFile.txt"));
			while (in.hasNext()) {
	        		String line = in.nextLine();
	        		index.add(line);
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
        try {  
            input = new FileInputStream("heap.4096");  
            byte[] buffer = new byte[pageSize]; 
            int p_cnt = 0;
            while (true) {  
                int len = input.read(buffer);  
                if (len == -1) {  
                    break;  
                }  
                String bufStr = new String(buffer);
                if (bufStr.contains(text)) {
                		long p = p_cnt * pageSize + bufStr.indexOf(text);
                		for (String str: index) {
                			if (str.contains(Long.toString(p))) {
                				String record = getRecord(buffer, str, p_cnt, pageSize);
                				System.out.println(record);
                				break;
                			}
                			
                		}
                }
                p_cnt += 1;
            }  
        } catch (Exception e) {  
        		e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 2) {
			System.exit(-1);
		}
		String text = args[0];
		int pageSize = Integer.parseInt(args[1]);
		dbquery myQuery = new dbquery();
		myQuery.loadIndex();
		myQuery.query("Warby Wares", pageSize);
	}

}
