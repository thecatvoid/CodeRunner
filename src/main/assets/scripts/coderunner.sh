#!/data/data/com.termux/files/usr/bin/bash

# coderunner.sh: A POSIX-compatible script for executing code files in multiple languages
# --------------------------------------------------------------------
# This script provides a framework for executing code files in various
# programming languages, now using file extensions.

# --------------------------------------------------------------------
# Environment and Configuration Variables
# --------------------------------------------------------------------
BASE_DIRECTORY="${HOME}/coderunner/"
BASE_DIRECTORY_SCRIPTS="${BASE_DIRECTORY}/scripts/"
RUNTIME_OUTPUT_PATH="/sdcard/coderunner/output/coderunner_termux_output.log"
PROJECT_NAME="coderunner"
LOG_FILE="${BASE_DIRECTORY}/coderunner.log"
DEFAULT_EXTENSION="sh"  # Default extension if none specified
UNIQUE_ID="$(date +%s)$$"
OPTIONAL_ARGS=""

# --------------------------------------------------------------------
# Utility Functions
# --------------------------------------------------------------------

# Display error message
print_error_message() {
    printf '[ERROR] %s\n' "$1" >&2
}

# Display informational message
print_info_message() {
    printf '[INFO] %s\n' "$1"
}

# Show usage information and list supported extensions
display_help_and_exit() {
    if [ -n "$1" ]; then
        print_error_message "$1"
        printf '\n'
    fi

    echo 'coderunner.sh - Execute code files in multiple programming languages'
    echo 'Usage:'
    echo '    ./coderunner.sh [-l extension] <input_file>'
    echo 'Supported Extensions:'
    echo '    sh      - POSIX shell script (default)'
    echo '    c       - C programming language'
    echo '    cpp     - C++ programming language'
    echo '    java    - Java programming language'
    echo '    py      - Python programming language'
    echo '    js      - JavaScript (Node.js)'
    echo '    rb      - Ruby programming language'
    echo '    lua     - Lua programming language'
    echo '    rs      - Rust programming language'
    echo 'Options:'
    echo '    -l <extension>   Specify the programming language extension'
    echo '    -h               Display this help message'

    exit 1
}

# Create directory if it doesn't exist
ensure_directory_exists() {
    if [ ! -d "$1" ]; then
        mkdir -p "$1" || {
            print_error_message "Failed to create directory: $1"
            exit 1
        }
    fi
}

# Write message to log file
write_to_log() {
    printf '%s - %s\n' "$(date '+%Y-%m-%d %H:%M:%S')" "$1" >>"${LOG_FILE}"
}

# Check if file exists and is readable
check_file_exists() {
    if [ ! -f "$1" ]; then
        print_error_message "File not found: $1"
        exit 1
    fi
    if [ ! -r "$1" ]; then
        print_error_message "File not readable: $1"
        exit 1
    fi
}

# --------------------------------------------------------------------
# Language Handler Functions
# --------------------------------------------------------------------

handle_sh_file() {
    local script_file="$1"
    shift
    bash "$script_file" "$@"
}

handle_c_file() {
    local source_file="$1"
    local output_file="${BASE_DIRECTORY}${PROJECT_NAME}.${UNIQUE_ID}"
    shift

    if ! gcc -o "$output_file" "$source_file"; then
        print_error_message "C compilation failed"
        exit 1
    fi

    "$output_file" "$@"
    rm -f "$output_file"
}

handle_cpp_file() {
    local source_file="$1"
    local output_file="${BASE_DIRECTORY}${PROJECT_NAME}.${UNIQUE_ID}"
    shift

    if ! g++ -o "$output_file" "$source_file"; then
        print_error_message "C++ compilation failed"
        exit 1
    fi

    "$output_file" "$@"
    rm -f "$output_file"
}

handle_java_file() {
    local source_file="$1"
    local class_name=$(basename "$source_file" .java)
    local class_dir="${BASE_DIRECTORY}classes"
    shift

    ensure_directory_exists "$class_dir"

    if ! javac -d "$class_dir" "$source_file"; then
        print_error_message "Java compilation failed"
        exit 1
    fi

    cd "$class_dir" && java "$class_name" "$@"
    rm -rf "$class_dir"
}

handle_python_file() {
    local script_file="$1"
    shift
    python3 "$script_file" "$@"
}

handle_nodejs_file() {
    local script_file="$1"
    shift
    node "$script_file" "$@"
}

handle_ruby_file() {
    local script_file="$1"
    shift
    ruby "$script_file" "$@"
}

handle_lua_file() {
    local script_file="$1"
    shift
    lua "$script_file" "$@"
}

handle_rust_file() {
    local source_file="$1"
    local output_file="${BASE_DIRECTORY}${PROJECT_NAME}.${UNIQUE_ID}"
    shift

    if ! rustc -o "$output_file" "$source_file"; then
        print_error_message "Rust compilation failed"
        exit 1
    fi

    "$output_file" "$@"
    rm -f "$output_file"
}

# --------------------------------------------------------------------
# Main Execution Functions
# --------------------------------------------------------------------

# Execute file based on the specified extension
execute_file() {
    local extension="$1"
    local input_file="$2"
    shift 2

    write_to_log "Executing file:"
    write_to_log "Extension: ${extension}"
    write_to_log "Input file: ${input_file}"
    write_to_log "Unique ID: ${UNIQUE_ID}"

    case "$extension" in
        sh)   handle_sh_file "$input_file" "$@" ;;
        c)    handle_c_file "$input_file" "$@" ;;
        cpp)  handle_cpp_file "$input_file" "$@" ;;
        java) handle_java_file "$input_file" "$@" ;;
        py)   handle_python_file "$input_file" "$@" ;;
        js)   handle_nodejs_file "$input_file" "$@" ;;
        rb)   handle_ruby_file "$input_file" "$@" ;;
        lua)  handle_lua_file "$input_file" "$@" ;;
        rs)   handle_rust_file "$input_file" "$@" ;;
        *)
            print_error_message "Unsupported extension: ${extension}"
            exit 1
            ;;
    esac
}

# Main entry point
main() {
    local extension="$DEFAULT_EXTENSION"
    local input_file=""

    while getopts "hl:" opt; do
        case $opt in
            h)
                display_help_and_exit
                ;;
            l)
                extension="$OPTARG"
                ;;
            ?)
                display_help_and_exit "Invalid option"
                ;;
        esac
    done

    shift $((OPTIND - 1))

    if [ $# -eq 0 ]; then
        display_help_and_exit "No input file provided"
    fi

    input_file="$1"
    shift

    check_file_exists "$input_file"

    ensure_directory_exists "$BASE_DIRECTORY"
    ensure_directory_exists "$BASE_DIRECTORY_SCRIPTS"

    execute_file "$extension" "$input_file" "$@"
}

main "$@" 2>&1 | tee "${RUNTIME_OUTPUT_PATH}"
