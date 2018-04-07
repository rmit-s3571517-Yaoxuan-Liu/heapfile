import java.util.ArrayList;
import java.util.List;

public class Pages {
	List<Byte> contents = new ArrayList<Byte>();
	
	public Pages(List<Record> records) {
		for (int i = 0; i < records.size(); i++) {
			for (int j = 0; j < records.get(i).content.length; j++) {
				this.contents.add(records.get(i).content[j]);
			}
		}
	}

}
