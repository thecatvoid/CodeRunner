import sympy

size = 11
grid = [[' ' for _ in range(size)] for _ in range(size)]
x, y = size // 2, size // 2
num = 1

for step in range(1, size, 2):
    for _ in range(step):
        grid[y][x] = '#' if sympy.isprime(num) else '.'
        x += 1
        num += 1
    for _ in range(step):
        grid[y][x] = '#' if sympy.isprime(num) else '.'
        y += 1
        num += 1
    step += 1
    for _ in range(step):
        grid[y][x] = '#' if sympy.isprime(num) else '.'
        x -= 1
        num += 1
    for _ in range(step):
        grid[y][x] = '#' if sympy.isprime(num) else '.'
        y -= 1
        num += 1

for row in grid:
    print(" ".join(row))

