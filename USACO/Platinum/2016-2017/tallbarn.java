import java.util.*;
import java.io.*;

public class tallbarn{
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };

	static int N;

	static class Reader {
		final private int BUFFER_SIZE = 1 << 16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer, bytesRead;

		public Reader() {
			din = new DataInputStream(System.in);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public Reader(String file_name) throws IOException {
			din = new DataInputStream(new FileInputStream(file_name));
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public String readLine() throws IOException {
			byte[] buf = new byte[360]; // line length
			int cnt = 0, c;
			while ((c = read()) != -1) {
				if (c == '\n')
					break;
				buf[cnt++] = (byte) c;
			}
			return new String(buf, 0, cnt);
		}

		public int nextInt() throws IOException {
			int ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (neg)
				return -ret;
			return ret;
		}

		public long nextLong() throws IOException {
			long ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');
			if (neg)
				return -ret;
			return ret;
		}

		public double nextDouble() throws IOException {
			double ret = 0, div = 1;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();

			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (c == '.') {
				while ((c = read()) >= '0' && c <= '9') {
					ret += (c - '0') / (div *= 10);
				}
			}

			if (neg)
				return -ret;
			return ret;
		}

		private void fillBuffer() throws IOException {
			bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			if (bytesRead == -1)
				buffer[0] = -1;
		}

		private byte read() throws IOException {
			if (bufferPointer == bytesRead)
				fillBuffer();
			return buffer[bufferPointer++];
		}

		public void close() throws IOException {
			if (din == null)
				return;
			din.close();
		}
	}
	static long K;
	static long floors[];
	
	static long count(double x) {
		long ret = 0;
		for(int i = 0; i < N; ++i) {
			ret += (long)((Math.sqrt(1 + 4.0 * floors[i] / x) - 1) / 2.0);
		}
		return ret;
	}
	
	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("tallbarn.in");
		PrintWriter out = new PrintWriter(new File("tallbarn.out"));
		N = in.nextInt();
		K = in.nextLong();
		K -= N;
		floors = new long[N];
		for(int i = 0; i < N; ++i) {
			floors[i] = in.nextLong();
		}
		double l = 0, r = Long.MAX_VALUE / 2;
		for(int i = 0; i < 200; ++i) {
			double x = (l + r) / 2.0;
			long amt = count(x);
			if(amt >= K) {
				l = x;
			}
			else {
				r = x;
			}
		}
		double ans = 0;
		long total = 0;
		for(int i = 0; i < N; ++i) {
			long x = (long)((Math.sqrt(1 + 4.0 * floors[i] / l) - 1) / 2.0);
			ans += 1.0 * floors[i] / (x + 1);
			total += x;
		}
		out.println(Math.round(ans - (K - total) * l));
		in.close();
		out.close();
	}
}