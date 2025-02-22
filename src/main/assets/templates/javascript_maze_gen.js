function generateMaze(size) {
    let maze = Array(size).fill().map(() => Array(size).fill("#"));
    for (let y = 1; y < size - 1; y += 2) {
        for (let x = 1; x < size - 1; x += 2) {
            maze[y][x] = ".";
            if (Math.random() > 0.5) maze[y + 1][x] = ".";
            else maze[y][x + 1] = ".";
        }
    }
    console.log(maze.map(row => row.join(" ")).join("\n"));
}

generateMaze(11);

