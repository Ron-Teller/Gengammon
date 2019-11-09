package BG;

public class Move {
	public static int Bar = -1;
	public static int Bearing = -2;
	private int source;  // checker is moved from this point
	private int dest;    // checker is moved to this point
	
	public Move(int _source, int _dest) {
		source = _source;
		dest = _dest;
	}
	
	public Move(Move move) {
		source = move.getSource();
		dest = move.getDest();
	}
	
	public int getSource() {
		return source;
	}
	
	public int getDest() {
		return dest;
	}
	
	public String toString() {
		String string = "";
		String src_str;
		String dest_str;
		
		src_str = (source == Bar) ? "Bar" : String.valueOf(source);
		dest_str = (dest == Bearing) ? "Bearing" : String.valueOf(dest);
		string = ("<"+src_str+","+dest_str+">");
		return string;
	}
	
	
}
