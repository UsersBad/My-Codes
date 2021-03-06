//Java version was a fakesolve, this time its a realsolve.
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
    if(a < 0) {
        a += MOD;
    }
    if(a >= MOD){
        a -= MOD;
    }
    return a;
}
 
int sub(int a, int b){
    a -= b;
    if(a < 0) a += MOD;
    if(a >= MOD) a -= MOD;
    return a;
}
 
int mult(int a, int b){
    return ((ll) a * b) % MOD;
}
 
void setIO2() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
}

void setIO(string s) {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    freopen((s + ".in").c_str(), "r", stdin);
    freopen((s + ".out").c_str(), "w", stdout);
}

int fact[100001];

int power(int x, int y) 
{ 
    int res = 1; 
    while (y > 0) 
    { 
        if (y & 1) 
            res = mult(res, x); 
        y = y>>1;
        x = mult(x, x);  
    } 
    return res; 
}

const int mxN = 100005;
int tree[mxN + 5];
void update(int index, int val) {
    index++;
    while(index < mxN + 5) {
        tree[index] += val;
        index += index & -index;
    }
}
int query(int index) {
    int ret = 0;
    index++;
    while(index > 0) {
        ret += tree[index];
        index -= index & -index;
    }
    return ret;
}
int rquery(int a, int b) {
    return query(b)-query(a-1);
}
void rupdate(int l, int r, int x){
    update(l, x);
    update(r + 1, -x);
}
const int mxL = 1e9;

int main(){
    setIO("tracking2");
//    setIO2();
    int N, K; cin >> N >> K;
    int c[N + 1 - K];
    for(int i = 0; i < N + 1 - K; ++i){
        cin >> c[i];
    }
    int know[N];
    fill(know, know + N, -1);
    for(int i = 0; i < N - K; ++i){
        if(c[i] == c[i + 1]) continue;
        if(c[i] > c[i + 1]) {
            know[i + K] = c[i + 1];
        } else{
            know[i] = c[i];
        }
    }
    int dom[N];
    multiset<int> wmins;
    for(int i = 0; i < N; ++i){
        if(i < N + 1 - K) wmins.insert(c[i]);
        if(i >= K) wmins.erase(wmins.lower_bound(c[i - K]));
        dom[i] = *prev(wmins.end());
    }
    vector<int> segs;
    int ind = 0;
    int last = ind;
    for(; ind < N; ){
        while(ind < N && dom[ind] == dom[last]) ++ind;
        int end = ind - 1;
        int sz = end - last + 1;
        int lb = dom[last];
        int ways = mxL - lb + 1;
        for(int i = 0; i <= sz; ++i){
            rupdate(i, i, -query(i));
        }
        int dp[sz + 1];
        int sum = 1;
        dp[0] = 1;
        for(int i = 1; i <= min(sz, K - 1); ++i){
            dp[i] = sum;
            if(know[last + i - 1] == -1) {
                rupdate(0, i - 1, 1);
                sum = mult(sum, ways - 1);
            } else{
                sum = 0;
                rupdate(0, i - 1, -1e5);
            }
            sum = add(sum, dp[i]);
        }
        for(int i = K; i <= sz; ++i){
            dp[i] = sum;
            if(query(i - K) >= 0) sum = sub(sum, mult(dp[i - K], power(ways - 1, query(i - K))));
            if(know[last + i - 1] == -1) {
                rupdate(0, i - 1, 1);
                sum = mult(sum, ways - 1);
            } else{
                sum = 0;
                rupdate(0, i - 1, -1e5);
            }
            sum = add(sum, dp[i]);
        }
        assert(sum > 0);
        segs.eb(sum);
        last = ind;
    }
    int ans = 1;
    for(int i : segs){
        ans = mult(i, ans);
    }
    cout << ans << "\n";
}
