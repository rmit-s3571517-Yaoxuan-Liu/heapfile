import java.nio.ByteBuffer;

public class Field {
	byte[] content;
	
	public Field() {
		this.content = new byte[2];
	}
	
	public Field(long num) {
		this.content = long2byte(num);
	}
	
	public Field(String str) {
		this.content = str2byte(str);
	}
	
	public byte[] long2byte(long num) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(num);
	    return buffer.array();
	}
	
	public byte[] str2byte(String str) {
		return str.getBytes();
	}
	
	public static void main(String[] args) {
		Field f = new Field("asdf");

		System.out.println(f.content);
	}
}
