from animals import *
import numpy as np

weights = np.random.uniform(0, 1, size=(100, 84))

max_epochs = 1
for epoch in xrange(max_epochs):
    for animal in xrange(len(snames)):
        animal_name = snames[animal]
        properties = props[animal]
        diff = np.abs(properties - weights)
        min_diff_indices = np.argmin(diff, axis=1)
        min_diff_idx = np.argmin(min_diff_indices)
        min_diff = weights[min_diff_idx]
        #print('diff: {}'.format(diff))
        print('min: {}'.format(min_diff))
        print('min: {}'.format(weights.min(axis=1)))
        print('min: {}'.format(min_diff_idx))
        print('min shape: {}'.format(min_diff.shape))

        print('is equal: {}'.format(weights.min(axis=1) == min_diff))




