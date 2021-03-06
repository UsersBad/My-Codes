//#pragma GCC optimize("Ofast")
//#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
//#pragma GCC optimization ("unroll-loops")
#include <bits/stdc++.h>
using namespace std;
 
using ll = long long;
using ull = unsigned ll;
using pii = pair<int, int>;
 
#define pb push_back
#define mp make_pair
#define eb emplace_back
#define all(x) (x).begin(),(x).end()
#define x first
#define y second
 
const int MOD = 1e9 + 7;
const int dx[] = {0, 0, 1, -1};
const int dy[] = {1, -1, 0, 0}; 
const char dir[] = {'R', 'L', 'D', 'U'};
 
int add(int a, int b){
    a += b;
    if(a >= MOD){
        a -= MOD;
    }
    return a;
}
 
int sub(int a, int b){
    a -= b;
    if(a < 0) a += MOD;
    return a;
}
 
int mult(int a, int b){
    return ((ll) a * b) % MOD;
}
 
void setIO2() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
//    freopen((s+".in").c_str(),"r",stdin);
//    freopen((s+".text").c_str(),"w",stdout);
}

void setIO(string s) {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	freopen((s + ".in").c_str(), "r", stdin);
	freopen((s + ".out").c_str(), "w", stdout);
}

const int mxN = 2005;

int g[mxN][mxN], prefr[mxN][mxN], prefc[mxN][mxN], prefr2[mxN][mxN], prefc2[mxN][mxN];
int dp[mxN][mxN][2];
int N;
int p2[mxN * mxN];

int main(){
    setIO("sprinklers2");
//    setIO2();
    cin >> N;
    for(int i = 0; i < N; ++i){
        string s; cin >> s;
        for(int j = 0; j < N; ++j){
            g[i][j] = s[j] == 'W' ? 1 : 0;
        }
    }
    int pt = 1, pw = 0;
    p2[0] = 1;
    while(pw <= N * N){
        pt = mult(pt, 2);
        ++pw;
        p2[pw] = pt;
    }
    for(int i = 0; i < N; ++i){
        int amt = 0;
        for(int j = 0; j < N; ++j){
            prefr[i][j + 1] = amt;
            amt += 1 - g[i][j];
            prefr2[i][j + 1] = amt;
        }
        amt = 0;
        for(int j = 0; j < N; ++j){
            prefc[i][j + 1] = amt;
            amt += 1 - g[j][i];
            prefc2[i][j + 1] = amt;
        }
    }
    //0 is from above, 1 is from left
    dp[1][0][0] = 1;
    dp[0][1][1] = 1;
    for(int i = 0; i <= N; ++i){
        for(int j = 0; j <= N; ++j){
            if(!i && !j) continue;
            //go down
            if(i < N){
                dp[i + 1][j][0] = add(dp[i + 1][j][0], mult(dp[i][j][0], p2[prefr2[i][j]]));
                if(!g[i][j - 1]) dp[i + 1][j][0] = add(dp[i + 1][j][0], mult(dp[i][j][1], p2[prefr[i][j]]));
            }
            //go right
            if(j < N){
                dp[i][j + 1][1] = add(dp[i][j + 1][1], mult(dp[i][j][1], p2[prefc2[j][i]]));
                if(!g[i - 1][j]) dp[i][j + 1][1] = add(dp[i][j + 1][1], mult(dp[i][j][0], p2[prefc[j][i]]));
            }
        }
    }
    cout << add(dp[N][N][0], dp[N][N][1]) << "\n";
}
