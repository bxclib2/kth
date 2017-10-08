# Read and Write PFiles
#
# Requires the pfile_utils in the path. On KTH machines run
# module add quicknet pfile_utils
# If you are performing Lab 3B in the DT2118 Speech and Speaker Recognition course, the
# above commands are automatically run when you run source tools/modules
#
# (C) 2016 Giampiero Salvi <giampi@kth.se>

import subprocess
import numpy as np
import sys
import os
import cPickle, gzip
from sklearn import metrics
import matplotlib.pyplot as plt

def pfile_read(filename):
    """ Reads the data in the PFile filename

    Returns 4 arrays:
    utt_ids: utterance ids repeated for every frame (num_frames)
    frame_ids: frame ids (num_frames)
    features: array (num_frames, num_features) of feature values
    labels: array (num_frames, num_labels) of labels (usually num_labels=1) 

    See also pfile_write   
    """
    proc = subprocess.Popen('pfile_info -i '+filename, shell=True, stdout=subprocess.PIPE)
    res = proc.stdout.read()
    _, info = res.splitlines()
    infoa = info.split(' ')
    num_utts = int(infoa[0])
    num_frames = int(infoa[2])
    num_labels = int(infoa[4])
    num_features = int(infoa[6])
    proc = subprocess.Popen('pfile_print -q -i '+filename, shell=True, stdout=subprocess.PIPE)
    data = np.loadtxt(proc.stdout)
    utt_ids = data[:,0]
    frame_ids = data[:,1]
    features = data[:,2:(num_features+2)]
    labels = data[:, (num_features+2):]
    assert labels.shape[1] == num_labels
    return utt_ids, frame_ids, features, labels


pred_file = sys.argv[1]
pfile = sys.argv[2]

if '.gz' in pred_file:
    pred_mat = cPickle.load(gzip.open(pred_file, 'rb'))
else:
    pred_mat = cPickle.load(open(pred_file, 'rb'))

# load the testing set to get the labels
print pfile
utt_ids, frame_ids, test_data, test_labels = pfile_read(pfile)
#test_labels = test_labels.astype(np.int32)

frame_mpost = np.argmax(pred_mat, axis=1)
print 'frame post: %s' % frame_mpost[:10]
frame_labels = np.transpose(test_labels).flatten()
print 'frame labels: %s' % frame_labels[:10]


frame_err = np.count_nonzero(frame_mpost - frame_labels) / len(frame_mpost)
# output the final error rate
print 'Error rate is ' + str(frame_err) + '%'

plt.figure()
plot_confusion_matrix(frame_cm_normalized, title="Frame level confusion matrix for " + info)
plt.show()

# phoneme level evaluation

phoneme_mpost = np.array([mapping[x] for x in frame_mpost])
phoneme_labels = np.array([mapping[x] for x in frame_labels])

phoneme_err = np.count_nonzero(phoneme_mpost - phoneme_labels) / len(phoneme_mpost)
print(phoneme_err)

phoneme_cm = metrics.confusion_matrix(phoneme_labels, phoneme_mpost)
phoneme_cm_normalized = phoneme_cm.astype('float') / phoneme_cm.sum(axis=1)[:, np.newaxis]

plt.figure()
plot_confusion_matrix(phoneme_cm_normalized, title="Phoneme level confusion matrix for " + info)
plt.show()


def plot_confusion_matrix(cm, title='Confusion matrix', cmap=plt.cm.Blues):
    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    plt.tight_layout()
    plt.ylabel('True label')
    plt.xlabel('Predicted label')
