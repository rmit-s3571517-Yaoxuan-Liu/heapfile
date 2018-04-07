import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class dbload {
	
	public static long offSet = 0;
	public static List<Record> records = new ArrayList<>();
	public static List<Pages> pages = new ArrayList<>();
	
	public static StringBuilder indexFile = new StringBuilder();
	
	
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
	 * @param records2
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
		if (args.length < 3) {
			System.exit(-1);
		}
		int pageSize = Integer.parseInt(args[1]);
		String outpath = args[2];
		String csv_file = "BUSINESS_NAMES_201804.csv";
		dbload myLoad = new dbload();
		myLoad.loadRecords(csv_file);
		System.out.println("records is " + records.size());
		myLoad.loadPages(records);
		System.out.println("pages is " + pages.size());
        // start time
        long startTime = System.currentTimeMillis();   
		myLoad.writeHeapFile(outpath + "." + pageSize);
        long endTime = System.currentTimeMillis(); 
        System.out.println("inserting the data costs： " + (endTime - startTime) + "ms");		
	}

}
