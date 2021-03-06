import java.util.*;
import java.io.*;

public class team {
	final static int MOD = 1000000009;
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

	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("team.in");
		PrintWriter out = new PrintWriter(new File("team.out"));
		N = in.nextInt();
		int M = in.nextInt();
		int K = in.nextInt();
		int[] j = new int[N];
		int[] p = new int[M];
		for(int i = 0; i < N; ++i) {
			j[i] = in.nextInt();
		}
		for(int i = 0; i < M; ++i) {
			p[i] = in.nextInt();
		}
		long[][][] dp = new long[K + 1][N + 1][M + 1];
		for(int k = 0; k <= N; ++k) {
			Arrays.fill(dp[0][k], 1);
		}
		for(int i = 1; i <= K; ++i) {
			for(int k = 1; k <= N; ++k) {
				for(int l = 1; l <= M; ++l) {
					dp[i][k][l] = dp[i - 1][k - 1][l - 1] * (j[k - 1] > p[l - 1] ? 1 : 0) + dp[i][k - 1][l] + dp[i][k][l - 1] - dp[i][k - 1][l - 1];
					dp[i][k][l] = (dp[i][k][l] % MOD + MOD) % MOD;
				}
			}			
		}
		out.println(dp[K][N][M]);
		in.close();
		out.close();
	}
}