def obfuscate_code(code)
  code.chars.shuffle.join
end

puts obfuscate_code("def hello_world puts 'Hello, World!' end")

