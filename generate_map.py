import os
import re

def generate_context_map():
    context_map = []
    class_pattern = re.compile(r'(public|abstract|final)?\s*(class|interface|enum)\s+(\w+)')
    
    # FORCE the script to look only in the source code folder
    # This bypasses any issues with parent folder names
    target_dir = os.path.join(".", "core", "src")
    
    print(f"--- FORCING SCAN IN: {os.path.abspath(target_dir)} ---")

    if not os.path.exists(target_dir):
        print("ERROR: Could not find 'core/src'. Are you sure you are in the project root?")
        return ""

    for dirpath, dirnames, filenames in os.walk(target_dir):
        for filename in filenames:
            if filename.endswith(".java"):
                full_path = os.path.join(dirpath, filename)
                # Make path relative to the project root for readability
                rel_path = os.path.relpath(full_path, ".")
                
                try:
                    with open(full_path, 'r', encoding='utf-8', errors='ignore') as f:
                        content = f.read()
                        
                        # Grab package and class name
                        package_match = re.search(r'package\s+([\w\.]+);', content)
                        package = package_match.group(1) if package_match else "default"
                        
                        class_match = class_pattern.search(content)
                        if class_match:
                            c_type = class_match.group(2)
                            c_name = class_match.group(3)
                            entry = f"- {rel_path} -> [{package}.{c_name}] ({c_type})"
                            context_map.append(entry)
                except Exception as e:
                    print(f"Error reading {filename}: {e}")

    return "\n".join(sorted(context_map))

if __name__ == "__main__":
    result = generate_context_map()
    
    if len(result) > 0:
        with open("project_map.txt", "w", encoding="utf-8") as f:
            f.write("PROJECT STRUCTURE MAP:\n")
            f.write("======================\n")
            f.write(result)
        print(f"SUCCESS! Map generated with {len(result.splitlines())} entries.")
        print("Check 'project_map.txt' now.")
    else:
        print("Still found 0 files. This is very strange.")