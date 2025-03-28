# Student Number: FRTETH003
# Name: Ethan Fortuin
# Date: 28/03/25

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Load data from CSV file
data = pd.read_csv('results.csv')

# Create a figure with two subplots
fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(15, 6))

# Create evenly spaced positions for x-axis
positions = np.arange(len(data['n']))

# Plot insert comparisons on first subplot
ax1.plot(positions, data['InsertMinComps'], 'g-', label='Best Case', marker='o')
ax1.plot(positions, data['InsertAvgComps'], 'b-', label='Average Case', marker='s')
ax1.plot(positions, data['InsertMaxComps'], 'r-', label='Worst Case', marker='^')

# Plot theoretical complexity for insert (log n)
x = np.array(data['n'])
theoretical_log = np.log2(x)
# Scale the theoretical line to match the data's magnitude
scale_factor = np.max(data['InsertAvgComps']) / np.max(theoretical_log)
ax1.plot(positions, theoretical_log * scale_factor, 'k--', label='O(log n)')

ax1.set_title('AVL Tree Insert Operation')
ax1.set_xlabel('Dataset Size (n)')
ax1.set_ylabel('Number of Comparisons')
ax1.legend()
ax1.set_xticks(positions)
ax1.set_xticklabels(data['n'], rotation=45)
ax1.grid(True)

# Plot search comparisons on second subplot
ax2.plot(positions, data['SearchMinComps'], 'g-', label='Best Case', marker='o')
ax2.plot(positions, data['SearchAvgComps'], 'b-', label='Average Case', marker='s')
ax2.plot(positions, data['SearchMaxComps'], 'r-', label='Worst Case', marker='^')

# Plot theoretical complexity for search (log n)
# Scale the theoretical line for search subplot
scale_factor_search = np.max(data['SearchAvgComps']) / np.max(theoretical_log)
ax2.plot(positions, theoretical_log * scale_factor_search, 'k--', label='O(log n)')

ax2.set_title('AVL Tree Search Operation')
ax2.set_xlabel('Dataset Size (n)')
ax2.set_ylabel('Number of Comparisons')
ax2.legend()
ax2.set_xticks(positions)
ax2.set_xticklabels(data['n'], rotation=45)
ax2.grid(True)

plt.tight_layout()
plt.savefig('avl_performance.png', dpi=300)
plt.show()
