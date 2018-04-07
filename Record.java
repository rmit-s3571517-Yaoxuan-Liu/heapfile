public class Record {
	long pos; // the offset of record
	Field[] fields;
	
	byte[] content;
	
	public Record() {
		pos = 0;
		fields = new Field[9];
		this.content = new byte[2];
	}
	
	public Record(String[] tokens) {
		fields = new Field[9];
		int cnt_length = 0;
		this.fields[0] = new Field(tokens[0]);
		cnt_length += this.fields[0].content.length;
		this.fields[1] = new Field(tokens[1]);
		cnt_length += this.fields[1].content.length;
		this.fields[2] = new Field(tokens[2]);
		cnt_length += this.fields[2].content.length;
		this.fields[3] = new Field(tokens[3]);
		cnt_length += this.fields[3].content.length;
		this.fields[4] = new Field(tokens[4]);
		cnt_length += this.fields[4].content.length;
		this.fields[5] = new Field(tokens[5]);
		cnt_length += this.fields[5].content.length;
		this.fields[6] = new Field(tokens[6]);
		cnt_length += this.fields[6].content.length;
		this.fields[7] = new Field(tokens[7]);
		cnt_length += this.fields[7].content.length;
		this.fields[8] = new Field(Long.parseLong(tokens[8]));
		cnt_length += this.fields[8].content.length;
		this.content = new byte[cnt_length];
		int index = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < this.fields[i].content.length; j++) {
				this.content[index++] = this.fields[i].content[j];
			}
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
