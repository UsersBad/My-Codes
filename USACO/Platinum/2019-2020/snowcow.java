import java.util.*;
import java.io.*;

public class snowcow {
	final static int MOD = 1000000007;
	final static int intMax = 1000000000;
	final static int intMin = -1000000000;
	final static int[] DX = { 0, 0, -1, 1 };
	final static int[] DY = { -1, 1, 0, 0 };

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
	
	static ArrayList<Integer>[] adj;
	static int[] preorder, size;
	static int ind;
	static int MAX_D = 17;
	static int MAX_N = 100000;
	static int[] depth = new int[MAX_N];
	static int[][] lca = new int[MAX_N][MAX_D];
	static void dfssetup(int curr, int par) {
		preorder[ind++] = curr;
		++size[curr];
		for(int out: adj[curr]) {
			if(out == par)
				continue;
			depth[out] = depth[curr] + 1;
			lca[out][0] = curr;
			dfssetup(out, curr);
			size[curr] += size[out];
		}
	}
	static int LCA(int a, int b) {
		if(depth[a] < depth[b]) {
			int temp = a;
			a = b;
			b = temp;
		}
	  	for(int d = MAX_D-1; d >= 0; d--) {
	  		if(depth[a] - (1<<d) >= depth[b]) {
	  			a = lca[a][d];
	  		}
	  	}
	  	for(int d = MAX_D-1; d >= 0; d--) {
	  		if(lca[a][d] != lca[b][d]) {
	  			a = lca[a][d];
	  			b = lca[b][d];
	    	}
	  	}
	  	if(a != b) {
		  	a = lca[a][0];
	    	b = lca[b][0];
	  	}
	  	return a;
	}

	static void initLCA() {
		for(int d = 1; d < MAX_D; d++) {
			for(int i = 0; i < MAX_N; i++) {
				lca[i][d] = lca[lca[i][d-1]][d-1];
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
//		Reader in = new Reader();
		Reader in = new Reader("snowcow.in");
		PrintWriter out = new PrintWriter(new File("snowcow.out"));
		int N = in.nextInt();
		int Q = in.nextInt();
		adj = new ArrayList[N];
		for(int i = 0; i < N; ++i) {
			adj[i] = new ArrayList<Integer>();
		}
		for(int i = 0; i < N - 1; ++i) {
			int a = in.nextInt() - 1;
			int b = in.nextInt() - 1;
			adj[a].add(b);
			adj[b].add(a);
		}
		ind = 0;
		preorder = new int[N];
		size = new int[N];
		dfssetup(0, -1);
		initLCA();
		int[] actualtopreorder = new int[N];
		for(int i = 0; i < N; ++i) {
			actualtopreorder[preorder[i]] = i;
		}
		LazySegmentTree lst = new LazySegmentTree(N);
		HashSet<Integer>[] pastQueries = new HashSet[100001];
		for(int i = 0; i <= 100000; ++i) {
			pastQueries[i] = new HashSet<Integer>();
		}
		l: for(int i = 0; i < Q; ++i) {
			int tp = in.nextInt();
			if(tp == 1) {
				int x = in.nextInt() - 1;
				int c = in.nextInt();
				HashSet<Integer> removeList = new HashSet<Integer>();
				for(Integer j : pastQueries[c]) {
					int lca = LCA(x, j);
					if(lca == j) continue l;
					if(lca != x) continue;
					lst.update(actualtopreorder[j], actualtopreorder[j] + size[j] - 1, -1);
					removeList.add(j);
				}
				for(Integer j : removeList) {
					pastQueries[c].remove(j);
				}
				pastQueries[c].add(x);
				lst.update(actualtopreorder[x], actualtopreorder[x] + size[x] - 1, 1);
			}
			else {
				int x = in.nextInt() - 1;
				out.println(lst.sumQuery(actualtopreorder[x], actualtopreorder[x] + size[x] - 1));
			}
		}
		in.close();
		out.close();
	}
	static class LazySegmentTree {
		private long[] min;
		private long[] lazy;
		private long[] sum;
		private long[] max;
		private int size;
		public LazySegmentTree(int size) {
			this.size = size;
			min = new long[4*size];
			sum = new long[4*size];
			max = new long[4*size];
			lazy = new long[4*size];
		}
		public void update(int l, int r, int inc) {
			update(1, 0, size-1, l, r, inc);
		}
		private void pushDown(int index, int l, int r) {
			min[index] += lazy[index];
			max[index] += lazy[index];
			sum[index] += lazy[index] * (r-l+1);
			if(l != r) {
				lazy[2*index] += lazy[index];
				lazy[2*index+1] += lazy[index];
			}
			lazy[index] = 0;
		}
		private void pullUp(int index, int l, int r) {
			int m = (l+r)/2;
			min[index] = Math.min(evaluateMin(2*index, l, m), evaluateMin(2*index+1, m+1, r));
			max[index] = Math.max(evaluateMax(2*index, l, m), evaluateMax(2*index+1, m+1, r));
			sum[index] = evaluateSum(2*index, l, m) + evaluateSum(2*index+1, m+1, r);
		}
		private long evaluateSum(int index, int l, int r) {
			return sum[index] + (r-l+1)*lazy[index];
		}
		private long evaluateMin(int index, int l, int r) {
			return min[index] + lazy[index];
		}
		private long evaluateMax(int index, int l, int r) {
			return max[index] + lazy[index];
		}
		private void update(int index, int l, int r, int left, int right, int inc) {
			if(r < left || l > right) return;
			if(l >= left && r <= right) {
				lazy[index] += inc;
				return;
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			update(2*index, l, m, left, right, inc);
			update(2*index+1, m+1, r, left, right, inc);
			pullUp(index, l, r);
		}
		public long minQuery(int l, int r) {
			return minQuery(1, 0, size-1, l, r);
		}
		private long minQuery(int index, int l, int r, int left, int right) {
			if(r < left || l > right) return Long.MAX_VALUE;
			if(l >= left && r <= right) {
				return evaluateMin(index, l, r);
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			long ret = Long.MAX_VALUE;
			ret = Math.min(ret, minQuery(2*index, l, m, left, right));
			ret = Math.min(ret, minQuery(2*index+1, m+1, r, left, right));
			pullUp(index, l, r);
			return ret;
		}
		public long sumQuery(int l, int r) {
			return sumQuery(1, 0, size-1, l, r);
		}
		private long sumQuery(int index, int l, int r, int left, int right) {
			if(r < left || l > right) return 0;
			if(l >= left && r <= right) {
				return evaluateSum(index, l, r);
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			long ret = 0;
			ret += sumQuery(2*index, l, m, left, right);
			ret += sumQuery(2*index+1, m+1, r, left, right);
			pullUp(index, l, r);
			return ret;
		}
		public long maxQuery(int l, int r) {
			return maxQuery(1, 0, size-1, l, r);
		}
		private long maxQuery(int index, int l, int r, int left, int right) {
			if(r < left || l > right) return Long.MIN_VALUE;
			if(l >= left && r <= right) {
				return evaluateMax(index, l, r);
			}
			pushDown(index, l, r);
			int m = (l+r)/2;
			long ret = 0;
			ret = Math.max(ret, maxQuery(2*index, l, m, left, right));
			ret = Math.max(ret, maxQuery(2*index+1, m+1, r, left, right));
			pullUp(index, l, r);
			return ret;
		}
	}
}