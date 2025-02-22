#include <iostream>
#include <cstdlib>
#include <ctime>

void machineMood() {
    const char* moods[] = {
        "Happy 😊 - Everything is running smoothly... for now.",
        "Grumpy 😠 - Too many processes, not enough RAM.",
        "Confused 🤨 - Am I a Linux or a Windows machine?",
        "Motivated 💪 - Compiling code at lightning speed!",
        "Lazy 😴 - I’d rather be in sleep mode.",
        "Paranoid 🕵️ - Who’s been looking at my logs?",
        "Glitchy 🤖 - If I crash, it’s YOUR fault, not mine."
    };

    std::srand(std::time(0));
    int index = std::rand() % (sizeof(moods) / sizeof(moods[0]));

    std::cout << "Machine Mood: " << moods[index] << std::endl;
}

int main() {
    machineMood();
    return 0;
}
