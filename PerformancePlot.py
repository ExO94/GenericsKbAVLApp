# Student Number: FRTETH003
# Name: Ethan Fortuin
# Date: 28/03/25

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

data = pd.read_csv('results.csv')

# Create a figure with two subplots
fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(15, 6))

# Plot insert comparisons
ax1.plot(data['n'], data['InsertMinComps'], 'g-', label='Best Case')
ax1.plot(data['n'], data['InsertAvgComps'], 'b-', label='Average Case')
ax1.plot(data['n'], data['InsertMaxComps'], 'r-', label='Worst Case')

# Plot theoretical complexity for insert (log n)
x = np.array(data['n'])
ax1.plot(x, np.log2(x), 'k--', label='O(log n)')

ax1.set_title('AVL Tree Insert Operation')
ax1.set_xlabel('Dataset Size (n)')
ax1.set_ylabel('Number of Comparisons')
ax1.legend()
ax1.set_xscale('log')
ax1.grid(True)

# Plot search comparisons
ax2.plot(data['n'], data['SearchMinComps'], 'g-', label='Best Case')
ax2.plot(data['n'], data['SearchAvgComps'], 'b-', label='Average Case')
ax2.plot(data['n'], data['SearchMaxComps'], 'r-', label='Worst Case')

# Plot theoretical complexity for search (log n)
ax2.plot(x, np.log2(x), 'k--', label='O(log n)')

ax2.set_title('AVL Tree Search Operation')
ax2.set_xlabel('Dataset Size (n)')
ax2.set_ylabel('Number of Comparisons')
ax2.legend()
ax2.set_xscale('log')
ax2.grid(True)

plt.tight_layout()
plt.savefig('avl_performance.png', dpi=300)
plt.show()