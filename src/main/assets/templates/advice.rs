use std::time::{SystemTime, UNIX_EPOCH};

fn pseudo_random_index(max: usize) -> usize {
    let now = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_secs();
    (now % max as u64) as usize // Generates an index within range
}

fn main() {
    let advice_list = [
        "Save your work. Future-you will thank you.",
        "Restart your system once in a while. It likes a fresh start too.",
        "Maybe touch some grass? The screen will be here when you return.",
        "Too many browser tabs won’t make you smarter—just more lost.",
        "Syntax errors build character. Debugging them builds patience.",
        "Update your software, but maybe not *immediately*.",
        "Take a break. Even CPUs throttle under heavy load.",
    ];

    let index = pseudo_random_index(advice_list.len());
    println!("💡 Machine's Advice: {}", advice_list[index]);
}
