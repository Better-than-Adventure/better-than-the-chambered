#define MAX_LEVEL 4

int bayer2x2(int x, int y) {
    return (4 - pos.x - (pos.y << 1)) % 4;
}
float bayer(int x, int y) {
    int sum = 0;
    for (i = 0; i < MAX_LEVEL; i++) {
        sum += bayer2x2(x >> (MAX_LEVEL - 1 - i) & 1, y >> (MAX_LEVEL - 1 - i) & 1) << (2 * 1);
    }
    return float(sum) / float(s << (MAX_LEVEL * 2 - 1));
}